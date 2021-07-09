package org.o2.feignclient;


import org.hzero.core.util.ResponseUtils;
import org.o2.feignclient.metadata.domain.vo.SystemParameterVO;
import org.o2.feignclient.metadata.infra.feign.SysParameterRemoteService;

import java.util.List;
import java.util.Map;


/**
 * @author lei.tang02@hand-china.com 2020/8/27
 */
public class O2MetadataManagementClient {

    private final SysParameterRemoteService sysParameterRemoteService;

    public O2MetadataManagementClient(SysParameterRemoteService sysParameterRemoteService) {
        this.sysParameterRemoteService = sysParameterRemoteService;
    }

    /**
     * 从redis查询系统参数
     *
     * @param paramCode 参数编码
     * @param tenantId 租户ID
     */
    public SystemParameterVO getSystemParameter(String paramCode, Long tenantId){
        return ResponseUtils.getResponse(sysParameterRemoteService.getSystemParameter(tenantId, paramCode), SystemParameterVO.class);
    }

    /**
     * 从redis查询系统参数
     *
     * @param paramCode 参数编码
     * @param tenantId 租户ID
     */
    public Map<String,SystemParameterVO> listSystemParameters(List<String> paramCode, Long tenantId){
        return ResponseUtils.getResponse(sysParameterRemoteService.listSystemParameters(tenantId, paramCode), Map.class);
    }

}
