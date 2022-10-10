package org.o2.metadata.api.co;

import lombok.Data;

import java.util.Set;

/**
 * @author hongyun.wang01@hand-china.com
 */
@Data
public class SystemParameterCO {
    private String paramCode;

    private String defaultValue;

    private Set<SystemParamValueCO> setSystemParamValue;
}