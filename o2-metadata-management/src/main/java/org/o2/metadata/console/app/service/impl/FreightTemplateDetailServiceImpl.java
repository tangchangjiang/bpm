package org.o2.metadata.console.app.service.impl;

import com.google.common.collect.Maps;
import org.apache.commons.collections4.CollectionUtils;
import org.o2.metadata.console.api.dto.RegionQueryLovDTO;
import org.o2.metadata.console.api.vo.FreightTemplateManagementVO;
import org.o2.metadata.console.app.bo.FreightDetailBO;
import org.o2.metadata.console.app.service.FreightCacheService;
import org.o2.metadata.console.app.service.FreightTemplateDetailService;
import org.o2.metadata.console.infra.constant.FreightConstants;
import org.o2.metadata.console.infra.entity.FreightTemplateDetail;
import org.o2.metadata.console.infra.entity.Region;
import org.o2.metadata.console.infra.repository.FreightTemplateDetailRepository;
import org.o2.metadata.console.infra.repository.FreightTemplateRepository;
import org.o2.metadata.console.infra.repository.RegionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 运费模板明细默认实现
 *
 * @author peng.xu@hand-china.com 2019/5/17
 */
@Service
public class FreightTemplateDetailServiceImpl extends AbstractFreightCacheOperation implements FreightTemplateDetailService {
    private FreightTemplateDetailRepository freightTemplateDetailRepository;
    private FreightCacheService freightCacheService;
    private RegionRepository regionRepository;

    public FreightTemplateDetailServiceImpl(FreightTemplateDetailRepository freightTemplateDetailRepository,
                                            FreightCacheService freightCacheService,
                                            RegionRepository regionRepository,
                                            FreightTemplateRepository freightTemplateRepository,
                                            RegionRepository regionRepository1) {
        this.freightTemplateDetailRepository = freightTemplateDetailRepository;
        this.freightCacheService = freightCacheService;
        this.regionRepository = regionRepository1;
        super.regionRepository = regionRepository;
        super.freightTemplateRepository = freightTemplateRepository;
    }

    @Override
    public List<FreightTemplateDetail> batchInsert(List<FreightTemplateDetail> freightTemplateDetailList, boolean isRegion) {
        checkData(freightTemplateDetailList, false, isRegion);
        List<FreightTemplateDetail> freightTemplateDetails = freightTemplateDetailRepository.batchInsertSelective(freightTemplateDetailList);

        // 更新缓存
        saveFreightDetailCache(freightTemplateDetails);

        return freightTemplateDetails;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<FreightTemplateDetail> batchUpdate(List<FreightTemplateDetail> freightTemplateDetailList, boolean isRegion) {
        checkData(freightTemplateDetailList, true, isRegion);
        FreightTemplateDetail defaultDetail = containUniqleDefault(freightTemplateDetailList, isRegion);
        if (defaultDetail != null) {
            List<FreightTemplateDetail> otherDetailtList = freightTemplateDetailRepository.queryOtherDefaultFreightTemplateDetail(defaultDetail);
            otherDetailtList.forEach(item -> {
                item.setDefaultFlag(0);
            });
            freightTemplateDetailRepository.batchUpdateByPrimaryKey(otherDetailtList);
        }
        List<FreightTemplateDetail> freightTemplateDetails = freightTemplateDetailRepository.batchUpdateByPrimaryKey(freightTemplateDetailList);

        //更新缓存
        saveFreightDetailCache(freightTemplateDetails);

        return freightTemplateDetails;
    }

    @Override
    public void batchDelete(List<FreightTemplateDetail> regionFreightDetailDisplayLis) {

        //需要前端显示的格式转成后端数据库需要的格式： 前端把地区合并了！！！
        FreightTemplateManagementVO freightTemplateManagementVO =  new FreightTemplateManagementVO();
        final List<FreightTemplateDetail> regionDetailListInput ;
        //  默认运费模板不需要处理； 地区运费模板要转化一下
        if (regionFreightDetailDisplayLis.get(0).getDefaultFlag() ==1 ){
            regionDetailListInput =regionFreightDetailDisplayLis;
        }else{
            regionDetailListInput =   freightTemplateManagementVO.exchangeRegionDetailDisplay2DBlist(regionFreightDetailDisplayLis);
        }
        freightTemplateManagementVO.setRegionFreightTemplateDetails(regionDetailListInput);

        freightTemplateDetailRepository.batchDeleteByPrimaryKey(regionDetailListInput);
        // 删除缓存
        deleteFreightDetailCache(regionDetailListInput);
    }

    @Override
    public List<FreightTemplateDetail> queryDefaultFreightTemplateDetail(Long templateId) {
        List<FreightTemplateDetail> details = freightTemplateDetailRepository.queryDefaultFreightTemplateDetail(templateId);
        Map<String,Region>  map = getRegionMap(details);
        for (FreightTemplateDetail detail : details) {
            Region region = map.get(detail.getRegionCode());
            detail.setRegionName(region.getRegionName());
        }
        return details;
    }

    @Override
    public List<FreightTemplateDetail> queryRegionFreightTemplateDetail(Long templateId) {
        List<FreightTemplateDetail> details = freightTemplateDetailRepository.queryRegionFreightTemplateDetail(templateId);
        Map<String,Region>  map = getRegionMap(details);
        for (FreightTemplateDetail detail : details) {
            Region region = map.get(detail.getRegionCode());
            detail.setRegionName(region.getRegionName());
            detail.setParentRegionName(region.getParentRegionName());
        }
        return details;
    }

    /**
     * 批量更新运费模板明细
     *
     * @param freightTemplateDetailList 更新的运费模板明细列表
     * @param isRegion                  是否属于指定区域的运费模板明细列表
     * @return
     */
    private List<FreightTemplateDetail> batchMerge(List<FreightTemplateDetail> freightTemplateDetailList, boolean isRegion) {
        final Map<String, Object> map = new HashMap<>(freightTemplateDetailList.size());
        final List<FreightTemplateDetail> updateList = new ArrayList<>();
        final List<FreightTemplateDetail> insertList = new ArrayList<>();

        for (int i = 0; i < freightTemplateDetailList.size(); i++) {
            final FreightTemplateDetail detail = freightTemplateDetailList.get(i);
            if (isRegion) {
                detail.regionDetailValidate();
            } else {
                detail.defaultDetailValidate();
            }

            // list验重
            String key = String.valueOf(detail.getRegionCode()) + String.valueOf(detail.getTemplateId());
            Assert.isTrue(map.get(key) == null, FreightConstants.ErrorCode.BASIC_DATA_FREIGHT_DETAIL_DUNPLICATE);
            map.put(key, i);

            // 数据库验重
            Assert.isTrue(!detail.exist(freightTemplateDetailRepository, isRegion), FreightConstants.ErrorCode.BASIC_DATA_FREIGHT_DETAIL_DUNPLICATE);

            if (detail.getTemplateDetailId() != null) {
                updateList.add(detail);
            } else {
                insertList.add(detail);
            }
        }

        final List<FreightTemplateDetail> resultList = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(updateList)) {
            resultList.addAll(freightTemplateDetailRepository.batchUpdateByPrimaryKey(updateList));
        }
        if (CollectionUtils.isNotEmpty(insertList)) {
            resultList.addAll(freightTemplateDetailRepository.batchInsertSelective(insertList));
        }

        return resultList;
    }

    /**
     * 校验数据
     *
     * @param freightTemplateDetailList 需要校验的运费模板明细列表
     * @param isCheckId                 是否校验ID
     * @param isRegion                  是否属于指定区域的运费模板明细列表
     */
    private void checkData(List<FreightTemplateDetail> freightTemplateDetailList, boolean isCheckId, boolean isRegion) {
        Map<String, Object> map = new HashMap<>(freightTemplateDetailList.size());
        for (int i = 0; i < freightTemplateDetailList.size(); i++) {
            FreightTemplateDetail freightTemplateDetail = freightTemplateDetailList.get(i);

            // 主键验空
            if (isCheckId) {
                Assert.notNull(freightTemplateDetail.getTemplateDetailId(), FreightConstants.ErrorCode.BASIC_DATA_FREIGHT_DETAIL_ID_IS_NULL);
            }

            if (isRegion) {
                //  指定地区的运费模板明细需要校验regionId不为空
                freightTemplateDetail.regionDetailValidate();
            } else {
                freightTemplateDetail.defaultDetailValidate();
            }

            // list验重
            String key =  String.valueOf(freightTemplateDetail.getRegionCode()+"") + String.valueOf(freightTemplateDetail.getTemplateId());
            Assert.isTrue(map.get(key) == null, FreightConstants.ErrorCode.BASIC_DATA_FREIGHT_DETAIL_DUNPLICATE);
            map.put(key, i);

            // 数据库验重
            Assert.isTrue(!freightTemplateDetail.exist(freightTemplateDetailRepository, isRegion), FreightConstants.ErrorCode.BASIC_DATA_FREIGHT_DETAIL_DUNPLICATE);
        }
    }

    /**
     * 获取运费模板明细列表中，默认的运费模板明细
     *
     * @param freightTemplateDetailList 待更新的运费模板明细列表
     * @param isRegion                  是否属于指定区域的运费模板明细列表
     * @return 默认的运费模板明细
     */
    private FreightTemplateDetail containUniqleDefault(List<FreightTemplateDetail> freightTemplateDetailList, boolean isRegion) {
        if (isRegion) {
            return null;
        }

        int defaultCount = 0;
        FreightTemplateDetail defaultFreightTemplateDetail = null;
        for (FreightTemplateDetail detail : freightTemplateDetailList) {
            if (detail.getDefaultFlag() != null && detail.getDefaultFlag() == 1) {
                defaultFreightTemplateDetail = detail;
            }
        }

        return defaultFreightTemplateDetail;
    }

    /**
     * 新增更新运费模板明细缓存
     *
     * @param freightTemplateDetailList 待更新缓存的运费模板明细列表
     */
    private void saveFreightDetailCache(List<FreightTemplateDetail> freightTemplateDetailList) {
        List<FreightDetailBO> freightDetailList = convertToFreightDetail(freightTemplateDetailList);
        freightCacheService.saveFreightDetails(freightDetailList);
    }

    /**
     * 删除运费模板明细缓存
     *
     * @param freightTemplateDetailList 待删除缓存的运费模板明细列表
     */
    private void deleteFreightDetailCache(List<FreightTemplateDetail> freightTemplateDetailList) {
        List<FreightDetailBO> freightDetailList = convertToFreightDetail(freightTemplateDetailList);
        freightCacheService.deleteFreightDetails(freightDetailList);
    }

    /**
     *  地区分组
     * @date 2021-08-07
     * @param freightTemplateDetails 运费模版详情
     * @return  map
     */
    private Map<String, Region> getRegionMap(List<FreightTemplateDetail> freightTemplateDetails) {
        List<String> regionCodes = new ArrayList<>();
        for (FreightTemplateDetail detail : freightTemplateDetails) {
            regionCodes.add(detail.getRegionCode());
        }
        RegionQueryLovDTO dto = new RegionQueryLovDTO();
        dto.setRegionCodes(regionCodes);
        Long tenantId = freightTemplateDetails.get(0).getTenantId();
        dto.setTenantId(tenantId);
        List<Region> regionList = regionRepository.listRegionLov(dto, tenantId);
        Map<String, Region> regionMap = Maps.newHashMapWithExpectedSize(regionList.size());
        for (Region region : regionList) {
            regionMap.put(region.getRegionCode(), region);

        }
        return regionMap;
    }

}
