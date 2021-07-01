package org.o2.metadata.console.api.vo;

import lombok.Data;

import java.util.Set;

/**
 * @author hongyun.wang01@hand-china.com
 */
@Data
public class SystemParamDetailVO {
    private String paramCode;

    private String kvValue;

    private Set<SystemParamValueVO> setValue;
}
