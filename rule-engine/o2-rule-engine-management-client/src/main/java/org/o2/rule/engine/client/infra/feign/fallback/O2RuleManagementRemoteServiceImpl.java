package org.o2.rule.engine.client.infra.feign.fallback;

import lombok.extern.slf4j.Slf4j;
import org.o2.core.helper.JsonHelper;
import org.o2.rule.engine.client.infra.feign.O2RuleManagementRemoteService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import java.util.List;

/**
 * O2 规则远程 Service Fallback
 *
 * @author xiang.zhao@hand-china.com
 * @date 2022/10/17
 */
@Slf4j
public class O2RuleManagementRemoteServiceImpl implements O2RuleManagementRemoteService {

    @Override
    public ResponseEntity<String> enable(Long organizationId, List<Long> ruleIds) {
        log.error("Enable Rules Error, organizationId:[{}], ruleIds:[{}]", organizationId, JsonHelper.objectToString(ruleIds));
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }

    @Override
    public ResponseEntity<String> disable(Long organizationId, List<Long> ruleIds) {
        log.error("Disable Rules Error, organizationId:[{}], ruleIds:[{}]", organizationId, JsonHelper.objectToString(ruleIds));
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }

}
