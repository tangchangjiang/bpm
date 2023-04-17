package org.o2.metadata.management.client;

import com.fasterxml.jackson.core.type.TypeReference;
import org.hzero.core.util.ResponseUtils;
import org.o2.metadata.management.client.domain.co.ResponseCO;
import org.o2.metadata.management.client.domain.co.SystemParameterCO;
import org.o2.metadata.management.client.domain.dto.SystemParameterQueryInnerDTO;
import org.o2.metadata.management.client.infra.feign.SysParameterRemoteService;

import java.util.List;
import java.util.Map;

/**
 *
 * 系统参数
 *
 * @author yipeng.zhu@hand-china.com 2021-09-23
 **/
public class SystemParameterClient {

    private final SysParameterRemoteService sysParameterRemoteService;

    public SystemParameterClient(SysParameterRemoteService sysParameterRemoteService) {
        this.sysParameterRemoteService = sysParameterRemoteService;
    }

    /**
     * 从redis查询系统参数
     *
     * @param paramCode 参数编码
     * @param tenantId  租户ID
     */
    public SystemParameterCO getSystemParameter(String paramCode, Long tenantId) {
        return ResponseUtils.getResponse(sysParameterRemoteService.getSystemParameter(tenantId, paramCode), SystemParameterCO.class);
    }


    /**
     * 从redis查询系统参数-SIZE
     *
     * @param paramCode 参数编码
     * @param tenantId  租户ID
     */
    public SystemParameterCO getSizeSystemParameter(String paramCode, Long tenantId) {
        return ResponseUtils.getResponse(sysParameterRemoteService.getSizeSystemParameter(tenantId, paramCode), SystemParameterCO.class);
    }


    /**
     * 从redis查询系统参数
     *
     * @param paramCodes 参数编码
     * @param tenantId   租户ID
     * @return map key:paramCode
     */
    public Map<String, SystemParameterCO> listSystemParameters(List<String> paramCodes, Long tenantId) {
        return ResponseUtils.getResponse(sysParameterRemoteService.listSystemParameters(tenantId, paramCodes), new TypeReference<Map<String, SystemParameterCO>>() {
        });
    }

    /**
     * 更新系统参数
     *
     * @param systemParameterQueryInnerDTO 系统
     * @param tenantId           租户ID
     */
    public ResponseCO updateSysParameter(SystemParameterQueryInnerDTO systemParameterQueryInnerDTO, Long tenantId) {
        return ResponseUtils.getResponse(sysParameterRemoteService.updateSysParameter(systemParameterQueryInnerDTO, tenantId), ResponseCO.class);
    }
}
