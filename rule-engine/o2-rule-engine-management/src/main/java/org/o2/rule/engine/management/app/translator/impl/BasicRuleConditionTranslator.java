package org.o2.rule.engine.management.app.translator.impl;

import org.apache.commons.lang3.StringUtils;
import org.hzero.core.base.BaseConstants;
import org.o2.core.helper.JsonHelper;
import org.o2.rule.engine.management.app.translator.RuleConditionTranslator;
import org.o2.rule.engine.management.domain.dto.RuleMiniConditionParameterDTO;
import org.o2.rule.engine.management.domain.dto.RuleParamLovValueDTO;
import org.o2.rule.engine.management.domain.entity.Rule;
import org.o2.rule.engine.management.infra.constants.RuleEngineConstants;
import org.springframework.stereotype.Service;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 基本规则条件转换类
 *
 * @author wei.cai@hand-china.com 2020/9/7
 */
@Service("basicRuleConditionTranslator")
public class BasicRuleConditionTranslator implements RuleConditionTranslator {

    private static final String TEXT = "TEXT";
    private static final String LIST = "LIST";
    private static final String LOV = "LOV";

    @Override
    public String conditionCode() {
        return RuleEngineConstants.ComponentCode.BASIC;
    }

    @Override
    public String translator(Rule rule, String conditionCode, List<RuleMiniConditionParameterDTO> parameters) {
        parameters.sort(Comparator.comparing(RuleMiniConditionParameterDTO::getPriority));

        final StringBuilder sb = new StringBuilder();
        // 有实体别名取实体别名，没有则取实体code，条件编码同理
        sb.append(StringUtils.defaultString(rule.getRuleEntityAlias(), rule.getEntityCode())).append(BaseConstants.Symbol.POINT).append(conditionCode);
        for (RuleMiniConditionParameterDTO parameter : parameters) {
            if (StringUtils.isBlank(parameter.getParamValue())) {
                return sb.toString();
            }
            final String compileValue;
            if (RuleEngineConstants.BasicParameter.PARAMETER_VALUE.equals(parameter.getParamCode())) {
                if (LOV.equals(parameter.getParamEditTypeCode())) {
                    List<RuleParamLovValueDTO> paramLovValues = JsonHelper.stringToArray(parameter.getParamValue(), RuleParamLovValueDTO.class);
                    compileValue = "\"" + StringUtils.join(paramLovValues.stream().map(RuleParamLovValueDTO::getCode).collect(Collectors.toList()), BaseConstants.Symbol.COMMA) + "\"";
                } else {
                    if ((TEXT.equalsIgnoreCase(parameter.getParamFormatCode()) || LIST.equalsIgnoreCase(parameter.getParamFormatCode()))) {
                        compileValue = "\"" + parameter.getParamValue() + "\"";
                    } else {
                        compileValue = parameter.getParamValue();
                    }
                }
            }  else {
                compileValue = parameter.getParamValue();
            }
            sb.append(BaseConstants.Symbol.SPACE).append(compileValue);
        }
        return sb.toString().trim();
    }

}
