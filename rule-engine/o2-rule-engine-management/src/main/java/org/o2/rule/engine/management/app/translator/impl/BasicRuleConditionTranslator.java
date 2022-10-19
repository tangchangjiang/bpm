package org.o2.rule.engine.management.app.translator.impl;

import org.apache.commons.lang3.StringUtils;
import org.o2.rule.engine.management.app.translator.RuleConditionTranslator;
import org.o2.rule.engine.management.domain.dto.RuleMiniConditionParameterDTO;
import org.o2.rule.engine.management.domain.entity.Rule;
import org.o2.rule.engine.management.infra.constants.RuleEngineConstants;
import org.springframework.stereotype.Service;
import java.util.Comparator;
import java.util.List;

/**
 * 基本规则条件转换类
 *
 * @author wei.cai@hand-china.com 2020/9/7
 */
@Service("basicRuleConditionTranslator")
public class BasicRuleConditionTranslator implements RuleConditionTranslator {

    private static final String STRING = "string";
    private static final String LIST = "list";

    @Override
    public String conditionCode() {
        return RuleEngineConstants.ComponentCode.BASIC;
    }

    @Override
    public String translator(Rule rule, String conditionCode, List<RuleMiniConditionParameterDTO> parameters) {
        parameters.sort(Comparator.comparing(RuleMiniConditionParameterDTO::getPriority));

        final StringBuilder sb = new StringBuilder();
        // 有实体别名取实体别名，没有则取实体code，条件编码同理
        sb.append(StringUtils.defaultString(rule.getRuleEntityAlias(), rule.getEntityCode())).append(conditionCode);
        for (RuleMiniConditionParameterDTO parameter : parameters) {

            final String compileValue;
            if (STRING.equalsIgnoreCase(parameter.getParamEditTypeCode())
                    || LIST.equalsIgnoreCase(parameter.getParamEditTypeCode())) {
                compileValue = "\"" + parameter.getParamValue() + "\"";
            } else {
                compileValue = parameter.getParamValue();
            }
            sb.append(compileValue).append(" ");
        }
        return sb.toString().trim();
    }

}
