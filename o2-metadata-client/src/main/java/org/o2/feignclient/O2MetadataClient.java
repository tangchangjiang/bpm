package org.o2.feignclient;

import com.fasterxml.jackson.core.type.TypeReference;

import org.hzero.core.util.ResponseUtils;
import org.o2.feignclient.metadata.domain.co.CarrierCO;
import org.o2.feignclient.metadata.domain.dto.FreightDTO;
import org.o2.feignclient.metadata.domain.vo.FreightInfoCO;
import org.o2.feignclient.metadata.domain.vo.OnlineShopCO;
import org.o2.feignclient.metadata.domain.vo.SystemParameterCO;
import org.o2.feignclient.metadata.domain.vo.WarehouseCO;
import org.o2.feignclient.metadata.infra.feign.*;

import java.util.List;
import java.util.Map;

/**
 * @author lei.tang02@hand-china.com 2020/8/27
 */
public class O2MetadataClient {

    private final SysParameterRemoteService sysParameterRemoteService;
    private final WarehouseRemoteService warehouseRemoteService;
    private final FreightRemoteService freightRemoteService;
    private final OnlineShopRemoteService onlineShopRemoteService;
    private final CarrierRemoteService carrierRemoteService;

    public O2MetadataClient(SysParameterRemoteService sysParameterRemoteService,
                            WarehouseRemoteService warehouseRemoteService,
                            FreightRemoteService freightRemoteService,
                            OnlineShopRemoteService onlineShopRemoteService,
                            CarrierRemoteService carrierRemoteService) {
        this.sysParameterRemoteService = sysParameterRemoteService;
        this.warehouseRemoteService = warehouseRemoteService;
        this.freightRemoteService = freightRemoteService;
        this.onlineShopRemoteService = onlineShopRemoteService;
        this.carrierRemoteService = carrierRemoteService;
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
     * @return list
     */
    public Map<String, SystemParameterCO> listSystemParameters(List<String> paramCodes, Long tenantId) {
        return ResponseUtils.getResponse(sysParameterRemoteService.listSystemParameters(paramCodes, tenantId), new TypeReference<Map<String, SystemParameterCO>>() {
        });
    }
    /**
     * 从redis查询系统仓库
     *
     * @param warehouseCodes 仓库编码
     * @param tenantId 租户ID
     */
    public Map<String, WarehouseCO> listWarehouses(List<String> warehouseCodes, Long tenantId){
        return ResponseUtils.getResponse(warehouseRemoteService.listWarehouses(tenantId, warehouseCodes), new TypeReference<Map<String, WarehouseCO>>() {
        });
    }
    /**
     * 获取模版
     *
     * @param freight 运费参数
     * @return 运费结果
     */
    public FreightInfoCO getFreightTemplate(FreightDTO freight, Long tenantId){
        return ResponseUtils.getResponse(freightRemoteService.getFreightTemplate(freight,tenantId), FreightInfoCO.class);
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

    /**
     *
     * @date 2021-08-10
     * @param onlineShopCode  网店编码
     * @return 网店
     */
    public OnlineShopCO getOnlineShop(String onlineShopCode) {
       return ResponseUtils.getResponse(onlineShopRemoteService.getOnlineShop(onlineShopCode), OnlineShopCO.class);
    }

    /**
     * 查询承运商
     * @param organizationId 租户id
     * @return  list
     */
    public List<CarrierCO> listCarriers(final Long organizationId){
        return ResponseUtils.getResponse(carrierRemoteService.listCarriers(organizationId),  new TypeReference<List<CarrierCO>>() {
        });
    }
}
