package org.o2.metadata.api.vo;

import lombok.Data;

import java.util.Set;

/**
 * @author hongyun.wang01@hand-china.com
 */
@Data
public class SystemParameterVO {
    private String paramCode;

    private String defaultValue;

    private Set<SystemParamValueVO> setSystemParamValue;
}
