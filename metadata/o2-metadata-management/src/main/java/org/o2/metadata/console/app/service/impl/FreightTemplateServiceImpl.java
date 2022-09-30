package org.o2.metadata.console.app.service.impl;

import io.choerodon.core.exception.CommonException;
import io.choerodon.core.oauth.DetailsHelper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.hzero.core.base.BaseConstants;
import org.hzero.mybatis.domian.Condition;
import org.hzero.mybatis.helper.SecurityTokenHelper;
import org.hzero.mybatis.util.Sqls;
import org.o2.core.copier.PropertiesCopier;
import org.o2.core.exception.O2CommonException;
import org.o2.core.helper.TransactionalHelper;
import org.o2.metadata.console.api.co.FreightInfoCO;
import org.o2.metadata.console.api.co.FreightTemplateCO;
import org.o2.metadata.console.api.dto.FreightDTO;
import org.o2.metadata.console.api.vo.FreightTemplateManagementVO;
import org.o2.metadata.console.app.bo.FreightBO;
import org.o2.metadata.console.app.bo.FreightTemplateBO;
import org.o2.metadata.console.app.bo.UomBO;
import org.o2.metadata.console.app.bo.UomTypeBO;
import org.o2.metadata.console.app.service.FreightCacheService;
import org.o2.metadata.console.app.service.FreightTemplateDetailService;
import org.o2.metadata.console.app.service.FreightTemplateService;
import org.o2.metadata.console.infra.constant.FreightConstants;
import org.o2.metadata.console.infra.convertor.FreightConverter;
import org.o2.metadata.console.infra.entity.FreightTemplate;
import org.o2.metadata.console.infra.entity.FreightTemplateDetail;
import org.o2.metadata.console.infra.lovadapter.repository.BaseLovQueryRepository;
import org.o2.metadata.console.infra.repository.FreightTemplateDetailRepository;
import org.o2.metadata.console.infra.repository.FreightTemplateRepository;
import org.o2.metadata.console.infra.repository.RegionRepository;
import org.o2.metadata.domain.freight.domain.FreightTemplateDO;
import org.o2.metadata.domain.freight.repository.FreightTemplateDomainRepository;
import org.o2.product.management.client.O2ProductClient;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 运费模板服务默认实现
 *
 * @author peng.xu@hand-china.com 2019/5/17
 */
@Service
@Slf4j
public class FreightTemplateServiceImpl extends AbstractFreightCacheOperation implements FreightTemplateService {
    private final FreightTemplateDetailRepository freightTemplateDetailRepository;
    private final FreightTemplateDetailService freightTemplateDetailService;
    private final FreightCacheService freightCacheService;
    private final FreightTemplateDomainRepository freightTemplateDomainRepository;
    private final BaseLovQueryRepository baseLovQueryRepository;
    private final O2ProductClient o2ProductClient;
    private final TransactionalHelper transactionalHelper;


    public FreightTemplateServiceImpl(final FreightTemplateRepository freightTemplateRepository,
                                      final FreightTemplateDetailRepository freightTemplateDetailRepository,
                                      final FreightTemplateDetailService freightTemplateDetailService,
                                      final FreightCacheService freightCacheService,
                                      final RegionRepository regionRepository,
                                      FreightTemplateDomainRepository freightTemplateDomainRepository,
                                      BaseLovQueryRepository baseLovQueryRepository, O2ProductClient o2ProductClient, TransactionalHelper transactionalHelper) {
        this.transactionalHelper = transactionalHelper;
        this.freightTemplateRepository = freightTemplateRepository;
        this.freightTemplateDetailRepository = freightTemplateDetailRepository;
        this.freightTemplateDetailService = freightTemplateDetailService;
        this.freightCacheService = freightCacheService;
        this.freightTemplateDomainRepository = freightTemplateDomainRepository;
        this.baseLovQueryRepository = baseLovQueryRepository;
        this.o2ProductClient = o2ProductClient;
        super.regionRepository = regionRepository;
    }

    @Override
    public FreightTemplateManagementVO queryTemplateAndDetails(final Long templateId, Long organizationId) {
        // 获取模板头信息
        final FreightTemplate freightTemplate = freightTemplateRepository.selectTemplateId(templateId);
        List<FreightTemplate> list = new ArrayList<>();
        list.add(freightTemplate);
        // 获取计价单位含义
        this.tranLov(list, organizationId);

        final FreightTemplateManagementVO freightTemplateManagementVO = new FreightTemplateManagementVO(freightTemplate);
        // 查询默认运费模板明细
        final List<FreightTemplateDetail> defaultDetailList = freightTemplateDetailService.queryDefaultFreightTemplateDetail(templateId);
        freightTemplateManagementVO.setDefaultFreightTemplateDetails(defaultDetailList);
        // 查询指定地区运费模板明细
        final List<FreightTemplateDetail> regionDetailList = freightTemplateDetailService.queryRegionFreightTemplateDetail(templateId);
        freightTemplateManagementVO.setRegionFreightTemplateDetails(regionDetailList);
        // 构建详情页地区运费模板展示
        final List<FreightTemplateDetail> regionDetailDisplayList = buildRegionDetailTemplate(freightTemplateManagementVO.getRegionFreightTemplateDetails());
        freightTemplateManagementVO.setRegionFreightDetailDisplayList(regionDetailDisplayList);

        return freightTemplateManagementVO;
    }

    /***
     * 构建详情页地区运费模板展示
     * @param regionDetailTemplateList 模板明细
     * @return 运费模板
     */
    private List<FreightTemplateDetail> buildRegionDetailTemplate(List<FreightTemplateDetail> regionDetailTemplateList) {

        final List<FreightTemplateDetail> regionDetailDisplayList = new ArrayList<>();
        if (CollectionUtils.isEmpty(regionDetailTemplateList)) {
            return regionDetailDisplayList;
        }
        //转化成前端需要的显示格式~
        Map<String, List<FreightTemplateDetail>> complexMap = new HashMap<>();
        for (FreightTemplateDetail freightTemplateDetail : regionDetailTemplateList) {
            complexMap.computeIfAbsent(getGroupKey(freightTemplateDetail), k -> new ArrayList<>()).add(freightTemplateDetail);
        }
        complexMap.forEach((key, value) -> {
            FreightTemplateDetail fist = value.get(0);
            FreightTemplateDetail detail = new FreightTemplateDetail();
            PropertiesCopier.copyEntities(fist, detail);
            List<String> regionNames = value.stream().map(FreightTemplateDetail::getRegionName).collect(Collectors.toList());
            List<String> regionId = value.stream().map(FreightTemplateDetail::getRegionCode).collect(Collectors.toList());
            List<Long> templateDetailIdArr = value.stream().map(FreightTemplateDetail::getTemplateDetailId).collect(Collectors.toList());
            detail.setRegionIdArr(regionId);
            detail.setRegionNameArr(regionNames);
            if (CollectionUtils.isNotEmpty(regionNames)) {
                detail.setRegionName(StringUtils.join(regionNames, BaseConstants.Symbol.COMMA));
            }
            detail.setTemplateDetailIdArr(templateDetailIdArr);
            regionDetailDisplayList.add(detail);
        });
        return regionDetailDisplayList;
    }

    /**
     * 获取分组key
     *
     * @param detail 运费详情
     * @return 分组key
     */
    private String getGroupKey(final FreightTemplateDetail detail) {
        return detail.getFirstPieceWeight() + "" + detail.getFirstPrice() + "" + detail.getNextPieceWeight() + "" + detail.getNextPrice() + "" + detail.getTransportTypeCode();
    }

    @Override
    public void tranLov(List<FreightTemplate> list, Long organizationId) {
        if (list.isEmpty()) {
            return;
        }
        List<String> uomCode = new ArrayList<>();
        List<String> uomTypeCode = new ArrayList<>();
        // 独立值集 本地缓存
        for (FreightTemplate freightTemplate : list) {
            // 计价方式
            String valuationType = freightTemplate.getValuationType();
            uomTypeCode.add(valuationType);
            // 计价单位
            String valuationUom = freightTemplate.getValuationUom();
            uomCode.add(valuationUom);
        }
        Map<String, UomBO> uomMap = baseLovQueryRepository.findUomByCodes(organizationId, uomCode);
        Map<String, UomTypeBO> uomTypeMap = baseLovQueryRepository.findUomTypeByCodes(organizationId, uomTypeCode);
        for (FreightTemplate freightTemplate : list) {
            String valuationType = freightTemplate.getValuationType();
            UomTypeBO uomTypeBO = uomTypeMap.get(valuationType);
            if (null != uomTypeBO) {
                freightTemplate.setValuationTypeMeaning(uomTypeBO.getName());
            }
            String valuationUom = freightTemplate.getValuationUom();
            UomBO uomBO = uomMap.get(valuationUom);
            if (null != uomBO) {
                freightTemplate.setValuationUomMeaning(uomBO.getName());
            }
        }
    }

    @Override
    public FreightInfoCO getFreightTemplate(FreightDTO freight) {
        return FreightConverter.doToCoObject(freightTemplateDomainRepository.getFreightTemplate(freight.getRegionCode(), freight.getTemplateCodes(), freight.getTenantId()));
    }

    @Override
    public FreightTemplateCO getDefaultTemplate(Long organizationId) {
        return FreightConverter.poToCoObject(freightTemplateRepository.getDefaultTemplate(organizationId));
    }

    @Override
    public Map<String, FreightTemplateCO> listFreightTemplate(Long tenantId, List<String> templateCodes) {
        List<FreightTemplateDO> freightInfoTemplateDOs = freightTemplateDomainRepository.listFreightTemplate(tenantId, templateCodes);
        Map<String, FreightTemplateCO> resultMap = new HashMap<>();
        for (FreightTemplateDO freightTemplateDO : freightInfoTemplateDOs) {
            FreightTemplateCO freightTemplateCO = FreightConverter.toFreightTemplateCo(freightTemplateDO);
            resultMap.put(freightTemplateCO.getTemplateCode(), freightTemplateCO);
        }
        return resultMap;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public FreightTemplateManagementVO createTemplateAndDetails(final FreightTemplateManagementVO freightTemplateManagementVO) {
        validNameUnique(freightTemplateManagementVO);
        final List<FreightTemplate> list = Collections.singletonList(freightTemplateManagementVO);
        final List<FreightTemplateDetail> defaultDetailList = freightTemplateManagementVO.getDefaultFreightTemplateDetails();
        //需要前端显示的格式转成后端数据库需要的格式： 前端把地区合并了！！！
        if (!CollectionUtils.isEmpty(freightTemplateManagementVO.getRegionFreightDetailDisplayList())) {
            final List<FreightTemplateDetail> regionDetailListInput = freightTemplateManagementVO.exchangeRegionDetailDisplay2DBlist(freightTemplateManagementVO.getRegionFreightDetailDisplayList());
            freightTemplateManagementVO.setRegionFreightTemplateDetails(regionDetailListInput);
        }
        final List<FreightTemplateDetail> regionDetailList = freightTemplateManagementVO.getRegionFreightTemplateDetails();
        checkData(list, false);
        final FreightTemplate result = batchInsert(list).get(0);
        final FreightTemplateManagementVO resultVO = new FreightTemplateManagementVO(result);
        return insertTemplateDetails(resultVO, defaultDetailList, regionDetailList);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public FreightTemplateManagementVO updateTemplateAndDetails(final FreightTemplateManagementVO freightTemplateManagementVO) {
        validNameUnique(freightTemplateManagementVO);
        final List<FreightTemplate> list = Collections.singletonList(freightTemplateManagementVO);
        //需要前端显示的格式转成后端数据库需要的格式： 前端把地区合并了！！！
        if (!CollectionUtils.isEmpty(freightTemplateManagementVO.getRegionFreightDetailDisplayList())) {
            final List<FreightTemplateDetail> regionDetailListInput = freightTemplateManagementVO.exchangeRegionDetailDisplay2DBlist(freightTemplateManagementVO.getRegionFreightDetailDisplayList());
            freightTemplateManagementVO.setRegionFreightTemplateDetails(regionDetailListInput);
        }
        final List<FreightTemplateDetail> defaultDetailList = freightTemplateManagementVO.getDefaultFreightTemplateDetails();
        final List<FreightTemplateDetail> regionDetailList = freightTemplateManagementVO.getRegionFreightTemplateDetails();

        checkData(list, true);

        final FreightTemplate result = batchMerge(list).get(0);
        FreightTemplateManagementVO resultVO = new FreightTemplateManagementVO(result);

        // 先删除 再插入
        final List<FreightTemplateDetail> oldList = freightTemplateDetailRepository.selectByCondition(Condition.builder(FreightTemplateDetail.class)
                .andWhere(Sqls.custom().andEqualTo(FreightTemplateDetail.FIELD_TENANT_ID, result.getTenantId())
                        .andEqualTo(FreightTemplateDetail.FIELD_TEMPLATE_ID, result.getTemplateId())
                ).build());
        if (CollectionUtils.isNotEmpty(oldList)) {
            freightTemplateDetailRepository.batchDeleteByPrimaryKey(oldList);
        }
        deleteFreightCache(resultVO);
        resultVO = insertTemplateDetails(resultVO, defaultDetailList, regionDetailList);
        return resultVO;
    }

    /**
     * 验证名称唯一性
     *
     * @param freightTemplateManagement 模板
     */
    private void validNameUnique(FreightTemplateManagementVO freightTemplateManagement) {
        if (null != freightTemplateManagement.getTemplateId()) {
            FreightTemplate only = new FreightTemplate();
            only.setTenantId(freightTemplateManagement.getTenantId());
            only.setTemplateId(freightTemplateManagement.getTemplateId());
            FreightTemplate original = freightTemplateRepository.selectByPrimaryKey(only);
            if (original.getTemplateName().equals(freightTemplateManagement.getTemplateName())) {
                return;
            }
        }
        FreightTemplate query = new FreightTemplate();
        query.setTemplateName(freightTemplateManagement.getTemplateName());
        query.setTenantId(freightTemplateManagement.getTenantId());
        List<FreightTemplate> list = freightTemplateRepository.select(query);
        if (!list.isEmpty()) {
            throw new O2CommonException(null, FreightConstants.ErrorCode.FREIGHT_NAME_DUPLICATE, FreightConstants.ErrorCode.FREIGHT_NAME_DUPLICATE);
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public FreightTemplateManagementVO insertTemplateDetails(FreightTemplateManagementVO resultVO, final List<FreightTemplateDetail> defaultDetailList, final List<FreightTemplateDetail> regionDetailList) {

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
    public Boolean removeTemplateAndDetails(final List<FreightTemplate> freightTemplateList, Long tenantId) {
        SecurityTokenHelper.validToken(freightTemplateList);
        checkProductRelate(freightTemplateList, tenantId);
        List<FreightTemplateBO> list = new ArrayList<>(freightTemplateList.size());
        List<String> detailCodes = new ArrayList<>(4);
        for (final FreightTemplate freightTemplate : freightTemplateList) {
            FreightTemplateBO template = new FreightTemplateBO();
            template.setFreight(convertToFreight(freightTemplate));
            list.add(template);
            detailCodes.add(freightTemplate.getTemplateCode());
        }
        FreightTemplateDetail query = new FreightTemplateDetail();
        query.setTemplateCodes(detailCodes);
        query.setTenantId(tenantId);
        List<FreightTemplateDetail> details = freightTemplateDetailRepository.selectByByCondition(query);
        transactionalHelper.transactionOperation(() -> {

            freightTemplateRepository.batchDeleteByPrimaryKey(freightTemplateList);
            freightTemplateDetailRepository.batchDeleteByPrimaryKey(details);
            // 清除缓存
            freightCacheService.deleteFreight(list);
        });
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
        long tenantId = DetailsHelper.getUserDetails().getTenantId();
        final Map<String, Object> map = new HashMap<>(freightTemplateList.size());
        final List<FreightTemplate> updateList = new ArrayList<>();
        final List<FreightTemplate> insertList = new ArrayList<>();
        for (int i = 0; i < freightTemplateList.size(); i++) {
            final FreightTemplate freightTemplate = freightTemplateList.get(i);
            freightTemplate.validate();
            // 数据库查重
            if (freightTemplate.getTemplateId() != null) {
                Assert.isTrue(!freightTemplate.exist(freightTemplateRepository), "数据库存在相同的运费模板");
            }
            // list查重
            Assert.isTrue(map.get(freightTemplate.getTemplateCode()) == null, "提交数据中存在相同的运费模板");
            //默认启用;默认不是默认模板
            freightTemplate.setActiveFlag(freightTemplate.getActiveFlag() == null ? 1 : freightTemplate.getActiveFlag());
            freightTemplate.setDafaultFlag(freightTemplate.getDafaultFlag() == null ? 0 : freightTemplate.getDafaultFlag());
            freightTemplate.setTenantId(freightTemplate.getTenantId() == null ? tenantId : freightTemplate.getTenantId());
            if (freightTemplate.getTemplateId() != null) {
                SecurityTokenHelper.validToken(freightTemplate);
                updateList.add(freightTemplate);
            } else {
                insertList.add(freightTemplate);
            }
            map.put(freightTemplate.getTemplateCode(), i);
            // 如果当前行是默认运费模板,将原来的默认运费模板替换；
            if (freightTemplate.getDafaultFlag() == 1) {
                final Sqls sqls2 = Sqls.custom();
                sqls2.andEqualTo(FreightTemplate.FIELD_DAFAULT_FLAG, 1);
                sqls2.andEqualTo(FreightTemplate.FIELD_TENANT_ID, freightTemplate.getTenantId());
                List<FreightTemplate> defaultTempList = freightTemplateRepository.selectByCondition(Condition.builder(FreightTemplate.class).andWhere(sqls2).build());
                FreightTemplate oldDefTemp;
                if (CollectionUtils.isNotEmpty(defaultTempList)) {
                    oldDefTemp = defaultTempList.get(0);
                    if (!oldDefTemp.getTemplateCode().equals(freightTemplate.getTemplateCode())) {
                        oldDefTemp.setDafaultFlag(0);
                        freightTemplateRepository.updateOptional(oldDefTemp, FreightTemplate.FIELD_DAFAULT_FLAG);
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
        if (freightTemplate.getDeliveryFreeFlag() != null && freightTemplate.getDeliveryFreeFlag() == 1) {
            return true;
        }

        // 如果运费模板不包邮，验证有且只有一个默认运费行
        final Sqls sqlcommand = Sqls.custom();
        sqlcommand.andEqualTo(FreightTemplateDetail.FIELD_TEMPLATE_ID, templateId)
                .andEqualTo(FreightTemplateDetail.FIELD_DEFAULT_FLAG, 1);

        final Condition cond = Condition.builder(FreightTemplateDetail.class).andWhere(sqlcommand).build();

        return freightTemplateDetailRepository.selectCountByCondition(cond) == 1;
    }

    @Override
    public void refreshCache(Long templateId, Long tenantId) {
        FreightTemplateManagementVO freightTemplateManagementVO = queryTemplateAndDetails(templateId, tenantId);

        List<FreightTemplateDetail> list = new ArrayList<>();
        if (freightTemplateManagementVO.getDefaultFreightTemplateDetails() != null) {
            list.addAll(freightTemplateManagementVO.getDefaultFreightTemplateDetails());
        }
        if (freightTemplateManagementVO.getRegionFreightTemplateDetails() != null) {
            list.addAll(freightTemplateManagementVO.getRegionFreightTemplateDetails());
        }

        // 清除缓存
        deleteFreightCache(freightTemplateManagementVO);

        // 更新缓存
        saveFreightCache(freightTemplateManagementVO);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void setDefaultTemp(final Long organizationId, Long templateId) {
        Assert.notNull(templateId, FreightConstants.ErrorCode.BASIC_DATA_FREIGHT_ID_IS_NULL);

        //查找默认的并删除
        Long oldTemplateId = null;
        final Sqls sqls2 = Sqls.custom();
        sqls2.andEqualTo(FreightTemplate.FIELD_DAFAULT_FLAG, 1);
        sqls2.andEqualTo(FreightTemplate.FIELD_TENANT_ID, organizationId);
        List<FreightTemplate> defaultTempList = freightTemplateRepository.selectByCondition(Condition.builder(FreightTemplate.class).andWhere(sqls2).build());
        FreightTemplate oldDefTemp;
        if (CollectionUtils.isNotEmpty(defaultTempList)) {
            oldDefTemp = defaultTempList.get(0);
            oldDefTemp.setDafaultFlag(0);
            freightTemplateRepository.updateOptional(oldDefTemp, FreightTemplate.FIELD_DAFAULT_FLAG);
            oldTemplateId = oldDefTemp.getTemplateId();

        }
        FreightTemplate defaultTemp = freightTemplateRepository.selectByPrimaryKey(templateId);
        Assert.notNull(defaultTemp, FreightConstants.ErrorCode.BASIC_DATA_FREIGHT_NOT_EXISTS);
        defaultTemp.setDafaultFlag(1);
        freightTemplateRepository.updateOptional(defaultTemp, FreightTemplate.FIELD_DAFAULT_FLAG);

        if (oldTemplateId != null) {
            refreshCache(oldTemplateId, organizationId);
        }
        refreshCache(templateId, organizationId);
    }

    /**
     * 验重
     *
     * @param freightTemplates 运费模版
     * @param isCheckId        是否检测
     */
    private <T extends FreightTemplate> void checkData(final List<T> freightTemplates, final boolean isCheckId) {
        final Map<String, Object> map = new HashMap<>(freightTemplates.size());
        for (int i = 0; i < freightTemplates.size(); i++) {
            final T freightTemplate = freightTemplates.get(i);
            // 默认启用 默认不是默认运费模板
            freightTemplate.setActiveFlag(freightTemplate.getActiveFlag() == null ? Integer.valueOf(1) : freightTemplate.getActiveFlag());
            freightTemplate.setDafaultFlag(freightTemplate.getDafaultFlag() == null ? Integer.valueOf(0) : freightTemplate.getDafaultFlag());

            if (isCheckId) {
                Assert.notNull(freightTemplate.getTemplateId(), FreightConstants.ErrorCode.BASIC_DATA_FREIGHT_ID_IS_NULL);
            }
            freightTemplate.validate();
            // 数据库查重
            Assert.isTrue(!freightTemplate.exist(freightTemplateRepository), FreightConstants.ErrorCode.BASIC_DATA_FREIGHT_DUPLICATE_CODE);
            // list查重
            Assert.isTrue(map.get(freightTemplate.getTemplateCode()) == null, FreightConstants.ErrorCode.BASIC_DATA_FREIGHT_DUPLICATE_CODE);
            map.put(freightTemplate.getTemplateCode(), i);
        }
    }

    /**
     * 设置templateId并保存运费模板明细
     *
     * @param freightTemplateDetailList 运费模版
     * @param templateId                模版ID
     * @param isRegion                  地区
     * @return 集合
     */
    private List<FreightTemplateDetail> setTemplateIdAndSave(final List<FreightTemplateDetail> freightTemplateDetailList, final Long templateId, final boolean isRegion) {
        long tenantId = DetailsHelper.getUserDetails().getTenantId();
        for (final FreightTemplateDetail detail : freightTemplateDetailList) {
            if (detail.getTemplateDetailId() != null) {
                detail.setTemplateDetailId(null);
            }
            detail.setTenantId(detail.getTenantId() == null ? tenantId : detail.getTenantId());
            detail.setTemplateId(templateId);
        }
        return freightTemplateDetailService.batchInsert(freightTemplateDetailList, isRegion);
    }

    /**
     * 免运费可以没有模板行；其他验证默认运费模板行有否有且只有一；
     *
     * @param freightTemplate 运费模版
     */
    private void checkUniqueDefault(final FreightTemplate freightTemplate) {
        if (!uniqueDefaultValidate(freightTemplate.getTemplateId())) {
            throw new CommonException(FreightConstants.ErrorCode.BASIC_DATA_FREIGHT_UNIQUE_DEFAULT);
        }
    }

    /**
     * 检查运费模板是否关联了平台产品
     *
     * @param freightTemplateList 运费模版
     * @param tenantId            租户ID
     */
    private void checkProductRelate(final List<FreightTemplate> freightTemplateList, Long tenantId) {
        List<String> codes = new ArrayList<>();
        for (FreightTemplate template : freightTemplateList) {
            codes.add(template.getTemplateCode());
        }
        Map<String, Boolean> map = o2ProductClient.queryTemplate(tenantId, codes);
        if (map.isEmpty()) {
            return;
        }
        for (Map.Entry<String, Boolean> entry : map.entrySet()) {
            Boolean v = entry.getValue();
            if (Boolean.TRUE.equals(v)) {
                throw new CommonException(FreightConstants.ErrorCode.BASIC_DATA_FREIGHT_CAN_NOT_REMOVE);
            }
        }

    }

    /**
     * 更新运费模板缓存(包含运费模板、运费模板明细)
     *
     * @param resultVO 运费模板
     */
    private void saveFreightCache(FreightTemplateManagementVO resultVO) {
        FreightTemplateBO freightTemplate = convertToFreightTemplate(resultVO);
        freightCacheService.saveFreight(freightTemplate);
    }

    /**
     * 批量更新运费模板缓存(仅运费模板信息，不包含运费模板明细)
     *
     * @param freightTemplateList 模版列表
     */
    private void batchSaveFreightCache(List<FreightTemplate> freightTemplateList) {
        if (freightTemplateList != null && !freightTemplateList.isEmpty()) {
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
     * @param freightTemplate 模板
     */
    private void deleteFreightCache(FreightTemplate freightTemplate) {
        FreightTemplateBO template = new FreightTemplateBO();
        template.setFreight(convertToFreight(freightTemplate));
        freightCacheService.deleteFreight(template);
    }

    @Override
    public FreightTemplateManagementVO queryDefaultTemplateDetail(Long organizationId) {
        // 查询默认模板
        final List<FreightTemplate> list = freightTemplateRepository.selectByCondition(Condition.builder(FreightTemplate.class)
                .andWhere(Sqls.custom().andEqualTo(FreightTemplate.FIELD_TENANT_ID, organizationId)
                        .andEqualTo(FreightTemplate.FIELD_DAFAULT_FLAG, 1)).build());

        Assert.isTrue(!CollectionUtils.isEmpty(list), "Default freight template does not exist");
        Assert.isTrue(list.size() == 1, "Default freight template is not unique");

        return queryTemplateAndDetails(list.get(0).getTemplateId(), organizationId);

    }


}
