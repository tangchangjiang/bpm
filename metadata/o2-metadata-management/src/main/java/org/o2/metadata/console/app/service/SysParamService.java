package org.o2.metadata.console.app.service;

import org.o2.core.response.BatchOperateResponse;
import org.o2.metadata.console.api.co.ResponseCO;
import org.o2.metadata.console.api.co.SystemParameterCO;
import org.o2.metadata.console.api.dto.SystemParameterQueryInnerDTO;
import org.o2.metadata.console.infra.entity.SystemParameter;

import java.util.List;

/**
 * @author yong.nie@hand-china.com
 * @date 2020/6/30 21:26
 **/

public interface SysParamService {

    /**
     * 从redis查询系统参数
     *
     * @param paramCode 参数编码
     * @param tenantId  租户ID
     * @return SystemParamDetailVO
     */
    SystemParameterCO getSystemParameter(String paramCode, Long tenantId);

    /**
     * 批量从redis查询系统参数
     *
     * @param paramCodes 参数编码集合
     * @param tenantId   租户ID
     * @return list
     */
    List<SystemParameterCO> listSystemParameters(List<String> paramCodes, Long tenantId);

    /**
     * 新建系统参数
     *
     * @param systemParameter 系统参数
     * @param tenantId        租户ID
     */
    void saveSystemParameter(SystemParameter systemParameter, Long tenantId);

    /**
     * 更新系统参数
     *
     * @param systemParameter 系统参数
     * @param tenantId        租户Id
     */
    void updateSystemParameter(SystemParameter systemParameter, Long tenantId);

    /**
     * 更新系统参数(map类型）
     *
     * @param systemParameterQueryInnerDTO 系统参数
     * @param tenantId                     租户ID
     * @return vo
     */
    ResponseCO updateSysParameter(SystemParameterQueryInnerDTO systemParameterQueryInnerDTO, Long tenantId);

    /**
     * 复制系统参数
     *
     * @param paramId  系统参数Id
     * @param tenantId 需要复制到的租户Id
     */
    void copySysParam(Long paramId, Long tenantId);

    /**
     * 平台层复制系统参数
     *
     * @param paramId   系统参数Id
     * @param tenantIds 需要复制到的租户Id
     * @return 结果
     */
    BatchOperateResponse copySysParamOfSite(Long paramId, List<Long> tenantIds);
}
