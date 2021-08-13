package org.o2.metadata.console.app.service.impl;

import io.choerodon.core.domain.Page;
import io.choerodon.core.exception.CommonException;
import io.choerodon.mybatis.pagehelper.PageHelper;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections.CollectionUtils;
import org.hzero.boot.platform.lov.adapter.LovAdapter;
import org.hzero.core.message.MessageAccessor;
import org.hzero.mybatis.domian.Condition;
import org.hzero.mybatis.util.Sqls;
import org.o2.metadata.console.api.dto.InfMappingDTO;
import org.o2.metadata.console.infra.constant.MetadataConstants;
import org.o2.metadata.console.infra.entity.PlatformInfoMapping;
import org.o2.metadata.console.infra.repository.PlatformInfoMappingRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.stream.Collectors;
import io.choerodon.mybatis.domain.AuditDomain;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.o2.metadata.console.app.service.PlatformInfoMappingService;
import java.util.List;


/**
 * 平台信息匹配表应用服务默认实现
 *
 * @author zhilin.ren@hand-china.com 2021-08-02 11:11:28
 */
@Service
@RequiredArgsConstructor
public class PlatformInfoMappingServiceImpl implements PlatformInfoMappingService {

    private final PlatformInfoMappingRepository platformInfoMappingRepository;
    private final LovAdapter lovAdapter;
    public static final String REFUND_STATUS = "REFUND_STATUS";
    public static final String ORDER_STATUS = "ORDER_STATUS";
    public static final String SHELF_STATUS= "SHELF_STATUS";
    public static final String REFUND_REASON = "REFUND_REASON";
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<PlatformInfoMapping> batchSave(List<PlatformInfoMapping> platformInfoMappingList) {
        Map<AuditDomain.RecordStatus, List<PlatformInfoMapping>> statusMap = platformInfoMappingList.stream().collect(Collectors.groupingBy(PlatformInfoMapping::get_status));
        // 删除
        if (statusMap.containsKey(AuditDomain.RecordStatus.delete)) {
            List<PlatformInfoMapping> deleteList = statusMap.get(AuditDomain.RecordStatus.delete);
            platformInfoMappingRepository.batchDeleteByPrimaryKey(deleteList);
        }
        // 更新
        if (statusMap.containsKey(AuditDomain.RecordStatus.update)) {
            List<PlatformInfoMapping> updateList = statusMap.get(AuditDomain.RecordStatus.update);
            updateList.forEach(item -> {
                // TODO: 唯一性校验
                platformInfoMappingRepository.updateByPrimaryKeySelective(item);
            });
        }
        // 新增
        if (statusMap.containsKey(AuditDomain.RecordStatus.create)) {
            List<PlatformInfoMapping> createList = statusMap.get(AuditDomain.RecordStatus.create);
            createList.forEach(item -> {
                // TODO: 唯一性校验
                platformInfoMappingRepository.insertSelective(item);
            });
        }
        return platformInfoMappingList;
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public PlatformInfoMapping save(PlatformInfoMapping platformInfoMapping) {

        // 唯一性校验
        Condition condition = Condition.builder(PlatformInfoMapping.class).andWhere(Sqls.custom()
                .andEqualTo(PlatformInfoMapping.FIELD_PLATFORM_CODE, platformInfoMapping.getPlatformCode())
                .andEqualTo(PlatformInfoMapping.FIELD_INF_TYPE_CODE, platformInfoMapping.getInfTypeCode())
                .andEqualTo(PlatformInfoMapping.FIELD_PLATFORM_INF_CODE, platformInfoMapping.getPlatformInfCode()))
                .build();
        List<PlatformInfoMapping> list = platformInfoMappingRepository.selectByCondition(condition);
        //保存平台信息匹配表
        if (platformInfoMapping.getPlatformInfMappingId() == null) {
            //判断是否重复
            if (CollectionUtils.isNotEmpty(list)){
                throw new CommonException(MetadataConstants.ErrorCode.O2MD_ERROR_CHECK_ERROR, MessageAccessor.getMessage(MetadataConstants.ErrorCode.O2MD_ERROR_CHECK_ERROR).desc());
            }
            setLovMeaning(platformInfoMapping);
            platformInfoMappingRepository.insertSelective(platformInfoMapping);
        } else {
            if (CollectionUtils.isNotEmpty(list)){
                // 判断是否重复
                PlatformInfoMapping result = list.get(0);
                if (!result.getPlatformInfMappingId().equals(platformInfoMapping.getPlatformInfMappingId())) {
                    throw new CommonException(MetadataConstants.ErrorCode.O2MD_ERROR_CHECK_ERROR,MessageAccessor.getMessage(MetadataConstants.ErrorCode.O2MD_ERROR_CHECK_ERROR).desc());
                }
            }
            setLovMeaning(platformInfoMapping);
            platformInfoMappingRepository.updateOptional(platformInfoMapping,
                    PlatformInfoMapping.FIELD_INF_TYPE_CODE,
                    PlatformInfoMapping.FIELD_PLATFORM_CODE,
                    PlatformInfoMapping.FIELD_INF_CODE,
                    PlatformInfoMapping.FIELD_INF_NAME,
                    PlatformInfoMapping.FIELD_PLATFORM_INF_CODE,
                    PlatformInfoMapping.FIELD_PLATFORM_INF_NAME,
                    PlatformInfoMapping.FIELD_TENANT_ID
            );
        }
        return platformInfoMapping;
    }

    @Override
    public Page<PlatformInfoMapping> page(InfMappingDTO platformInfMapping, PageRequest pageRequest) {
        return PageHelper.doPageAndSort(pageRequest,
                () -> platformInfoMappingRepository.listInfMapping(platformInfMapping));
    }

    /**
     * 动态查询值集
     * @param platformInfoMapping 参数
     */
    private void setLovMeaning(PlatformInfoMapping platformInfoMapping) {
        String lovValue = platformInfoMapping.getInfTypeCode();
        String lovMeaning = lovAdapter.queryLovMeaning(lovValue,
                platformInfoMapping.getTenantId(), platformInfoMapping.getInfCode());
        platformInfoMapping.setInfName(lovMeaning);

    }
}
