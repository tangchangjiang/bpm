package org.o2.feignclient.metadata.domain.co;

import java.util.Set;

import lombok.Data;

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