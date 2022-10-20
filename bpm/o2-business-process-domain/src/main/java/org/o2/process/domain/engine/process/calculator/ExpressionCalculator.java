package org.o2.process.domain.engine.process.calculator;

import org.o2.rule.engine.client.domain.RuleObject;

public interface ExpressionCalculator {

    boolean calculate(String expression, RuleObject ruleObject, Long tenantId);
}
