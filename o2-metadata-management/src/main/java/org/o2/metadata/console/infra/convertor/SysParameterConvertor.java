package org.o2.metadata.console.infra.convertor;

import com.sun.javafx.logging.PulseLogger;
import org.apache.commons.collections4.CollectionUtils;
import org.o2.metadata.console.api.vo.SystemParamValueVO;
import org.o2.metadata.console.api.vo.SystemParameterVO;
import org.o2.metadata.core.systemparameter.domain.SystemParamValueDO;
import org.o2.metadata.core.systemparameter.domain.SystemParameterDO;

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
public class SysParameterConvertor {
    /**
     * DO 转 VO
     * @param systemParameterDO 系统参数
     * @return  bean
     */
    public static SystemParameterVO doToVoObject(SystemParameterDO systemParameterDO){
        if (systemParameterDO == null) {
            return null;
        }
        SystemParameterVO systemParameterVO = new SystemParameterVO();
        systemParameterVO.setParamCode(systemParameterDO.getParamCode());
        systemParameterVO.setDefaultValue(systemParameterDO.getDefaultValue());
        Set<SystemParamValueDO> systemParamValueDO = systemParameterDO.getSetSystemParamValue();
        Set<SystemParamValueVO> systemParamValueVos = new HashSet<>();
        for (SystemParamValueDO valueDO : systemParamValueDO) {
            SystemParamValueVO valueVO = new SystemParamValueVO();
            valueVO.setParam1(valueDO.getParam1());
            valueVO.setParam2(valueDO.getParam2());
            valueVO.setParam3(valueDO.getParam3());
            valueVO.setParamValue(valueDO.getParamValue());
            systemParamValueVos.add(valueVO);
        }
        systemParameterVO.setSetSystemParamValue(systemParamValueVos);
        return systemParameterVO;
    }
    /**
     * DO 转 VO
     * @param systemParameterDOList 系统参数集合
     * @return  list
     */
    public static List<SystemParameterVO> doToVoListObjects(List<SystemParameterDO> systemParameterDOList) {
        List<SystemParameterVO> systemParameterVOList = new ArrayList<>();
        if (systemParameterDOList == null) {
            return systemParameterVOList;
        }
        for (SystemParameterDO systemParameterDO : systemParameterDOList) {
            systemParameterVOList.add(doToVoObject(systemParameterDO));
        }
        return systemParameterVOList;
    }
}
