package org.o2.rule.engine.client.infra.feign;

import org.o2.core.common.O2Service;
import org.o2.rule.engine.client.infra.feign.fallback.O2RuleManagementRemoteServiceImpl;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import java.util.List;

/**
 * O2 规则远程 Service
 *
 * @author xiang.zhao@hand-china.com
 * @date 2022/10/17
 */
@FeignClient(
        value = O2Service.RuleEngineManagement.NAME,
        path = "/v1",
        fallback = O2RuleManagementRemoteServiceImpl.class
)
public interface O2RuleManagementRemoteService {

    /**
     * 批量启用规则
     *
     * @param organizationId 租户ID
     * @param ruleIds        规则id
     * @return 返回信息
     */
    @PutMapping("/{organizationId}/o2re-rules/enable")
    ResponseEntity<String> enable(@PathVariable Long organizationId, @RequestBody List<Long> ruleIds);

    /**
     * 批量禁用规则
     *
     * @param organizationId 租户ID
     * @param ruleIds        规则id
     * @return 返回信息
     */
    @PutMapping("/{organizationId}/o2re-rules/disable")
    ResponseEntity<String> disable(@PathVariable Long organizationId, @RequestBody List<Long> ruleIds);

}
