package org.o2.metadata.console.infra.convertor;

import org.o2.metadata.console.api.dto.SystemParamValueDTO;
import org.o2.metadata.console.infra.entity.SystemParamValue;

/**
 *
 * 系统参数值转换
 *
 * @author yipeng.zhu@hand-china.com 2021-07-23
 **/
public class SystemParamValueConvertor {

    public static SystemParamValue dtoToPoObject(SystemParamValueDTO systemParamValueDTO){

        if (systemParamValueDTO == null) {
            return null;
        }
        SystemParamValue systemParamValue = new SystemParamValue();
        systemParamValue.setValueId(systemParamValueDTO.getValueId());
        systemParamValue.setParamId(systemParamValueDTO.getParamId());
        systemParamValue.setParamValue(systemParamValueDTO.getParamValue());
        systemParamValue.setParam1(systemParamValueDTO.getParam1());
        systemParamValue.setParam2(systemParamValueDTO.getParam2());
        systemParamValue.setParam3(systemParamValueDTO.getParam3());
        systemParamValue.setTenantId(systemParamValueDTO.getTenantId());
        systemParamValue.setParamKey(systemParamValueDTO.getParamKey());
        systemParamValue.setDescription(systemParamValueDTO.getDescription());
        return systemParamValue;
    }
}
