package org.o2.metadata.console.app.bo;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author changjiang
 * @date 2022/5/26
 */
@Data
public class SourcingConfigUpdateBO {

    private List<String> cacheNames;

    private String operationClassName;

    private Long operator;

    private LocalDateTime operationTime;
}
