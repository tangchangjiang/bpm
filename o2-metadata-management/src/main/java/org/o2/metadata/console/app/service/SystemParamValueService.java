package org.o2.metadata.console.app.service;


import org.o2.metadata.console.infra.entity.SystemParamValue;

import java.util.List;

/**
 * 系统参数值应用服务
 *
 * @author hongyun.wang01@hand-china.com 2020-02-13
 */
public interface SystemParamValueService {
    /**
     * 获取参数类型为String的参数值
     *
     * @param paramCode 参数code
     * @param tenantId  租户id
     * @return StringList<String>
     */
    String getSysValueByParam(String paramCode, Long tenantId);

    /**
     * 获取参数类型为list的参数值
     *
     * @param paramCode 参数code
     * @param tenantId  租户id
     * @return List<String>
     */
    List<String> getSysListByParam(String paramCode, Long tenantId);


    /**
     * 新建参数值
     * @param  systemParamValue 参数值
     * @return
     */
    void saveSystemParamValue(SystemParamValue systemParamValue);

    /**
     * 更新参数值
     * @param  systemParamValue 参数值
     */
    void updateSystemParamValue(SystemParamValue systemParamValue);

    /**
     * 删除参数值
     * @param  systemParamValue 参数值
     */
    void removeSystemParamValue(SystemParamValue systemParamValue);
    
    /**
     * 校验值
     * @param systemParamValue 参数值
     */
    void  systemParamValueValidate(SystemParamValue systemParamValue);

}
