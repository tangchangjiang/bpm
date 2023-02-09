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
 * @auther: changjiang.tang
 * @date: 2023/02/09/14:15
 * @since: V1.7.0
 */
@Service
public class StringRuleConditionTranslator implements RuleConditionTranslator {
    @Override
    public String conditionCode() {
        return RuleEngineConstants.ComponentCode.STRING;
    }

    @Override
    public String translator(Rule rule, String conditionCode, List<RuleMiniConditionParameterDTO> parameters) {
        parameters.sort(Comparator.comparing(RuleMiniConditionParameterDTO::getPriority));

        final StringBuilder sb = new StringBuilder();
        // 有实体别名取实体别名，没有则取实体code，条件编码同理
        String entityName = StringUtils.defaultString(rule.getRuleEntityAlias(), rule.getEntityCode()) + BaseConstants.Symbol.POINT + conditionCode;
        sb.append(entityName);
        for (RuleMiniConditionParameterDTO parameter : parameters) {
            if (StringUtils.isBlank(parameter.getParamValue())) {
                return sb.toString();
            }
            String compileValue = null;
            if (RuleEngineConstants.BasicParameter.PARAMETER_OPERATOR.equals(parameter.getParamCode())) {
                if(RuleEngineConstants.StringOperator.IS_NOT_BLANK.equals(parameter.getParamValue())){
                    final StringBuilder blankExpress = new StringBuilder();
                    blankExpress.append(RuleEngineConstants.StringOperator.IS_NOT_BLANK)
                            .append(BaseConstants.Symbol.LEFT_BRACE)
                            .append(entityName)
                            .append(BaseConstants.Symbol.RIGHT_BRACE);
                    return blankExpress.toString().trim();
                }
            }

            sb.append(BaseConstants.Symbol.SPACE).append(compileValue);
        }
        return sb.toString().trim();
    }

}
