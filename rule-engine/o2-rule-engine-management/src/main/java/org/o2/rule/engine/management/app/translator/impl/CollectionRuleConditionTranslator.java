package org.o2.rule.engine.management.app.translator.impl;

import org.apache.commons.lang3.StringUtils;
import org.o2.rule.engine.management.app.translator.RuleConditionTranslator;
import org.o2.rule.engine.management.domain.dto.CollectionOperator;
import org.o2.rule.engine.management.domain.dto.RuleMiniConditionParameterDTO;
import org.o2.rule.engine.management.domain.entity.Rule;
import org.o2.rule.engine.management.infra.constants.RuleEngineConstants;
import org.springframework.stereotype.Service;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 集合规则条件转换类
 *
 * @author xiang.zhao@hand-china.com 2022/10/24
 */
@Service("collectionRuleConditionTranslator")
public class CollectionRuleConditionTranslator implements RuleConditionTranslator {

    private static final String TEXT = "TEXT";
    private static final String LIST = "LIST";
    private static final String BIG_DECIMAL = "BIGDECIMAL";
    private static final String EXPRESSING_FORMAT = "(%s.%s listGet %s) %s%s %s";

    @Override
    public String conditionCode() {
        return RuleEngineConstants.ComponentCode.COLLECTION;
    }

    @Override
    public String translator(Rule rule, String conditionCode, List<RuleMiniConditionParameterDTO> parameters) {
        parameters.sort(Comparator.comparing(RuleMiniConditionParameterDTO::getPriority));
        final Map<String, RuleMiniConditionParameterDTO> parameterMap = parameters.stream().collect(Collectors.toMap(RuleMiniConditionParameterDTO::getParamCode, Function.identity()));
        final RuleMiniConditionParameterDTO paramValue = parameterMap.get(RuleEngineConstants.BasicParameter.PARAMETER_VALUE);
        final RuleMiniConditionParameterDTO paramOperator = parameterMap.get(RuleEngineConstants.BasicParameter.PARAMETER_OPERATOR);
        final RuleMiniConditionParameterDTO paramCollectionOperator = parameterMap.get(RuleEngineConstants.BasicParameter.PARAMETER_C_OPERATOR);
        final RuleMiniConditionParameterDTO paramProperty = parameterMap.get(RuleEngineConstants.BasicParameter.PARAMETER_PROPERTY);

        String value;
        if ((TEXT.equalsIgnoreCase(paramValue.getParamFormatCode()) || LIST.equalsIgnoreCase(paramValue.getParamFormatCode()))) {
            value = "\"" + paramValue.getParamValue() + "\"";

        } else if (BIG_DECIMAL.equalsIgnoreCase(paramValue.getParamFormatCode())) {
            value = "new java.math.BigDecimal(\"" + paramValue.getParamValue() + "\")";
        } else {
            value = paramValue.getParamValue();
        }

        final String expression = String.format(EXPRESSING_FORMAT,
                StringUtils.defaultString(rule.getRuleEntityAlias(), rule.getEntityCode()),
                conditionCode,
                paramProperty.getParamValue(),
                paramCollectionOperator.getParamValue(),
                CollectionOperator.translatorOperator(paramOperator.getParamValue()),
                value);
        return expression;
    }

}
