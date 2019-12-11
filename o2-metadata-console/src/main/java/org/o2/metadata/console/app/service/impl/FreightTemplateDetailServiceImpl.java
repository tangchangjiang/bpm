package org.o2.metadata.console.app.service.impl;

import io.choerodon.core.exception.CommonException;
import org.apache.commons.collections4.CollectionUtils;
import org.hzero.mybatis.helper.SecurityTokenHelper;
import org.o2.metadata.console.app.bo.FreightDetailBO;
import org.o2.metadata.console.app.service.FreightCacheService;
import org.o2.metadata.console.app.service.FreightTemplateDetailService;
import org.o2.metadata.core.domain.entity.FreightTemplateDetail;
import org.o2.metadata.core.domain.repository.CarrierRepository;
import org.o2.metadata.core.domain.repository.FreightTemplateDetailRepository;
import org.o2.metadata.core.domain.repository.FreightTemplateRepository;
import org.o2.metadata.core.domain.repository.RegionRepository;
import org.o2.metadata.core.infra.constants.BasicDataConstants;
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

    public FreightTemplateDetailServiceImpl(FreightTemplateDetailRepository freightTemplateDetailRepository,
                                            FreightCacheService freightCacheService,
                                            CarrierRepository carrierRepository,
                                            RegionRepository regionRepository,
                                            FreightTemplateRepository freightTemplateRepository) {
        this.freightTemplateDetailRepository = freightTemplateDetailRepository;
        this.freightCacheService = freightCacheService;
        super.carrierRepository = carrierRepository;
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
                item.setIsDefault(Integer.valueOf(0));
            });
            freightTemplateDetailRepository.batchUpdateByPrimaryKey(otherDetailtList);
        }
        List<FreightTemplateDetail> freightTemplateDetails = freightTemplateDetailRepository.batchUpdateByPrimaryKey(freightTemplateDetailList);

        //更新缓存
        saveFreightDetailCache(freightTemplateDetails);

        return freightTemplateDetails;
    }

    @Override
    public List<FreightTemplateDetail> defaultBatchMerge(List<FreightTemplateDetail> freightTemplateDetailList) {
        List<FreightTemplateDetail> freightTemplateDetails = batchMerge(freightTemplateDetailList, false);

        //更新缓存
        saveFreightDetailCache(freightTemplateDetails);

        return freightTemplateDetails;
    }

    @Override
    public List<FreightTemplateDetail> regionBatchMerge(List<FreightTemplateDetail> freightTemplateDetailList) {
        List<FreightTemplateDetail> freightTemplateDetails = batchMerge(freightTemplateDetailList, true);

        //更新缓存
        saveFreightDetailCache(freightTemplateDetails);

        return freightTemplateDetails;
    }

    @Override
    public void batchDelete(List<FreightTemplateDetail> freightTemplateDetailList) {
        freightTemplateDetailRepository.batchDeleteByPrimaryKey(freightTemplateDetailList);

        // 删除缓存
        deleteFreightDetailCache(freightTemplateDetailList);
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
            String key = String.valueOf(detail.getCarrierId()) + String.valueOf(detail.getRegionId()) + String.valueOf(detail.getTemplateId());
            Assert.isTrue(map.get(key) == null, BasicDataConstants.ErrorCode.BASIC_DATA_FREIGHT_DETAIL_DUNPLICATE);
            map.put(key, i);

            // 数据库验重
            Assert.isTrue(!detail.exist(freightTemplateDetailRepository, isRegion), BasicDataConstants.ErrorCode.BASIC_DATA_FREIGHT_DETAIL_DUNPLICATE);

            if (detail.getTemplateDetailId() != null) {
                SecurityTokenHelper.validToken(detail);
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
                Assert.notNull(freightTemplateDetail.getTemplateDetailId(), BasicDataConstants.ErrorCode.BASIC_DATA_FREIGHT_DETAIL_ID_IS_NULL);
            }

            if (isRegion) {
                //  指定地区的运费模板明细需要校验regionId不为空
                freightTemplateDetail.regionDetailValidate();
            } else {
                freightTemplateDetail.defaultDetailValidate();
            }

            // list验重
            String key = String.valueOf(freightTemplateDetail.getCarrierId()) + String.valueOf(freightTemplateDetail.getRegionId()) + String.valueOf(freightTemplateDetail.getTemplateId());
            Assert.isTrue(map.get(key) == null, BasicDataConstants.ErrorCode.BASIC_DATA_FREIGHT_DETAIL_DUNPLICATE);
            map.put(key, i);

            // 数据库验重
            Assert.isTrue(!freightTemplateDetail.exist(freightTemplateDetailRepository, isRegion), BasicDataConstants.ErrorCode.BASIC_DATA_FREIGHT_DETAIL_DUNPLICATE);
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
            if (detail.getIsDefault() != null && detail.getIsDefault().intValue() == 1) {
                defaultFreightTemplateDetail = detail;
            }
        }

        if (defaultCount > 1) {
            throw new CommonException(BasicDataConstants.ErrorCode.BASIC_DATA_FREIGHT_UNIQUE_DEFAULT);
        } else {
            return defaultFreightTemplateDetail;
        }
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
}
