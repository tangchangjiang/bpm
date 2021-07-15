package org.o2.metadata.app.service;


import org.o2.metadata.api.vo.SystemParameterVO;

import java.util.List;

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
    SystemParameterVO getSystemParameter(String paramCode, Long tenantId);
    /**
     *
     * @param
     * @return 
     */
    List<SystemParameterVO> listSystemParameters(List<String> paramCodes, Long organizationId);
}
