package org.o2.metadata.console.app.service.impl;

import io.choerodon.core.exception.CommonException;
import lombok.RequiredArgsConstructor;
import org.hzero.mybatis.domian.Condition;
import org.hzero.mybatis.util.Sqls;
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
        int count = platformInfMappingRepository.selectCountByCondition(condition);
        if (count > 0 ){
            throw new CommonException(MetadataConstants.ErrorCode.O2MD_ERROR_CHECK_ERROR);
        }
        //保存平台信息匹配表
        if (platformInfMapping.getPlatformInfMappingId() == null) {
            platformInfMappingRepository.insertSelective(platformInfMapping);
        } else {
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
}
