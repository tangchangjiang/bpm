package org.o2.metadata.console.api.co;

import lombok.Data;

import java.util.Set;

/**
 * @author yong.nie@hand-china.com
 * @date 2020/6/12 11:23
 **/

@Data
public class SystemParameterCO {

    private String paramCode;

    private String defaultValue;

    private Set<SystemParamValueCO> setSystemParamValue;
}
