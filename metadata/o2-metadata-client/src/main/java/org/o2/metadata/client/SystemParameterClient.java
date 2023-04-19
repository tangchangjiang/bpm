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

    /**
     * 从Redis查询系统参数（多租户）
     *
     * @param paramCodeMap 参数编码Map tenantId:paramCode
     * @return 系统参数map  tenantId:SystemParameterCO
     */
    public Map<Long, SystemParameterCO> getSysParamBatchTenant(Map<Long, String> paramCodeMap) {
        return ResponseUtils.getResponse(sysParameterRemoteService.getSysParamBatchTenant(paramCodeMap), new TypeReference<Map<Long, SystemParameterCO>>() {
        });
    }

    /**
     * 批量从redis查询系统参数(多租户)
     *
     * @param paramCodesMap 参数编码map tenantId:paramCodes
     * @return 参数信息map tenantId:paramCode:SystemParameterCO
     */
    public Map<Long, Map<String, SystemParameterCO>> listSysParamBatchTenant(Map<Long, List<String>> paramCodesMap) {
        return ResponseUtils.getResponse(sysParameterRemoteService.listSysParamBatchTenant(paramCodesMap), new TypeReference<Map<Long, Map<String, SystemParameterCO>>>() {
        });
    }
}
