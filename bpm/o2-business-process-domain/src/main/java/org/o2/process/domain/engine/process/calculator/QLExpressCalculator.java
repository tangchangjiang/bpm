package org.o2.process.domain.engine.process.calculator;

import lombok.extern.slf4j.Slf4j;
import org.o2.process.domain.infra.ProcessEngineConstants;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * @author tangcj
 * @version V1.0
 * @date 2022/9/30 16:40
 */
@Slf4j
@Service
public class QLExpressCalculator implements ExpressionCalculator{

    @Override
    public boolean calculate(String expression, Map<String, Object> dataMap) {
        //todo
        return true;
    }

    @Override
    public String getExpressType() {
        return ProcessEngineConstants.ExpressType.QL_EXPRESS;
    }
}
