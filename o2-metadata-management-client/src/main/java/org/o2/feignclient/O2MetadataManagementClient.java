package org.o2.feignclient;


import com.fasterxml.jackson.core.type.TypeReference;
import org.hzero.core.util.ResponseUtils;
import org.o2.feignclient.metadata.domain.vo.SystemParameterVO;
import org.o2.feignclient.metadata.domain.vo.WarehouseVO;
import org.o2.feignclient.metadata.infra.feign.SysParameterRemoteService;
import org.o2.feignclient.metadata.infra.feign.WarehouseRemoteService;

import java.util.List;
import java.util.Map;


/**
 * @author lei.tang02@hand-china.com 2020/8/27
 */
public class O2MetadataManagementClient {

    private final SysParameterRemoteService sysParameterRemoteService;
    private final WarehouseRemoteService warehouseRemoteService;

    public O2MetadataManagementClient(SysParameterRemoteService sysParameterRemoteService,
                                      WarehouseRemoteService warehouseRemoteService) {
        this.sysParameterRemoteService = sysParameterRemoteService;
        this.warehouseRemoteService = warehouseRemoteService;
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
     * 从redis查询系统仓库
     *
     * @param warehouseCode 仓库编码
     * @param tenantId 租户ID
     */
    public WarehouseVO getWarehouse(String warehouseCode, Long tenantId){
        return ResponseUtils.getResponse(warehouseRemoteService.getWarehouse(tenantId, warehouseCode), WarehouseVO.class);
    }

    /**
     * 从redis查询系统参数
     *  @param paramCodes 参数编码
     * @param tenantId 租户ID
     * @return map
     */
    public Map<String, SystemParameterVO> listSystemParameters(List<String> paramCodes, Long tenantId) {
        return ResponseUtils.getResponse(sysParameterRemoteService.listSystemParameters(tenantId, paramCodes), new TypeReference<Map<String, SystemParameterVO>>() {
        });
    }

}
