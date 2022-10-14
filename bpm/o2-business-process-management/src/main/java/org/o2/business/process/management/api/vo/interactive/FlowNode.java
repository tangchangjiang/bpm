package org.o2.business.process.management.api.vo.interactive;

import lombok.Data;

import java.util.Map;

/**
 * @author tangcj
 * @version V1.0
 * @date 2022/10/11 9:55
 */
@Data
public class FlowNode {

    private String beanId;

    private String nodeName;

    private Integer enabledFlag;

    private Map<String, Object> args;

    private String description;
}
