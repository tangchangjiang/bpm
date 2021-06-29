package org.o2.metadata.app.service;


import org.o2.metadata.api.vo.SystemParamDetailVO;

/**
 * 系统参数缓存
 *
 * @author mark.bao@hand-china.com 2019-05-30
 */
public interface SysParameterService {


    /**
     * 从redis查询系统参数
     *
     * @param paramCode 参数编码
     * @param tenantId 租户ID
     * @return SystemParamDetailVO
     */
    SystemParamDetailVO listSystemParameter(String paramCode, Long tenantId);

}
