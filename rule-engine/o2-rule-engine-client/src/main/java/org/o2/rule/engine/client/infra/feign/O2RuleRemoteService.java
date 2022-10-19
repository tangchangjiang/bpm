package org.o2.rule.engine.client.infra.feign;

import org.o2.core.common.O2Service;
import org.o2.rule.engine.client.infra.feign.fallback.O2RuleRemoteServiceImpl;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * O2 规则远程 Service
 *
 * @author wei.cai@hand-china.com
 * @date 2022/10/17
 */
@FeignClient(
        value = O2Service.RuleEngineManagement.NAME,
        path = "/v1",
        fallback = O2RuleRemoteServiceImpl.class
)
public interface O2RuleRemoteService {

    /**
     * 查询规则通过Code
     *
     * @param organizationId 租户ID
     * @param ruleCode       规则编码
     * @return 返回信息
     */
    @GetMapping("/{organizationId}/o2re-rules-internal/{ruleCode}")
    ResponseEntity<String> findRuleByCode(@PathVariable Long organizationId, @PathVariable String ruleCode);

}
