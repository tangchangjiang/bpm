package org.o2.metadata.console.app.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.hzero.mybatis.domian.Condition;
import org.hzero.mybatis.util.Sqls;
import org.o2.metadata.console.app.service.MallLangPromptTenantInitService;
import org.o2.metadata.console.infra.entity.MallLangPrompt;
import org.o2.metadata.console.infra.repository.MallLangPromptRepository;
import org.springframework.stereotype.Service;


import java.util.List;

/**
 * 多语言文件租户初始化
 *
 * @author zhanpeng.jiang@hand-china.com
 * @date 2021/12/17 15:18
 */
@Slf4j
@Service
public class MallLangPromptTenantInitServiceImpl implements MallLangPromptTenantInitService {

    private final MallLangPromptRepository mallLangPromptRepository;

    public MallLangPromptTenantInitServiceImpl(MallLangPromptRepository mallLangPromptRepository) {
        this.mallLangPromptRepository = mallLangPromptRepository;
    }

    @Override
    public void tenantInitialize(long sourceTenantId, Long targetTenantId) {
        log.info("initializeMallLangPrompt start");
        // 1. 查询平台租户（所有已启用）
        final List<MallLangPrompt> platformMallLangPrompts = mallLangPromptRepository.selectByCondition(Condition.builder(MallLangPrompt.class)
                .andWhere(Sqls.custom()
                        .andEqualTo(MallLangPrompt.FIELD_TENANT_ID, sourceTenantId))
                .build());

        if (CollectionUtils.isEmpty(platformMallLangPrompts)) {
            log.info("platformMallLangPrompts is empty.");
            return;
        }

        // 2. 查询目标租户是否存在数据
        final List<MallLangPrompt> targetMallLangPrompts = mallLangPromptRepository.selectByCondition(Condition.builder(MallLangPrompt.class)
                .andWhere(Sqls.custom()
                        .andEqualTo(MallLangPrompt.FIELD_TENANT_ID, targetTenantId))
                .build());

        if (CollectionUtils.isNotEmpty(targetMallLangPrompts)) {
            // 2.1 删除目标租户数据
            mallLangPromptRepository.batchDeleteByPrimaryKey(targetMallLangPrompts);
        }

        // 3. 插入平台数据到目标租户
        platformMallLangPrompts.forEach(mallLangPrompt -> {
            mallLangPrompt.setLangPromptId(null);
            mallLangPrompt.setTenantId(targetTenantId);
        });
        mallLangPromptRepository.batchInsert(platformMallLangPrompts);

        log.info("initializeMallLangPrompt finish, tenantId[{}]", targetTenantId);
    }
}
