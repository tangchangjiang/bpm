package org.o2.process.domain.engine.definition.Activity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.o2.process.domain.infra.ProcessEngineConstants;

import java.util.Map;

/**
 * @author tangcj
 * @version V1.0
 * @date 2022/9/28 15:01
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class ServiceTask extends BaseTask {

    private String beanId;

    private Integer enabledFlag;

    private Map<String, String> args;

    @Override
    public String getType() {
        return ProcessEngineConstants.FlowElementType.SERVICE_TASK;
    }
}
