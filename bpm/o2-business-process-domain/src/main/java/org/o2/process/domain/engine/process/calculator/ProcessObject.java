package org.o2.process.domain.engine.process.calculator;

import lombok.Data;
import org.o2.rule.engine.client.domain.RuleObject;

import java.util.Map;

/**
 * @author tangcj
 * @version V1.0
 * @date 2022/10/12 15:34
 */
@Data
public class ProcessObject implements RuleObject {

    private Map<String, Object> dataMap;

}
