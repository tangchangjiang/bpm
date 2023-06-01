package org.o2.metadata.console.infra.convertor;

import org.apache.commons.collections4.CollectionUtils;
import org.o2.metadata.console.api.dto.SystemParamValueDTO;
import org.o2.metadata.console.infra.entity.SystemParamValue;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 *
 * 系统参数值转换
 *
 * @author yipeng.zhu@hand-china.com 2021-07-23
 **/
public class SystemParamValueConverter {
    private SystemParamValueConverter() {
    }

    /**
     * dto 转 po
     * @param systemParamValueDTO 系统参数
     * @return  po
     */
    public static SystemParamValue dtoToPoObject(SystemParamValueDTO systemParamValueDTO) {

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
        systemParamValue.setCreationDate(systemParamValueDTO.getCreationDate());
        systemParamValue.setCreatedBy(systemParamValueDTO.getCreatedBy());
        systemParamValue.setLastUpdateDate(systemParamValueDTO.getLastUpdateDate());
        systemParamValue.setLastUpdatedBy(systemParamValueDTO.getLastUpdatedBy());
        systemParamValue.setObjectVersionNumber(systemParamValueDTO.getObjectVersionNumber());
        systemParamValue.setTableId(systemParamValueDTO.getTableId());
        systemParamValue.set_tls(systemParamValueDTO.get_tls());
        systemParamValue.set_status(systemParamValueDTO.get_status());
        systemParamValue.setFlex(systemParamValueDTO.getFlex());
        systemParamValue.set_token(systemParamValueDTO.get_token());
        return systemParamValue;
    }

    /**
     * po转po
     *
     * @param systemParamValues 系统参数值
     * @return 系统参数值
     */
    public static List<SystemParamValue> poToPoList(List<SystemParamValue> systemParamValues) {
        if (CollectionUtils.isEmpty(systemParamValues)) {
            return Collections.emptyList();
        }
        List<SystemParamValue> systemParamValueList = new ArrayList<>();
        for (SystemParamValue systemParamValue : systemParamValues) {
            systemParamValueList.add(toSystemParamValue(systemParamValue));
        }
        return systemParamValueList;
    }

    public static SystemParamValue toSystemParamValue(SystemParamValue systemParamValue) {
        if (systemParamValue == null) {
            return null;
        }
        SystemParamValue sysParamValue = new SystemParamValue();
        sysParamValue.setParamValue(systemParamValue.getParamValue());
        sysParamValue.setParam1(systemParamValue.getParam1());
        sysParamValue.setParam2(systemParamValue.getParam2());
        sysParamValue.setParam3(systemParamValue.getParam3());
        sysParamValue.setParamKey(systemParamValue.getParamKey());
        sysParamValue.setDescription(systemParamValue.getDescription());
        return sysParamValue;
    }
}
