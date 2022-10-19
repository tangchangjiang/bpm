package org.o2.rule.engine.client.app.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.o2.rule.engine.client.app.service.RuleObjectService;
import org.o2.rule.engine.client.domain.RuleObject;
import org.o2.rule.engine.client.domain.entity.Rule;
import org.o2.rule.engine.client.infra.rule.RuleExecuteContext;

/**
 * 规则对象Service
 *
 * @author wei.cai@hand-china.com
 * @date 2022/10/13
 */
@Slf4j
public class RuleObjectServiceImpl implements RuleObjectService {

    @Override
    public RuleExecuteContext<String, Object> generateContext(Long tenantId, Rule rule, RuleObject ruleObject) {

        final RuleExecuteContext<String, Object> context = new RuleExecuteContext<>();

        //支持规则实体别名
        final String code = StringUtils.isEmpty(rule.getEntityAlias()) ? rule.getEntityCode() : rule.getEntityAlias();

        context.put(code, ruleObject);
        return context;
    }

}
