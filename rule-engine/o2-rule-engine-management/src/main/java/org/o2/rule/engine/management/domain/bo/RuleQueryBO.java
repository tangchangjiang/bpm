package org.o2.rule.engine.management.domain.bo;

import lombok.Data;
import java.util.List;

/**
 * @author xiang.zhao@hand-chian.com 2022/10/21
 */
@Data
public class RuleQueryBO {
    private Long tenantId;
    private List<Long> ruleIds;
    private List<String> ruleCodes;

    public RuleQueryBO() {

    }

    public RuleQueryBO(Long tenantId) {
        this.tenantId = tenantId;
    }

    public RuleQueryBO(Long tenantId, List<Long> ruleIds) {
        this.tenantId = tenantId;
        this.ruleIds = ruleIds;
    }
}
