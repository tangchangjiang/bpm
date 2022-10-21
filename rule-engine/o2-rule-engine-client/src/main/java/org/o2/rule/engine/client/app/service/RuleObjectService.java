package org.o2.rule.engine.client.app.service;

import org.o2.rule.engine.client.app.exception.RuleExecuteException;
import org.o2.rule.engine.client.domain.RuleObject;
import org.o2.rule.engine.client.domain.entity.Rule;
import org.o2.rule.engine.client.infra.rule.RuleExecuteContext;

/**
 * 规则对象Service, 主要针对对象进行转换
 *
 * @author wei.cai@hand-china.com
 * @date 2022/10/13
 */
public interface RuleObjectService {

    /**
     * 通过RuleObject生成上下文
     *
     * @param tenantId 租户ID
     * @param rule 规则
     * @param ruleObject 规则对象
     * @return 返回规则对象上下文
     * @throws RuleExecuteException 规则执行异常
     */
    RuleExecuteContext<String, Object> generateContext(Long tenantId, Rule rule, RuleObject ruleObject) throws RuleExecuteException;

}
