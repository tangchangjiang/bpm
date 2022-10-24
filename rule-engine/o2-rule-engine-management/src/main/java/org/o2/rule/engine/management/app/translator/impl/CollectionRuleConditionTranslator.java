package org.o2.rule.engine.management.app.translator.impl;

import org.apache.commons.lang3.StringUtils;
import org.hzero.core.base.BaseConstants;
import org.o2.rule.engine.management.app.translator.RuleConditionTranslator;
import org.o2.rule.engine.management.domain.dto.RuleMiniConditionParameterDTO;
import org.o2.rule.engine.management.domain.entity.Rule;
import org.o2.rule.engine.management.infra.constants.RuleEngineConstants;
import org.springframework.stereotype.Service;
import java.util.Comparator;
import java.util.List;

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
    private static final String LIST_GET = "listGet";

    @Override
    public String conditionCode() {
        return RuleEngineConstants.ComponentCode.COLLECTION;
    }

    @Override
    public String translator(Rule rule, String conditionCode, List<RuleMiniConditionParameterDTO> parameters) {
        parameters.sort(Comparator.comparing(RuleMiniConditionParameterDTO::getPriority));

        final StringBuilder sb = new StringBuilder();
        // 有实体别名取实体别名，没有则取实体code，条件编码同理
        sb.append(StringUtils.defaultString(rule.getRuleEntityAlias(), rule.getEntityCode()))
                .append(BaseConstants.Symbol.POINT).append(conditionCode)
                .append(BaseConstants.Symbol.SPACE).append(LIST_GET);
        for (RuleMiniConditionParameterDTO parameter : parameters) {
            final String compileValue;
            if (RuleEngineConstants.BasicParameter.PARAMETER_VALUE.equals(parameter.getParamCode())) {
                if ((TEXT.equalsIgnoreCase(parameter.getParamFormatCode()) || LIST.equalsIgnoreCase(parameter.getParamFormatCode()))) {
                    compileValue = "\"" + parameter.getParamValue() + "\"";

                } else if (BIG_DECIMAL.equalsIgnoreCase(parameter.getParamFormatCode())) {
                    compileValue = "new java.math.BigDecimal(\"" + parameter.getParamValue() + "\")";
                } else {
                    compileValue = parameter.getParamValue();
                }
                sb.append(BaseConstants.Symbol.SPACE).append(compileValue);
            } else if (RuleEngineConstants.BasicParameter.PARAMETER_OPERATOR.equals(parameter.getParamCode())) {
                String operate;
                switch (parameter.getParamValue()) {
                    case RuleEngineConstants.Operator.GREATER:
                        operate = RuleEngineConstants.Operator.GREATER_CODE;
                        break;
                    case RuleEngineConstants.Operator.GREATER_OR_EQUAL:
                        operate = RuleEngineConstants.Operator.GREATER_OR_EQUAL;
                        break;
                    case RuleEngineConstants.Operator.LESS:
                        operate = RuleEngineConstants.Operator.LESS_OR_EQUAL;
                        break;
                    case RuleEngineConstants.Operator.LESS_OR_EQUAL:
                        operate = RuleEngineConstants.Operator.LESS_OR_EQUAL_CODE;
                        break;
                    case RuleEngineConstants.Operator.EQUAL:
                        operate = RuleEngineConstants.Operator.EQUAL_CODE;
                        break;
                    default:
                        operate = parameter.getParamValue();
                        break;
                }
                sb.append(operate);
            } else {
                sb.append(BaseConstants.Symbol.SPACE).append(parameter.getParamValue());
            }
        }
        return sb.toString().trim();
    }

}
