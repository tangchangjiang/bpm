package org.o2.metadata.console.app.service.impl;

import org.apache.commons.collections4.CollectionUtils;
import org.hzero.mybatis.domian.Condition;
import org.hzero.mybatis.helper.SecurityTokenHelper;
import org.hzero.mybatis.util.Sqls;
import org.o2.metadata.console.app.bo.FreightBO;
import org.o2.metadata.console.app.bo.FreightTemplateBO;
import org.o2.metadata.console.app.service.FreightCacheService;
import org.o2.metadata.console.app.service.FreightTemplateDetailService;
import org.o2.metadata.console.app.service.FreightTemplateService;
import org.o2.metadata.core.domain.entity.FreightTemplate;
import org.o2.metadata.core.domain.entity.FreightTemplateDetail;
import org.o2.metadata.core.domain.repository.FreightTemplateDetailRepository;
import org.o2.metadata.core.domain.repository.FreightTemplateRepository;
import org.o2.metadata.core.domain.repository.RegionRepository;
import org.o2.metadata.core.domain.vo.FreightTemplateVO;
import org.o2.metadata.core.infra.constants.BasicDataConstants;
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
public class FreightTemplateServiceImpl extends AbstractFreightCacheOperation implements FreightTemplateService {
    private final FreightTemplateRepository freightTemplateRepository;
    private final FreightTemplateDetailRepository freightTemplateDetailRepository;
    private final FreightTemplateDetailService freightTemplateDetailService;
    private final FreightCacheService freightCacheService;

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
    public FreightTemplateVO queryTemplateAndDetails(final Long templateId) {
        final FreightTemplate freightTemplate = freightTemplateRepository.selectyTemplateId(templateId);
        final FreightTemplateVO freightTemplateVO = new FreightTemplateVO(freightTemplate);

        final List<FreightTemplateDetail> defaultDetailList = freightTemplateDetailRepository.queryDefaultFreightTemplateDetail(templateId);
        freightTemplateVO.setDefaultFreightTemplateDetails(defaultDetailList);
        final List<FreightTemplateDetail> regionDetailList = freightTemplateDetailRepository.queryRegionFreightTemplateDetail(templateId);
        freightTemplateVO.setRegionFreightTemplateDetails(regionDetailList);

        return freightTemplateVO;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public FreightTemplateVO createTemplateAndDetails(final FreightTemplateVO freightTemplateVO) {
        final List<FreightTemplate> list = Arrays.asList(freightTemplateVO);
        final List<FreightTemplateDetail> defaultDetailList = freightTemplateVO.getDefaultFreightTemplateDetails();
        final List<FreightTemplateDetail> regionDetailList = freightTemplateVO.getRegionFreightTemplateDetails();

        checkData(list, false);

        final FreightTemplate result = batchInsert(list).get(0);
        final FreightTemplateVO resultVO = new FreightTemplateVO(result);

        if (CollectionUtils.isNotEmpty(defaultDetailList)) {
            final List<FreightTemplateDetail> savedDefaultDetailList = setTemplateIdAndSave(defaultDetailList, result.getTemplateId(), false);
            resultVO.setDefaultFreightTemplateDetails(savedDefaultDetailList);
        }
        if (CollectionUtils.isNotEmpty(regionDetailList)) {
            final List<FreightTemplateDetail> savedRegionDetailList = setTemplateIdAndSave(regionDetailList, result.getTemplateId(), true);
            resultVO.setRegionFreightTemplateDetails(savedRegionDetailList);
        }

        checkUniqueDefault(freightTemplateVO);

        // 更新redis缓存
        saveFreightCache(resultVO);

        return resultVO;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public FreightTemplateVO updateTemplateAndDetails(final FreightTemplateVO freightTemplateVO) {
        final List<FreightTemplate> list = Arrays.asList(freightTemplateVO);
        final List<FreightTemplateDetail> defaultDetailList = freightTemplateVO.getDefaultFreightTemplateDetails();
        final List<FreightTemplateDetail> regionDetailList = freightTemplateVO.getRegionFreightTemplateDetails();

        checkData(list, true);

        final FreightTemplate result = batchMerge(list).get(0);
        final FreightTemplateVO resultVO = new FreightTemplateVO(result);

        if (CollectionUtils.isNotEmpty(defaultDetailList)) {
            final List<FreightTemplateDetail> savedDefaultDetailList = freightTemplateDetailService.defaultBatchMerge(defaultDetailList);
            resultVO.setDefaultFreightTemplateDetails(savedDefaultDetailList);
        }
        if (CollectionUtils.isNotEmpty(regionDetailList)) {
            final List<FreightTemplateDetail> savedRegionDetailList = freightTemplateDetailService.regionBatchMerge(regionDetailList);
            resultVO.setRegionFreightTemplateDetails(savedRegionDetailList);
        }

        checkUniqueDefault(freightTemplateVO);

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
            freightTemplate.setEnabledFlag(Integer.valueOf(0));
            freightTemplateRepository.updateOptional(freightTemplate,FreightTemplate.FIELD_ENABLED_FLAG);
        }

        return true;
    }

    @Override
    public List<FreightTemplate> batchInsert(final List<FreightTemplate> freightTemplateList) {
        checkData(freightTemplateList, false);
        List<FreightTemplate> freightTemplates = freightTemplateRepository.batchInsertSelective(freightTemplateList);

        // 更新缓存
        batchSaveFreightCache(freightTemplates);

        return freightTemplates;
    }

    @Override
    public List<FreightTemplate> batchUpdate(final List<FreightTemplate> freightTemplateList) {
        checkData(freightTemplateList, true);
        List<FreightTemplate> freightTemplates = freightTemplateRepository.batchUpdateByPrimaryKey(freightTemplateList);

        // 更新缓存
        batchSaveFreightCache(freightTemplates);

        return freightTemplates;
    }

    @Override
    public List<FreightTemplate> batchMerge(final List<FreightTemplate> freightTemplateList) {
        final Map<String, Object> map = new HashMap<>(freightTemplateList.size());
        final List<FreightTemplate> updateList = new ArrayList<>();
        final List<FreightTemplate> insertList = new ArrayList<>();
        for (int i = 0; i < freightTemplateList.size(); i++) {
            final FreightTemplate freightTemplate = freightTemplateList.get(i);
            freightTemplate.validate();
            // 数据库查重
            Assert.isTrue(!freightTemplate.exist(freightTemplateRepository), "数据库存在相同的运费模板");
            // list查重
            Assert.isTrue(map.get(freightTemplate.getTemplateCode()) == null, "提交数据中存在相同的运费模板");
            if (freightTemplate.getTemplateId() != null) {
                SecurityTokenHelper.validToken(freightTemplate);
                updateList.add(freightTemplate);
            } else {
                insertList.add(freightTemplate);
            }
            map.put(freightTemplate.getTemplateCode(), i);
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
        if (freightTemplate.getDafaultFlag() != null && freightTemplate.getDafaultFlag().intValue() == 1) {
            return true;
        }

        // 如果运费模板不包邮，验证有且只有一个默认运费行
        final Sqls sqlcommand = Sqls.custom();
        sqlcommand.andEqualTo(FreightTemplateDetail.FIELD_TEMPLATE_ID, templateId)
                .andEqualTo(FreightTemplateDetail.FIELD_DEFAULT_FLAG, Integer.valueOf(1))
                .andIsNull(FreightTemplateDetail.FIELD_REGION_ID);

        final Condition cond = Condition.builder(FreightTemplateDetail.class).andWhere(sqlcommand).build();

        return freightTemplateDetailRepository.selectCountByCondition(cond) == 1;
    }

    @Override
    public void refreshCache(Long templateId) {
        FreightTemplateVO freightTemplateVO = queryTemplateAndDetails(templateId);

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
            freightTemplate.setEnabledFlag(freightTemplate.getEnabledFlag()==null?Integer.valueOf(1):freightTemplate.getEnabledFlag());
            if (isCheckId) {
                Assert.notNull(freightTemplate.getTemplateId(), BasicDataConstants.ErrorCode.BASIC_DATA_FREIGHT_ID_IS_NULL);
            }
            freightTemplate.validate();
            // 数据库查重
            Assert.isTrue(!freightTemplate.exist(freightTemplateRepository), BasicDataConstants.ErrorCode.BASIC_DATA_FREIGHT_DUPLICATE_CODE);
            // list查重
            Assert.isTrue(map.get(freightTemplate.getTemplateCode()) == null, BasicDataConstants.ErrorCode.BASIC_DATA_FREIGHT_DUPLICATE_CODE);
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
        for (final FreightTemplateDetail detail : freightTemplateDetailList) {
            detail.setTemplateId(templateId);
        }
        return freightTemplateDetailService.batchInsert(freightTemplateDetailList, isRegion);
    }

    /**
     * 验证包邮的运费模板是否有且只有一个默认模板明细行
     *
     * @param freightTemplate
     */
    private void checkUniqueDefault(final FreightTemplate freightTemplate) {
        Assert.isTrue(uniqueDefaultValidate(freightTemplate.getTemplateId()), BasicDataConstants.ErrorCode.BASIC_DATA_FREIGHT_UNIQUE_DEFAULT);
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
            Assert.isTrue(!relateFlag, BasicDataConstants.ErrorCode.BASIC_DATA_FREIGHT_CAN_NOT_REMOVE);
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

        return  queryTemplateAndDetails(list.get(0).getTemplateId());

    }


}
