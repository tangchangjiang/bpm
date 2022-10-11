package org.o2.process.domain.engine.process.provider;

import io.choerodon.core.exception.CommonException;
import lombok.extern.slf4j.Slf4j;
import org.o2.process.domain.engine.process.calculator.ExpressionCalculator;
import org.o2.process.domain.infra.ProcessEngineConstants;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author tangcj
 * @version V1.0
 * @date 2022/9/30 15:10
 */
@Slf4j
@Service
public class CalculatorProvider {

    public Map<String, ExpressionCalculator> calculatorMap = new HashMap<>();

   public CalculatorProvider(List<ExpressionCalculator> expressionCalculatorList){
       expressionCalculatorList.forEach(calculator -> calculatorMap.put(calculator.getExpressType(), calculator));
   }


   public boolean calculate(String expressType, String expression, Map<String, Object> dataMap){
       ExpressionCalculator expressionCalculator = calculatorMap.get(expressType);
       if(null == expressionCalculator){
           throw new CommonException(ProcessEngineConstants.ErrorCode.UNSUPPORTED_EXPRESS_TYPE);
       }
       return expressionCalculator.calculate(expression, dataMap);
   }
}
