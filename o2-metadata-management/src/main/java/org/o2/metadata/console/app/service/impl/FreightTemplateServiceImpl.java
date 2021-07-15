package org.o2.metadata.console.app.service.impl;

import io.choerodon.core.oauth.DetailsHelper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.hzero.boot.platform.lov.handler.LovSqlHandler;
import org.hzero.mybatis.domian.Condition;
import org.hzero.mybatis.helper.SecurityTokenHelper;
import org.hzero.mybatis.util.Sqls;
import org.o2.metadata.console.app.bo.FreightBO;
import org.o2.metadata.console.app.bo.FreightTemplateBO;
import org.o2.metadata.console.app.service.FreightCacheService;
import org.o2.metadata.console.app.service.FreightTemplateDetailService;
import org.o2.metadata.console.app.service.FreightTemplateService;
import org.o2.metadata.console.infra.constant.MetadataConstants;
import org.o2.metadata.console.infra.entity.FreightTemplate;
import org.o2.metadata.console.infra.entity.FreightTemplateDetail;
import org.o2.metadata.console.infra.repository.FreightTemplateDetailRepository;
import org.o2.metadata.console.infra.repository.FreightTemplateRepository;
import org.o2.metadata.console.infra.repository.RegionRepository;
import org.o2.metadata.console.api.vo.FreightTemplateVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.util.*;

/**
 * 运费模板服务默认实现
 *
 * @author peng.xu@hand-china.com 2019/5/17
 */
@Service
@Slf4j
public class FreightTemplateServiceImpl extends AbstractFreightCacheOperation implements FreightTemplateService {
    private final FreightTemplateRepository freightTemplateRepository;
    private final FreightTemplateDetailRepository freightTemplateDetailRepository;
    private final FreightTemplateDetailService freightTemplateDetailService;
    private final FreightCacheService freightCacheService;
    @Autowired
    private LovSqlHandler lovSqlHandler;

    public FreightTemplateServiceImpl(final FreightTemplateRepository freightTemplateRepository,
                                      final FreightTemplateDetailRepository freightTemplateDetailRepository,
                                      final FreightTemplateDetailService freightTemplateDetailService,
                                      final FreightCacheService freightCacheService,
                                      final RegionRepository regionRepository) {
        this.freightTemplateRepository = freightTemplateRepository;
        this.freightTemplateDetailRepository = freightTemplateDetailRepository;
        this.freightTemplateDetailService = freightTemplateDetailService;
        this.freightCacheService = freightCacheService;
        super.regionRepository = regionRepository;
        super.freightTemplateRepository = freightTemplateRepository;
    }

    @Override
    public FreightTemplateVO queryTemplateAndDetails(final Long templateId, Long organizationId) {
        final FreightTemplate freightTemplate = freightTemplateRepository.selectyTemplateId(templateId);
        List<FreightTemplate> list = new ArrayList<>();
        list.add(freightTemplate);
        this.tranLov(list,organizationId);
        final FreightTemplateVO freightTemplateVO = new FreightTemplateVO(freightTemplate);

        final List<FreightTemplateDetail> defaultDetailList = freightTemplateDetailRepository.queryDefaultFreightTemplateDetail(templateId);
        freightTemplateVO.setDefaultFreightTemplateDetails(defaultDetailList);
        final List<FreightTemplateDetail> regionDetailList = freightTemplateDetailRepository.queryRegionFreightTemplateDetail(templateId);
        freightTemplateVO.setRegionFreightTemplateDetails(regionDetailList);
        final List<FreightTemplateDetail> regionDetailDisplayList  =freightTemplateVO.exchangeRegionDetailTemplateList2Displayist(freightTemplateVO.getRegionFreightTemplateDetails());
        freightTemplateVO.setRegionFreightDetailDisplayList(regionDetailDisplayList);

        return freightTemplateVO;
    }

    @Override
    public void tranLov(List<FreightTemplate> list, Long organizationId) {
        if (CollectionUtils.isNotEmpty(list)){
            List<Map<String, Object>>  uomList = getSqlMeaning(MetadataConstants.FreightType.LOV_VALUATION_UOM_NEW, organizationId);
            List<Map<String, Object>>  typeList = getSqlMeaning(MetadataConstants.FreightType.LOV_VALUATION_TYPE_NEW, organizationId);

            list.forEach(freightTemplateVO->{
                String valuationType = freightTemplateVO.getValuationType();
                String valuationUom = freightTemplateVO.getValuationUom();
                if (StringUtils.isNotBlank(valuationUom) && StringUtils.isNotBlank(valuationType)){
                    if (CollectionUtils.isNotEmpty(uomList)){
                        Optional<Map<String, Object>> first = uomList.stream().filter(li -> li.get("uomTypeCode").equals(valuationType)
                                && li.get("uomCode").equals(valuationUom)).findFirst();
                        if (first.isPresent()){
                            freightTemplateVO.setValuationUomMeaning(first.get().get("uomName").toString());
                        }
                    }
                }
                if (StringUtils.isNotBlank(valuationType)){
                    if (CollectionUtils.isNotEmpty(typeList)){
                        Optional<Map<String, Object>> first = typeList.stream().filter(li -> li.get("uomTypeCode").equals(valuationType) ).findFirst();
                        if (first.isPresent()){
                            freightTemplateVO.setValuationTypeMeaning(first.get().get("uomTypeName").toString());
                        }
                    }
                }
            });
        }
    }

    public List<Map<String, Object>> getSqlMeaning(String lovCode, Long tenantId) {
        Map<String,Object> params = new HashMap<>(4);
        params.put("lovCode",lovCode);
        params.put("page",0);
        params.put("size",100);
        params.put("tenantId",tenantId);
        List<Map<String, Object>> lovSqlMeaning = lovSqlHandler.queryData(lovCode, tenantId, params, 0, 100);
        return lovSqlMeaning;
    }

    public String fetchGroupKey(final FreightTemplateDetail detail) {
        return detail.getFirstPieceWeight()+""+detail.getFirstPrice()+""+detail.getNextPieceWeight()+""+detail.getNextPrice()+""+detail.getTransportTypeCode();
    }


        @Override
    @Transactional(rollbackFor = Exception.class)
    public FreightTemplateVO createTemplateAndDetails(final FreightTemplateVO freightTemplateVO) {
        final List<FreightTemplate> list = Arrays.asList(freightTemplateVO);
        final List<FreightTemplateDetail> defaultDetailList = freightTemplateVO.getDefaultFreightTemplateDetails();
        //需要前端显示的格式转成后端数据库需要的格式： 前端把地区合并了！！！
       if (!CollectionUtils.isEmpty(freightTemplateVO.getRegionFreightDetailDisplayList())){
                final List<FreightTemplateDetail> regionDetailListInput =     freightTemplateVO.exchangeRegionDetailDisplay2DBlist(freightTemplateVO.getRegionFreightDetailDisplayList());
                freightTemplateVO.setRegionFreightTemplateDetails(regionDetailListInput);
       }
        final List<FreightTemplateDetail> regionDetailList = freightTemplateVO.getRegionFreightTemplateDetails();

        checkData(list, false);

        final FreightTemplate result = batchInsert(list).get(0);
        final FreightTemplateVO resultVO = new FreightTemplateVO(result);

        return  insertTemplateDetails(resultVO,defaultDetailList,regionDetailList);

    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public FreightTemplateVO updateTemplateAndDetails(final FreightTemplateVO freightTemplateVO) {
        final List<FreightTemplate> list = Arrays.asList(freightTemplateVO);
        //需要前端显示的格式转成后端数据库需要的格式： 前端把地区合并了！！！
        if (!CollectionUtils.isEmpty(freightTemplateVO.getRegionFreightDetailDisplayList())){
            final List<FreightTemplateDetail> regionDetailListInput  =   freightTemplateVO.exchangeRegionDetailDisplay2DBlist(freightTemplateVO.getRegionFreightDetailDisplayList());
            freightTemplateVO.setRegionFreightTemplateDetails(regionDetailListInput);
        }
        final List<FreightTemplateDetail> defaultDetailList = freightTemplateVO.getDefaultFreightTemplateDetails();
        final List<FreightTemplateDetail> regionDetailList = freightTemplateVO.getRegionFreightTemplateDetails();

        checkData(list, true);

        final FreightTemplate result = batchMerge(list).get(0);
        FreightTemplateVO resultVO = new FreightTemplateVO(result);

        // 先删除 再插入
        final List<FreightTemplateDetail> oldList = freightTemplateDetailRepository.selectByCondition(Condition.builder(FreightTemplateDetail.class)
                .andWhere(Sqls.custom().andEqualTo(FreightTemplateDetail.FIELD_TENANT_ID,   result.getTenantId())
                        .andEqualTo(FreightTemplateDetail.FIELD_TEMPLATE_ID,result.getTemplateId())
                        ).build());
        if (CollectionUtils.isNotEmpty(oldList)){
            freightTemplateDetailRepository.batchDeleteByPrimaryKey(oldList);
        }
        deleteFreightCache(resultVO);

        resultVO  = insertTemplateDetails(resultVO,defaultDetailList,regionDetailList);

        return  resultVO;

    }


    @Transactional(rollbackFor = Exception.class)
    public FreightTemplateVO insertTemplateDetails(  FreightTemplateVO resultVO ,  final List<FreightTemplateDetail> defaultDetailList, final List<FreightTemplateDetail> regionDetailList) {

        if (CollectionUtils.isNotEmpty(defaultDetailList)) {
            final List<FreightTemplateDetail> savedDefaultDetailList = setTemplateIdAndSave(defaultDetailList, resultVO.getTemplateId(), false);
            resultVO.setDefaultFreightTemplateDetails(savedDefaultDetailList);
        }
        if (CollectionUtils.isNotEmpty(regionDetailList)) {
            final List<FreightTemplateDetail> savedRegionDetailList = setTemplateIdAndSave(regionDetailList, resultVO.getTemplateId(), true);
            resultVO.setRegionFreightTemplateDetails(savedRegionDetailList);
        }
        checkUniqueDefault(resultVO);

        // 更新redis缓存
        saveFreightCache(resultVO);
        return resultVO;
    }


        @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean removeTemplateAndDetails(final List<FreightTemplate> freightTemplateList) {
        SecurityTokenHelper.validToken(freightTemplateList);
        checkProductRelate(freightTemplateList);

        for (final FreightTemplate freightTemplate : freightTemplateList) {
            // 清除缓存
            deleteFreightCache(freightTemplate);
            freightTemplate.setActiveFlag(Integer.valueOf(0));
            freightTemplateRepository.updateOptional(freightTemplate,FreightTemplate.FIELD_ACTIVE_FLAG);
        }

        return true;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<FreightTemplate> batchInsert(final List<FreightTemplate> freightTemplateList) {
        checkData(freightTemplateList, false);
        List<FreightTemplate> freightTemplates = freightTemplateRepository.batchInsertSelective(freightTemplateList);

        // 更新缓存
        batchSaveFreightCache(freightTemplates);

        return freightTemplates;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<FreightTemplate> batchUpdate(final List<FreightTemplate> freightTemplateList) {
        checkData(freightTemplateList, true);
        List<FreightTemplate> freightTemplates = freightTemplateRepository.batchUpdateByPrimaryKey(freightTemplateList);

        // 更新缓存
        batchSaveFreightCache(freightTemplates);

        return freightTemplates;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<FreightTemplate> batchMerge(final List<FreightTemplate> freightTemplateList) {
        long tenantId =  DetailsHelper.getUserDetails().getTenantId();
        final Map<String, Object> map = new HashMap<>(freightTemplateList.size());
        final List<FreightTemplate> updateList = new ArrayList<>();
        final List<FreightTemplate> insertList = new ArrayList<>();
        for (int i = 0; i < freightTemplateList.size(); i++) {
            final FreightTemplate freightTemplate = freightTemplateList.get(i);
            freightTemplate.validate();
            // 数据库查重
            if (freightTemplate.getTemplateId()!=null){
                Assert.isTrue(!freightTemplate.exist(freightTemplateRepository), "数据库存在相同的运费模板");
            }
            // list查重
            Assert.isTrue(map.get(freightTemplate.getTemplateCode()) == null, "提交数据中存在相同的运费模板");
            //默认启用;默认不是默认模板
            freightTemplate.setActiveFlag(freightTemplate.getActiveFlag()==null?1:freightTemplate.getActiveFlag());
            freightTemplate.setDafaultFlag(freightTemplate.getDafaultFlag()==null?0:freightTemplate.getDafaultFlag());
            freightTemplate.setTenantId(freightTemplate.getTenantId()==null?tenantId:freightTemplate.getTenantId());
            if (freightTemplate.getTemplateId() != null) {
                SecurityTokenHelper.validToken(freightTemplate);
                updateList.add(freightTemplate);
            } else {
                insertList.add(freightTemplate);
            }
            map.put(freightTemplate.getTemplateCode(), i);
            // 如果当前行是默认运费模板,将原来的默认运费模板替换；
            if (freightTemplate.getDafaultFlag()==1){
                final Sqls sqls2 = Sqls.custom();
                    sqls2.andEqualTo(FreightTemplate.FIELD_DAFAULT_FLAG, Integer.valueOf(1));
                    sqls2.andEqualTo(FreightTemplate.FIELD_TENANT_ID,  freightTemplate.getTenantId());
                    List<FreightTemplate> defaultTempList = freightTemplateRepository.selectByCondition( Condition.builder(FreightTemplate.class).andWhere(sqls2).build());
                    FreightTemplate oldDefTemp  ;
                    if(CollectionUtils.isNotEmpty(defaultTempList)){
                        oldDefTemp =  defaultTempList.get(0);
                        if (!oldDefTemp.getTemplateCode().equals(freightTemplate.getTemplateCode())){
                            oldDefTemp.setDafaultFlag(0);
                            freightTemplateRepository.updateOptional(oldDefTemp,FreightTemplate.FIELD_DAFAULT_FLAG);
                        }

                    }
            }
        }
        final List<FreightTemplate> resultList = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(updateList)) {
            resultList.addAll(freightTemplateRepository.batchUpdateByPrimaryKey(updateList));
        }
        if (CollectionUtils.isNotEmpty(insertList)) {
            resultList.addAll(freightTemplateRepository.batchInsertSelective(insertList));
        }

        // 更新缓存
        batchSaveFreightCache(resultList);

        return resultList;
    }

    @Override
    public boolean uniqueDefaultValidate(final Long templateId) {

        final FreightTemplate freightTemplate = freightTemplateRepository.selectByPrimaryKey(templateId);
        // freightTemplate为空或包邮时，通过验证
        if (freightTemplate == null) {
            return true;
        }
        if (freightTemplate.getDeliveryFreeFlag() != null && freightTemplate.getDeliveryFreeFlag().intValue() == 1) {
            return true;
        }

        // 如果运费模板不包邮，验证有且只有一个默认运费行
        final Sqls sqlcommand = Sqls.custom();
        sqlcommand.andEqualTo(FreightTemplateDetail.FIELD_TEMPLATE_ID, templateId)
                .andEqualTo(FreightTemplateDetail.FIELD_DEFAULT_FLAG, Integer.valueOf(1));

        final Condition cond = Condition.builder(FreightTemplateDetail.class).andWhere(sqlcommand).build();

        return freightTemplateDetailRepository.selectCountByCondition(cond) == 1;
    }

    @Override
    public void refreshCache(Long templateId) {
        FreightTemplateVO freightTemplateVO = queryTemplateAndDetails(templateId, null);

        List<FreightTemplateDetail> list = new ArrayList<>();
        if (freightTemplateVO.getDefaultFreightTemplateDetails() != null) {
            list.addAll(freightTemplateVO.getDefaultFreightTemplateDetails());
        }
        if (freightTemplateVO.getRegionFreightTemplateDetails() != null) {
            list.addAll(freightTemplateVO.getRegionFreightTemplateDetails());
        }

        // 清除缓存
        deleteFreightCache(freightTemplateVO);

        // 更新缓存
        saveFreightCache(freightTemplateVO);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void setDefaultTemp(final Long organizationId, Long templateId) {
        Assert.notNull(templateId, MetadataConstants.ErrorCode.BASIC_DATA_FREIGHT_ID_IS_NULL);

        //查找默认的并删除
        Long oldTemplateId =null;
         final Sqls sqls2 = Sqls.custom();
         sqls2.andEqualTo(FreightTemplate.FIELD_DAFAULT_FLAG, Integer.valueOf(1));
         sqls2.andEqualTo(FreightTemplate.FIELD_TENANT_ID,  organizationId);
         List<FreightTemplate> defaultTempList = freightTemplateRepository.selectByCondition( Condition.builder(FreightTemplate.class).andWhere(sqls2).build());
         FreightTemplate oldDefTemp  ;
         if(CollectionUtils.isNotEmpty(defaultTempList)){
             oldDefTemp =  defaultTempList.get(0);
             oldDefTemp.setDafaultFlag(0);
             freightTemplateRepository.updateOptional(oldDefTemp,FreightTemplate.FIELD_DAFAULT_FLAG);
             oldTemplateId=oldDefTemp.getTemplateId();

         }
        FreightTemplate defaultTemp = freightTemplateRepository.selectByPrimaryKey(templateId);
        Assert.notNull(defaultTemp, MetadataConstants.ErrorCode.BASIC_DATA_FREIGHT_NOT_EXISTS);
        defaultTemp.setDafaultFlag(Integer.valueOf(1));
        freightTemplateRepository.updateOptional(defaultTemp,FreightTemplate.FIELD_DAFAULT_FLAG);

        if (oldTemplateId!=null){
            refreshCache(oldTemplateId);
        }
        refreshCache(templateId);
    }

    /**
     * 验重
     *
     * @param freightTemplates
     * @param isCheckId
     */
    private <T extends FreightTemplate> void checkData(final List<T> freightTemplates, final boolean isCheckId) {
        final Map<String, Object> map = new HashMap<>(freightTemplates.size());
        for (int i = 0; i < freightTemplates.size(); i++) {
            final T freightTemplate = freightTemplates.get(i);
            // 默认启用 默认不是默认运费模板
            freightTemplate.setActiveFlag(freightTemplate.getActiveFlag()==null?Integer.valueOf(1):freightTemplate.getActiveFlag());
            freightTemplate.setDafaultFlag(freightTemplate.getDafaultFlag()==null?Integer.valueOf(0):freightTemplate.getDafaultFlag());

            if (isCheckId) {
                Assert.notNull(freightTemplate.getTemplateId(), MetadataConstants.ErrorCode.BASIC_DATA_FREIGHT_ID_IS_NULL);
            }
            freightTemplate.validate();
            // 数据库查重
            Assert.isTrue(!freightTemplate.exist(freightTemplateRepository), MetadataConstants.ErrorCode.BASIC_DATA_FREIGHT_DUPLICATE_CODE);
            // list查重
            Assert.isTrue(map.get(freightTemplate.getTemplateCode()) == null, MetadataConstants.ErrorCode.BASIC_DATA_FREIGHT_DUPLICATE_CODE);
            map.put(freightTemplate.getTemplateCode(), i);
        }
    }

    /**
     * 设置templateId并保存运费模板明细
     *
     * @param freightTemplateDetailList
     * @param templateId
     * @param isRegion
     * @return
     */
    private List<FreightTemplateDetail> setTemplateIdAndSave(final List<FreightTemplateDetail> freightTemplateDetailList, final Long templateId, final boolean isRegion) {
        long tenantId =DetailsHelper.getUserDetails().getTenantId();
        for (final FreightTemplateDetail detail : freightTemplateDetailList) {
            if (detail.getTemplateDetailId()!=null){detail.setTemplateDetailId(null);}
            detail.setTenantId(detail.getTenantId() ==null?tenantId:detail.getTenantId());
            detail.setTemplateId(templateId);
        }
        return freightTemplateDetailService.batchInsert(freightTemplateDetailList, isRegion);
    }

    /**
     * 免运费可以没有模板行；其他验证默认运费模板行有否有且只有一；
     *
     * @param freightTemplate
     */
    private void checkUniqueDefault(final FreightTemplate freightTemplate) {
        Assert.isTrue(uniqueDefaultValidate(freightTemplate.getTemplateId()), MetadataConstants.ErrorCode.BASIC_DATA_FREIGHT_UNIQUE_DEFAULT);
    }

    /**
     * 检查运费模板是否关联了平台产品
     *
     * @param freightTemplateList
     * @return
     */
    private void checkProductRelate(final List<FreightTemplate> freightTemplateList) {
        for (final FreightTemplate freightTemplate : freightTemplateList) {
            final boolean relateFlag = freightTemplateRepository.isFreightTemplateRelatePro(freightTemplate);
            Assert.isTrue(!relateFlag, MetadataConstants.ErrorCode.BASIC_DATA_FREIGHT_CAN_NOT_REMOVE);
        }
    }

    /**
     * 更新运费模板缓存(包含运费模板、运费模板明细)
     *
     * @param resultVO
     */
    private void saveFreightCache(FreightTemplateVO resultVO) {
        FreightTemplateBO freightTemplate = convertToFreightTemplate(resultVO);
        freightCacheService.saveFreight(freightTemplate);
    }

    /**
     * 批量更新运费模板缓存(仅运费模板信息，不包含运费模板明细)
     *
     * @param freightTemplateList
     */
    private void batchSaveFreightCache(List<FreightTemplate> freightTemplateList) {
        if (freightTemplateList != null && freightTemplateList.size() > 0) {
            for (FreightTemplate freightTemplate : freightTemplateList) {
                FreightBO freight = convertToFreight(freightTemplate);
                FreightTemplateBO template = new FreightTemplateBO();
                template.setFreight(freight);

                freightCacheService.saveFreight(template);
            }
        }

    }

    /**
     * 删除运费模板和运费模板明细信息
     *
     * @param freightTemplate
     */
    private void deleteFreightCache(FreightTemplate freightTemplate) {
        FreightTemplateBO template = new FreightTemplateBO();
        template.setFreight(convertToFreight(freightTemplate));
        freightCacheService.deleteFreight(template);
    }

    @Override
    public FreightTemplateVO querydefaultTemplate(Long   organizationId) {
        // 查询默认模板
        final List<FreightTemplate> list = freightTemplateRepository.selectByCondition(Condition.builder(FreightTemplate.class)
                .andWhere(Sqls.custom().andEqualTo(FreightTemplate.FIELD_TENANT_ID,   organizationId)
                        .andEqualTo(FreightTemplate.FIELD_DAFAULT_FLAG, Integer.valueOf(1))).build());

        Assert.isTrue(!CollectionUtils.isEmpty(list), "默认运费模板不存在");
        Assert.isTrue(!(list.size()!=1), "默认运费模板不唯一");

        return  queryTemplateAndDetails(list.get(0).getTemplateId(), organizationId);

    }


}
