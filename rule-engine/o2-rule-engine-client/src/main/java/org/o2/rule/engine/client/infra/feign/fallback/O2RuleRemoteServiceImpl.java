package org.o2.rule.engine.client.infra.feign.fallback;

import lombok.extern.slf4j.Slf4j;
import org.o2.rule.engine.client.infra.feign.O2RuleRemoteService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

/**
 * O2 规则远程 Service Fallback
 *
 * @author wei.cai@hand-china.com
 * @date 2022/10/17
 */
@Slf4j
public class O2RuleRemoteServiceImpl implements O2RuleRemoteService {

    @Override
    public ResponseEntity<String> findRuleByCode(Long organizationId, String ruleCode) {
        log.error("Find Rule By Code Error, organizationId:[{}], ruleCode:[{}]", organizationId, ruleCode);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }

}
