package org.o2.metadata.infra.convertor;

import org.o2.metadata.api.co.SystemParamValueCO;
import org.o2.metadata.api.co.SystemParameterCO;
import org.o2.metadata.domain.systemparameter.domain.SystemParamValueDO;
import org.o2.metadata.domain.systemparameter.domain.SystemParameterDO;
import org.o2.metadata.infra.entity.SystemParamValue;
import org.o2.metadata.infra.entity.SystemParameter;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 *
 * 数据转换
 *
 * @author yipeng.zhu@hand-china.com 2021-07-09
 **/
public class SysParameterConverter {
    private SysParameterConverter(){}
    /**
     * DO 转 VO
     * @param systemParameterDO 系统参数
     * @return  bean
     */
    public static SystemParameterCO doToCoObject(SystemParameterDO systemParameterDO){
        if (systemParameterDO == null) {
            return null;
        }
        SystemParameterCO co = new SystemParameterCO();
        co.setParamCode(systemParameterDO.getParamCode());
        co.setDefaultValue(systemParameterDO.getDefaultValue());
        Set<SystemParamValueDO> systemParamValueDO = systemParameterDO.getSetSystemParamValue();
        if (null == systemParamValueDO || systemParamValueDO.isEmpty()) {
            return co;
        }
        Set<SystemParamValueCO> systemParamValueVos = new HashSet<>();
        for (SystemParamValueDO valueDO : systemParamValueDO) {
            SystemParamValueCO value = new SystemParamValueCO();
            value.setParam1(valueDO.getParam1());
            value.setParam2(valueDO.getParam2());
            value.setParam3(valueDO.getParam3());
            value.setParamValue(valueDO.getParamValue());
            value.setParamKey(valueDO.getParamKey());
            systemParamValueVos.add(value);
        }
        co.setSetSystemParamValue(systemParamValueVos);
        return co;
    }
    /**
     * PO 转 DO
     * @param systemParameter 系统参数
     * @return  bean
     */
    public static SystemParameterDO  poToDoObject(SystemParameter systemParameter){
        if (systemParameter == null) {
            return null;
        }
        SystemParameterDO systemParameterDO = new SystemParameterDO();
        systemParameterDO.setParamCode(systemParameter.getParamCode());
        systemParameterDO.setDefaultValue(systemParameter.getDefaultValue());
        Set<SystemParamValue> systemParamValue = systemParameter.getSetSystemParamValue();
        if (null == systemParamValue || systemParamValue.isEmpty()) {
            return systemParameterDO;
        }
        Set<SystemParamValueDO> systemParamValueDos = new HashSet<>();
        for (SystemParamValue value : systemParamValue) {
            SystemParamValueDO valueDO = new SystemParamValueDO();
            valueDO.setParam1(value.getParam1());
            valueDO.setParam2(value.getParam2());
            valueDO.setParam3(value.getParam3());
            valueDO.setParamValue(value.getParamValue());
            valueDO.setParamKey(value.getParamKey());
            systemParamValueDos.add(valueDO);
        }
        systemParameterDO.setSetSystemParamValue(systemParamValueDos);
        return systemParameterDO;
    }
    /**
     * DO 转 VO
     * @param systemParameterDOList 系统参数集合
     * @return  list
     */
    public static List<SystemParameterCO> doToCoListObjects(List<SystemParameterDO> systemParameterDOList) {
        List<SystemParameterCO> systemParameterVOList = new ArrayList<>();
        if (systemParameterDOList == null) {
            return systemParameterVOList;
        }
        for (SystemParameterDO systemParameterDO : systemParameterDOList) {
            systemParameterVOList.add(doToCoObject(systemParameterDO));
        }
        return systemParameterVOList;
    }
    /**
     * PO 转 DO
     * @param systemParameterList 系统参数集合
     * @return  list
     */
    public static List<SystemParameterDO> poToDoListObjects(List<SystemParameter> systemParameterList) {
        List<SystemParameterDO> systemParameterDOList = new ArrayList<>();
        if (systemParameterList == null) {
            return systemParameterDOList;
        }
        for (SystemParameter systemParameter : systemParameterList) {
            systemParameterDOList.add(poToDoObject(systemParameter));
        }
        return systemParameterDOList;
    }
}
