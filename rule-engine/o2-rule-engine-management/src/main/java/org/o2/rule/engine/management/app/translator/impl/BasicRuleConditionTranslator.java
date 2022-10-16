package org.o2.rule.engine.management.app.translator.impl;

import org.o2.rule.engine.management.app.translator.RuleConditionTranslator;
import org.o2.rule.engine.management.domain.dto.RuleMiniConditionParameterDTO;
import org.o2.rule.engine.management.domain.entity.Rule;
import org.o2.rule.engine.management.infra.constants.RuleEngineConstants;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Map;

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
    public String translator(Rule rule, List<RuleMiniConditionParameterDTO> parameters) {
        final Map<String, RuleMiniConditionParameterDTO> valueMap = convertToMap(parameters);

        final String parameterCode = valueMap.get(RuleEngineConstants.BasicParameter.PARAMETER_PARAMETER_CODE).getParameterValue();
        final String operator = valueMap.get(RuleEngineConstants.BasicParameter.PARAMETER_OPERATOR).getParameterValue();
        final String value = valueMap.get(RuleEngineConstants.BasicParameter.PARAMETER_VALUE).getParameterValue();
        final String compileValue;
        if (STRING.equalsIgnoreCase(valueMap.get(RuleEngineConstants.BasicParameter.PARAMETER_VALUE).getParamEditTypeCode())
                || LIST.equalsIgnoreCase(valueMap.get(RuleEngineConstants.BasicParameter.PARAMETER_VALUE).getParamEditTypeCode())) {
            compileValue = "\"" + value + "\"";
        } else {
            compileValue = value;
        }

        return parameterCode + " " + operator + " " + compileValue;
    }

}
