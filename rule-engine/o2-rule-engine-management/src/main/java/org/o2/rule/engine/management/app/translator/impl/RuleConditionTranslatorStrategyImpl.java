package org.o2.rule.engine.management.app.translator.impl;

import com.google.common.collect.Maps;
import org.apache.commons.collections.CollectionUtils;
import org.o2.rule.engine.management.app.translator.RuleConditionTranslator;
import org.o2.rule.engine.management.app.translator.RuleConditionTranslatorStrategy;
import org.o2.rule.engine.management.domain.dto.RuleMiniConditionParameterDTO;
import org.o2.rule.engine.management.domain.entity.Rule;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.stereotype.Service;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import io.choerodon.core.exception.CommonException;

/**
 * 条件翻译策略类
 *
 * @author wei.cai@hand-china.com 2020/9/7
 */
@Service
@ConditionalOnBean(value = {RuleConditionTranslator.class})
public class RuleConditionTranslatorStrategyImpl implements RuleConditionTranslatorStrategy {

    private final List<RuleConditionTranslator> ruleConditionTranslators;
    private Map<String, RuleConditionTranslator> ruleConditionTranslatorsMap;

    public RuleConditionTranslatorStrategyImpl(final List<RuleConditionTranslator> ruleConditionTranslators) {
        this.ruleConditionTranslators = ruleConditionTranslators;
        this.init();
    }

    /**
     * 初始化
     */
    private synchronized void init() {
        if (CollectionUtils.isEmpty(ruleConditionTranslators)) {
            ruleConditionTranslatorsMap = Collections.emptyMap();
            return;
        }
        ruleConditionTranslatorsMap = Maps.newHashMapWithExpectedSize(ruleConditionTranslators.size());
        for (RuleConditionTranslator ruleConditionTranslator : ruleConditionTranslators) {
            if (ruleConditionTranslatorsMap.containsKey(ruleConditionTranslator.conditionCode())) {
                throw new CommonException("there is already has same condition code: " + ruleConditionTranslator.conditionCode() + " ,class: " + ruleConditionTranslator.getClass());
            }
            ruleConditionTranslatorsMap.put(ruleConditionTranslator.conditionCode(), ruleConditionTranslator);
        }
    }

    @Override
    public String translate(Rule rule, String code, List<RuleMiniConditionParameterDTO> parameters) {
        if (!ruleConditionTranslatorsMap.containsKey(code)) {
            throw new CommonException("there is no condition code: " + code);
        }
        return ruleConditionTranslatorsMap.get(code).translator(rule, parameters);
    }
}
