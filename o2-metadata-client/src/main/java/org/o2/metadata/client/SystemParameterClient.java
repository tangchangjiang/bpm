package org.o2.metadata.client;

import com.fasterxml.jackson.core.type.TypeReference;
import org.hzero.core.util.ResponseUtils;
import org.o2.metadata.client.domain.co.SystemParameterCO;
import org.o2.metadata.client.infra.feign.SysParameterRemoteService;

import java.util.List;
import java.util.Map;

/**
 *
 * 系统参数
 *
 * @author yipeng.zhu@hand-china.com 2021-11-15
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
     * @param tenantId 租户ID
     */
    public SystemParameterCO getSystemParameter(String paramCode, Long tenantId) {
        return ResponseUtils.getResponse(sysParameterRemoteService.getSystemParameter(tenantId, paramCode), SystemParameterCO.class);
    }

    /**
     * 批量从redis查询系统参数
     * @param  paramCodes 参数编码
     * @return list key:参数编码 value:系统参数
     */
    public Map<String, SystemParameterCO> listSystemParameters(List<String> paramCodes, Long tenantId) {
        return ResponseUtils.getResponse(sysParameterRemoteService.listSystemParameters(paramCodes, tenantId), new TypeReference<Map<String, SystemParameterCO>>() {
        });
    }
}
