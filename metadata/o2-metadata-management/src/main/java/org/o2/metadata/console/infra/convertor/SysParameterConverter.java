package org.o2.metadata.console.infra.convertor;

import org.o2.metadata.console.api.co.SystemParamValueCO;
import org.o2.metadata.console.api.co.SystemParameterCO;
import org.o2.metadata.console.infra.entity.SystemParamValue;
import org.o2.metadata.console.infra.entity.SystemParameter;
import org.o2.metadata.domain.systemparameter.domain.SystemParamValueDO;
import org.o2.metadata.domain.systemparameter.domain.SystemParameterDO;

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
    private SysParameterConverter() {
        //无需实现
    }

    /**
     * DO 转 CO
     * @param systemParameterDO 系统参数
     * @return  bean
     */
    public static SystemParameterCO doToCoObject(SystemParameterDO systemParameterDO) {
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
        Set<SystemParamValueCO> valueCos = new HashSet<>();
        for (SystemParamValueDO valueDO : systemParamValueDO) {
            SystemParamValueCO valueCo = new SystemParamValueCO();
            valueCo.setParam1(valueDO.getParam1());
            valueCo.setParam2(valueDO.getParam2());
            valueCo.setParam3(valueDO.getParam3());
            valueCo.setParamValue(valueDO.getParamValue());
            valueCo.setParamKey(valueDO.getParamKey());
            valueCos.add(valueCo);
        }
        co.setSetSystemParamValue(valueCos);
        return co;
    }

    /**
     * PO 转 DO
     * @param systemParameter 系统参数
     * @return  bean
     */
    public static SystemParameterDO  poToDoObject(SystemParameter systemParameter) {
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
     * DO 转 CO
     * @param systemParameterDOList 系统参数集合
     * @return  list
     */
    public static List<SystemParameterCO> doToCoListObjects(List<SystemParameterDO> systemParameterDOList) {
        List<SystemParameterCO> cos = new ArrayList<>();
        if (systemParameterDOList == null) {
            return cos;
        }
        for (SystemParameterDO systemParameterDO : systemParameterDOList) {
            cos.add(doToCoObject(systemParameterDO));
        }
        return cos;
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

    /**
     * po转po
     *
     * @param systemParameter 系统参数
     * @return 系统参数
     */
    public static SystemParameter poToPoObject(SystemParameter systemParameter) {
        if (systemParameter == null) {
            return null;
        }
        SystemParameter sysParam = new SystemParameter();
        sysParam.setParamCode(systemParameter.getParamCode());
        sysParam.setParamName(systemParameter.getParamName());
        sysParam.setParamTypeCode(systemParameter.getParamTypeCode());
        sysParam.setActiveFlag(systemParameter.getActiveFlag());
        sysParam.setRemark(systemParameter.getRemark());
        sysParam.setDefaultValue(systemParameter.getDefaultValue());

        return systemParameter;
    }
}
