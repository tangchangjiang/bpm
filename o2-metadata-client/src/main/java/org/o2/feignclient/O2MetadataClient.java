package org.o2.feignclient;

import com.fasterxml.jackson.core.type.TypeReference;

import org.hzero.core.util.ResponseUtils;
import org.o2.feignclient.metadata.domain.dto.FreightDTO;
import org.o2.feignclient.metadata.domain.vo.FreightInfoVO;
import org.o2.feignclient.metadata.domain.vo.SystemParameterVO;
import org.o2.feignclient.metadata.domain.vo.WarehouseVO;
import org.o2.feignclient.metadata.infra.feign.FreightRemoteService;
import org.o2.feignclient.metadata.infra.feign.SysParameterRemoteService;
import org.o2.feignclient.metadata.infra.feign.WarehouseRemoteService;

import java.util.List;
import java.util.Map;

/**
 * @author lei.tang02@hand-china.com 2020/8/27
 */
public class O2MetadataClient {

    private final SysParameterRemoteService sysParameterRemoteService;
    private final WarehouseRemoteService warehouseRemoteService;
    private final FreightRemoteService freightRemoteService;

    public O2MetadataClient(SysParameterRemoteService sysParameterRemoteService,
                            WarehouseRemoteService warehouseRemoteService,
                            FreightRemoteService freightRemoteService) {
        this.sysParameterRemoteService = sysParameterRemoteService;
        this.warehouseRemoteService = warehouseRemoteService;
        this.freightRemoteService = freightRemoteService;
    }

    /**
     * 从redis查询系统参数
     *
     * @param paramCode 参数编码
     * @param tenantId 租户ID
     */
    public SystemParameterVO getSystemParameter(String paramCode, Long tenantId) {
        return ResponseUtils.getResponse(sysParameterRemoteService.getSystemParameter(tenantId, paramCode), SystemParameterVO.class);
    }

    /**
     * 批量从redis查询系统参数
     * @param  paramCodes 参数编码
     * @return list
     */
    public Map<String, SystemParameterVO> listSystemParameters(List<String> paramCodes, Long tenantId) {
        return ResponseUtils.getResponse(sysParameterRemoteService.listSystemParameters(paramCodes, tenantId), new TypeReference<Map<String, SystemParameterVO>>() {
        });
    }
    /**
     * 从redis查询系统仓库
     *
     * @param warehouseCodes 仓库编码
     * @param tenantId 租户ID
     */
    public Map<String,WarehouseVO> listWarehouses(List<String> warehouseCodes, Long tenantId){
        return ResponseUtils.getResponse(warehouseRemoteService.listWarehouses(tenantId, warehouseCodes), new TypeReference<Map<String, WarehouseVO>>() {
        });
    }
    /**
     * 获取模版
     *
     * @param freight 运费参数
     * @return 运费结果
     */
    public FreightInfoVO getFreightTemplate(FreightDTO freight, Long tenantId){
        return ResponseUtils.getResponse(freightRemoteService.getFreightTemplate(freight,tenantId), FreightInfoVO.class);
    }

    /**
     * 仓库快递配送接单量增量更新
     *
     * @param organizationId 租户ID
     * @param warehouseCode  仓库编码
     * @param increment      快递配送接单量增量
     */
    public Boolean updateExpressValue(final Long organizationId, final String warehouseCode, final String increment) {
        return ResponseUtils.isFailed(warehouseRemoteService.updateExpressValue(organizationId, warehouseCode, increment));
    }
}
