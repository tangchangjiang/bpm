package org.o2.rule.engine.client;

import org.hzero.core.util.ResponseUtils;
import org.o2.core.response.OperateResponse;
import org.o2.rule.engine.client.infra.feign.O2RuleManagementRemoteService;
import java.util.List;

/**
 * 规则引擎管理客户端
 *
 * @author xiang.zhao@hand-chian.com 2022/10/19
 * @date 2022/10/19
 */
public class RuleEngineManagementClient {
    private final O2RuleManagementRemoteService o2RuleManagementRemoteService;

    public RuleEngineManagementClient(final O2RuleManagementRemoteService o2RuleManagementRemoteService) {
        this.o2RuleManagementRemoteService = o2RuleManagementRemoteService;
    }

    /**
     * 批量启用规则
     *
     * @param organizationId 租户ID
     * @param ruleIds        规则id
     * @return 返回信息
     */
    public OperateResponse enable(Long organizationId, List<Long> ruleIds) {
        return ResponseUtils.getResponse(o2RuleManagementRemoteService.enable(organizationId, ruleIds), OperateResponse.class);
    }

    /**
     * 批量禁用规则
     *
     * @param organizationId 租户ID
     * @param ruleIds        规则id
     * @return 返回信息
     */
    public OperateResponse disable(Long organizationId, List<Long> ruleIds) {
        return ResponseUtils.getResponse(o2RuleManagementRemoteService.disable(organizationId, ruleIds), OperateResponse.class);
    }
}
