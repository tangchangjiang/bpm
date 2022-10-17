package org.o2.rule.engine.management.infra.util;

import org.o2.rule.engine.management.app.translator.RuleConditionTranslatorStrategy;
import org.o2.rule.engine.management.domain.dto.RuleMiniConditionParameterDTO;
import org.o2.rule.engine.management.domain.entity.Rule;
import java.util.List;

import io.choerodon.core.convertor.ApplicationContextHelper;

/**
 * 规则条件翻译帮助类
 *
 * @author wei.cai@hand-china.com 2020/9/7
 */
public class RuleConditionTranslatorHelper {

    private static final RuleConditionTranslatorStrategy STRATEGY = ApplicationContextHelper.getContext().getBean(RuleConditionTranslatorStrategy.class);

    private RuleConditionTranslatorHelper() {

    }

    public static String translate(final Rule rule, final String conditionCode, final String code, final List<RuleMiniConditionParameterDTO> parameters) {
        return STRATEGY.translate(rule, conditionCode, code, parameters);
    }

}
