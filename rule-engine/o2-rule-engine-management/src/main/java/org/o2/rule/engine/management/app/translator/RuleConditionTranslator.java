package org.o2.rule.engine.management.app.translator;

import org.o2.rule.engine.management.domain.dto.RuleMiniConditionParameterDTO;
import org.o2.rule.engine.management.domain.entity.Rule;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * RuleConditionTranslator 规则条件翻译接口
 *
 * @author wei.cai@hand-china.com 2020/9/7
 */
public interface RuleConditionTranslator {

    /**
     * 条件编码
     * @return 条件编码
     */
    String conditionCode();

    /**
     * 翻译
     * @param parameters 参数
     * @return 矫健
     */
    default String translator(final List<RuleMiniConditionParameterDTO> parameters) {
        return null;
    }

    /**
     * 翻译
     * @param rule 规则
     * @param parameters 参数
     * @return 矫健
     */
    default String translator(final Rule rule, final List<RuleMiniConditionParameterDTO> parameters) {
        return translator(parameters);
    }

    /**
     * 转成MAP
     * @param parameters 参数值
     * @return map
     */
    default Map<String, RuleMiniConditionParameterDTO> convertToMap(final List<RuleMiniConditionParameterDTO> parameters) {
        return parameters.stream().collect(Collectors.toMap(RuleMiniConditionParameterDTO::getParameterCode, Function.identity()));
    }

}
