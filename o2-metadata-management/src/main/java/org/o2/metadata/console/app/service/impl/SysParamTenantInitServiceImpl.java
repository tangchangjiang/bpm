package org.o2.metadata.console.app.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.hzero.core.base.BaseConstants;
import org.hzero.mybatis.domian.Condition;
import org.hzero.mybatis.util.Sqls;
import org.o2.metadata.console.app.service.CacheJobService;
import org.o2.metadata.console.app.service.SysParamTenantInitService;
import org.o2.metadata.console.app.service.SystemParamValueService;
import org.o2.metadata.console.infra.entity.SystemParamValue;
import org.o2.metadata.console.infra.entity.SystemParameter;
import org.o2.metadata.console.infra.repository.SystemParamValueRepository;
import org.o2.metadata.console.infra.repository.SystemParameterRepository;
import org.springframework.stereotype.Service;


import java.util.List;
import java.util.stream.Collectors;

/**
 * description
 *
 * @author zhanpeng.jiang@hand-china.com
 * @date 2021/12/17 13:55
 */
@Slf4j
@Service
public class SysParamTenantInitServiceImpl implements SysParamTenantInitService {

    private final SystemParameterRepository systemParameterRepository;
    private final SystemParamValueRepository systemParamValueRepository;
    private final SystemParamValueService systemParamValueService;
    private final CacheJobService cacheJobService;

    public SysParamTenantInitServiceImpl(SystemParameterRepository systemParameterRepository,
                                         SystemParamValueRepository systemParamValueRepository,
                                         SystemParamValueService systemParamValueService,
                                         CacheJobService cacheJobService) {
        this.systemParameterRepository = systemParameterRepository;
        this.systemParamValueRepository = systemParamValueRepository;
        this.systemParamValueService = systemParamValueService;
        this.cacheJobService = cacheJobService;
    }


    /**
     * 租户初始化
     *
     * @param sourceTenantId 源租户Id
     * @param targetTenantId 租户Id
     */
    @Override
    public void tenantInitialize(long sourceTenantId, Long targetTenantId) {
        log.info("initializeSystemParameter start");
        // 1. 查询平台租户（所有已启用）
        final List<SystemParameter> platformSysParams = systemParameterRepository.selectByCondition(Condition.builder(SystemParameter.class)
                .andWhere(Sqls.custom()
                        .andEqualTo(SystemParameter.FIELD_TENANT_ID, sourceTenantId)
                        .andEqualTo(SystemParameter.FIELD_ACTIVE_FLAG, BaseConstants.Flag.YES))
                .build());

        if (CollectionUtils.isEmpty(platformSysParams)) {
            log.info("platformSysParams is empty.");
            return;
        }

        // 2. 查询目标租户是否存在数据
        final List<SystemParameter> targetSysParams = systemParameterRepository.selectByCondition(Condition.builder(SystemParameter.class)
                .andWhere(Sqls.custom()
                        .andEqualTo(SystemParameter.FIELD_TENANT_ID, targetTenantId))
                .build());

        if (CollectionUtils.isNotEmpty(targetSysParams)) {
            // 2.1 删除目标租户数据&缓存
            final List<Long> targetSysParamIds = targetSysParams.stream().map(SystemParameter::getParamId).collect(Collectors.toList());

            final List<SystemParamValue> targetSysParamValues = systemParamValueRepository.selectByCondition(Condition.builder(SystemParameter.class)
                    .andWhere(Sqls.custom()
                            .andEqualTo(SystemParamValue.FIELD_TENANT_ID, targetTenantId)
                            .andIn(SystemParamValue.FIELD_PARAM_ID, targetSysParamIds))
                    .build());
            for (SystemParamValue targetSysParamValue : targetSysParamValues) {
                systemParamValueService.removeSystemParamValue(targetSysParamValue);
            }

            systemParameterRepository.batchDeleteByPrimaryKey(targetSysParams);
        }
        // 3. 插入平台数据到目标租户
        for (SystemParameter platformSysParam : platformSysParams) {
            platformSysParam.setParamId(null);
            platformSysParam.setTenantId(targetTenantId);
            // 查询关联表
            final List<SystemParamValue> platformSysParamValues = systemParamValueRepository.selectByCondition(Condition.builder(SystemParameter.class)
                    .andWhere(Sqls.custom()
                            .andEqualTo(SystemParamValue.FIELD_TENANT_ID, sourceTenantId)
                            .andEqualTo(SystemParamValue.FIELD_PARAM_ID, platformSysParam.getParamId()))
                    .build());
            // 插入，catalog携带插入后主键
            systemParameterRepository.insert(platformSysParam);
            platformSysParamValues.forEach(platformSysParamValue -> {
                platformSysParamValue.setValueId(null);
                platformSysParamValue.setParamId(platformSysParam.getParamId());
                platformSysParamValue.setTenantId(targetTenantId);
            });
            systemParamValueRepository.batchInsert(platformSysParamValues);
        }

        // 4. 刷新缓存
        cacheJobService.refreshSysParameter(targetTenantId);

        log.info("initializeSystemParameter finish");
    }
}
