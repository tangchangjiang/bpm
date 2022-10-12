package org.o2.process.domain.engine.process.calculator;

import java.util.Map;

public interface ExpressionCalculator {

    boolean calculate(String expression, Map<String, Object> dataMap, Long tenantId);

    String getExpressType();
}
