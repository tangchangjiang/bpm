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
import org.o2.metadata.console.infra.entity.PlatformInfMapping;
import org.o2.metadata.console.infra.repository.PlatformInfMappingRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.stream.Collectors;
import io.choerodon.mybatis.domain.AuditDomain;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.o2.metadata.console.app.service.PlatformInfMappingService;
import java.util.List;


/**
 * 平台信息匹配表应用服务默认实现
 *
 * @author zhilin.ren@hand-china.com 2021-08-02 11:11:28
 */
@Service
@RequiredArgsConstructor
public class PlatformInfMappingServiceImpl implements PlatformInfMappingService {

    private final PlatformInfMappingRepository platformInfMappingRepository;
    private final LovAdapter lovAdapter;
    public static final String REFUND_STATUS = "REFUND_STATUS";
    public static final String ORDER_STATUS = "ORDER_STATUS";
    public static final String SHELF_STATUS= "SHELF_STATUS";
    public static final String REFUND_REASON = "REFUND_REASON";
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<PlatformInfMapping> batchSave(List<PlatformInfMapping> platformInfMappingList) {
        Map<AuditDomain.RecordStatus, List<PlatformInfMapping>> statusMap = platformInfMappingList.stream().collect(Collectors.groupingBy(PlatformInfMapping::get_status));
        // 删除
        if (statusMap.containsKey(AuditDomain.RecordStatus.delete)) {
            List<PlatformInfMapping> deleteList = statusMap.get(AuditDomain.RecordStatus.delete);
            platformInfMappingRepository.batchDeleteByPrimaryKey(deleteList);
        }
        // 更新
        if (statusMap.containsKey(AuditDomain.RecordStatus.update)) {
            List<PlatformInfMapping> updateList = statusMap.get(AuditDomain.RecordStatus.update);
            updateList.forEach(item -> {
                // TODO: 唯一性校验
                platformInfMappingRepository.updateByPrimaryKeySelective(item);
            });
        }
        // 新增
        if (statusMap.containsKey(AuditDomain.RecordStatus.create)) {
            List<PlatformInfMapping> createList = statusMap.get(AuditDomain.RecordStatus.create);
            createList.forEach(item -> {
                // TODO: 唯一性校验
                platformInfMappingRepository.insertSelective(item);
            });
        }
        return platformInfMappingList;
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public PlatformInfMapping save(PlatformInfMapping platformInfMapping) {

        // 唯一性校验
        Condition condition = Condition.builder(PlatformInfMapping.class).andWhere(Sqls.custom()
                .andEqualTo(PlatformInfMapping.FIELD_PLATFORM_CODE, platformInfMapping.getPlatformCode())
                .andEqualTo(PlatformInfMapping.FIELD_INF_TYPE_CODE, platformInfMapping.getInfTypeCode())
                .andEqualTo(PlatformInfMapping.FIELD_PLATFORM_INF_CODE, platformInfMapping.getPlatformInfCode()))
                .build();
        List<PlatformInfMapping> list = platformInfMappingRepository.selectByCondition(condition);
        //保存平台信息匹配表
        if (platformInfMapping.getPlatformInfMappingId() == null) {
            //判断是否重复
            if (CollectionUtils.isNotEmpty(list)){
                throw new CommonException(MetadataConstants.ErrorCode.O2MD_ERROR_CHECK_ERROR, MessageAccessor.getMessage(MetadataConstants.ErrorCode.O2MD_ERROR_CHECK_ERROR).desc());
            }
            setLovMeaning(platformInfMapping);
            platformInfMappingRepository.insertSelective(platformInfMapping);
        } else {
            if (CollectionUtils.isNotEmpty(list)){
                // 判断是否重复
                PlatformInfMapping result = list.get(0);
                if (!result.getPlatformInfMappingId().equals(platformInfMapping.getPlatformInfMappingId())) {
                    throw new CommonException(MetadataConstants.ErrorCode.O2MD_ERROR_CHECK_ERROR,MessageAccessor.getMessage(MetadataConstants.ErrorCode.O2MD_ERROR_CHECK_ERROR).desc());
                }
            }
            setLovMeaning(platformInfMapping);
            platformInfMappingRepository.updateOptional(platformInfMapping,
                    PlatformInfMapping.FIELD_INF_TYPE_CODE,
                    PlatformInfMapping.FIELD_PLATFORM_CODE,
                    PlatformInfMapping.FIELD_INF_CODE,
                    PlatformInfMapping.FIELD_INF_NAME,
                    PlatformInfMapping.FIELD_PLATFORM_INF_CODE,
                    PlatformInfMapping.FIELD_PLATFORM_INF_NAME,
                    PlatformInfMapping.FIELD_TENANT_ID
            );
        }
        return platformInfMapping;
    }

    @Override
    public Page<PlatformInfMapping> page(InfMappingDTO platformInfMapping, PageRequest pageRequest) {
        return PageHelper.doPageAndSort(pageRequest,
                () -> platformInfMappingRepository.listInfMapping(platformInfMapping));
    }

    /**
     * 动态查询值集
     * @param platformInfMapping 参数
     */
    private void setLovMeaning(PlatformInfMapping platformInfMapping) {
        String lovValue = platformInfMapping.getInfTypeCode();
        String lovMeaning = lovAdapter.queryLovMeaning(lovValue,
                platformInfMapping.getTenantId(), platformInfMapping.getInfCode());
        platformInfMapping.setInfName(lovMeaning);

    }
}
