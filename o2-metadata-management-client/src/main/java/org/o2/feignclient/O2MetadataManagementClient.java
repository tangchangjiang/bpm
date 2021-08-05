package org.o2.feignclient;


import com.fasterxml.jackson.core.type.TypeReference;
import org.hzero.core.util.ResponseUtils;
import org.o2.feignclient.metadata.domain.dto.*;
import org.o2.feignclient.metadata.domain.vo.*;
import org.o2.feignclient.metadata.infra.feign.*;


import java.util.List;
import java.util.Map;
import java.util.Set;


/**
 * @author lei.tang02@hand-china.com 2020/8/27
 */
public class O2MetadataManagementClient {

    private final SysParameterRemoteService sysParameterRemoteService;
    private final WarehouseRemoteService warehouseRemoteService;
    private final OnlineShopRelWarehouseRemoteService onlineShopRelWarehouseRemoteService;
    private final FreightRemoteService freightRemoteService;
    private final StaticResourceRemoteService staticResourceRemoteService;
    private final CatalogVersionRemoteService catalogVersionRemoteService;
    private final CarrierRemoteService carrierRemoteService;
    private final PosRemoteService posRemoteService;
    private final PlatformInfMappingRemoteService platformInfMappingRemoteService;
    private final OnlineShopRemoteService onlineShopRemoteService;


    public O2MetadataManagementClient(SysParameterRemoteService sysParameterRemoteService,
                                      WarehouseRemoteService warehouseRemoteService,
                                      OnlineShopRelWarehouseRemoteService onlineShopRelWarehouseRemoteService,
                                      FreightRemoteService freightRemoteService,
                                      StaticResourceRemoteService staticResourceRemoteService, CatalogVersionRemoteService catalogVersionRemoteService,
                                      CarrierRemoteService carrierRemoteService,
                                      PosRemoteService posRemoteService, PlatformInfMappingRemoteService platformInfMappingRemoteService, OnlineShopRemoteService onlineShopRemoteService) {
        this.sysParameterRemoteService = sysParameterRemoteService;
        this.warehouseRemoteService = warehouseRemoteService;
        this.onlineShopRelWarehouseRemoteService = onlineShopRelWarehouseRemoteService;
        this.freightRemoteService = freightRemoteService;
        this.staticResourceRemoteService = staticResourceRemoteService;
        this.catalogVersionRemoteService = catalogVersionRemoteService;
        this.carrierRemoteService = carrierRemoteService;
        this.posRemoteService = posRemoteService;
        this.platformInfMappingRemoteService = platformInfMappingRemoteService;
        this.onlineShopRemoteService = onlineShopRemoteService;
    }

    /**
     * 从redis查询系统参数
     *
     * @param paramCode 参数编码
     * @param tenantId  租户ID
     */
    public SystemParameterVO getSystemParameter(String paramCode, Long tenantId) {
        return ResponseUtils.getResponse(sysParameterRemoteService.getSystemParameter(tenantId, paramCode), SystemParameterVO.class);
    }

    /**
     * 从redis查询仓库
     *
     * @param warehouseCodes 仓库编码
     * @param tenantId       租户ID
     */
    public Map<String, WarehouseVO> listWarehouses(List<String> warehouseCodes, Long tenantId) {
        return ResponseUtils.getResponse(warehouseRemoteService.listWarehouses(tenantId, warehouseCodes), new TypeReference<Map<String, WarehouseVO>>() {
        });
    }

    /**
     * 更新系统参数
     *
     * @param systemParameterDTO 系统
     * @param tenantId           租户ID
     */
    public ResponseVO updateSysParameter(SystemParameterDTO systemParameterDTO, Long tenantId) {
        return ResponseUtils.getResponse(sysParameterRemoteService.updateSysParameter(systemParameterDTO, tenantId), ResponseVO.class);
    }

    /**
     * 从redis查询系统参数
     *
     * @param paramCodes 参数编码
     * @param tenantId   租户ID
     * @return map key:paramCode
     */
    public Map<String, SystemParameterVO> listSystemParameters(List<String> paramCodes, Long tenantId) {
        return ResponseUtils.getResponse(sysParameterRemoteService.listSystemParameters(tenantId, paramCodes), new TypeReference<Map<String, SystemParameterVO>>() {
        });
    }

    /**
     * 从redis查询网店关联有效的仓库
     *
     * @param onlineShopeCode 网店编码
     * @param tenantId        租户ID
     * @return map<warehouseCode, OnlineShopRelWarehouseVO>
     */
    public Map<String, OnlineShopRelWarehouseVO> listOnlineShopRelWarehouses(String onlineShopeCode, Long tenantId) {
        return ResponseUtils.getResponse(onlineShopRelWarehouseRemoteService.listOnlineShopRelWarehouses(onlineShopeCode, tenantId), new TypeReference<Map<String, OnlineShopRelWarehouseVO>>() {
        });
    }

    /**
     * 获取模版
     *
     * @param freight 运费参数
     * @return 运费结果
     */
    public FreightInfoVO getFreightTemplate(FreightDTO freight, Long tenantId) {
        return ResponseUtils.getResponse(freightRemoteService.getFreightTemplate(freight, tenantId), FreightInfoVO.class);
    }

    /**
     * 查询有效仓库
     *
     * @param onlineShopCode 网店编码
     * @param tenantId       租户ID
     * @return 集合
     */
    public List<WarehouseVO> listActiveWarehouse(String onlineShopCode, Long tenantId) {
        return ResponseUtils.getResponse(warehouseRemoteService.listActiveWarehouse(onlineShopCode, tenantId), new TypeReference<List<WarehouseVO>>() {
        });
    }

    /**
     * 批量查询目录版本
     * @param catalogVersionDTO 目录版本集合
     * @param tenantId 租户ID
     * @return map key:编码 value:名称
     */
    public Map<String, String> listCatalogVersions(CatalogVersionDTO catalogVersionDTO, Long tenantId) {
        return ResponseUtils.getResponse(catalogVersionRemoteService.listCatalogVersions(catalogVersionDTO, tenantId), new TypeReference<Map<String, String>>() {
        });
    }

    /**
     * 批量查询承运商
     *
     * @param carrierDTO 承运商
     * @param tenantId   租户ID
     * @return map key:carrierCode
     */
    public Map<String, CarrierVO> listCarriers(CarrierDTO carrierDTO, Long tenantId) {
        return ResponseUtils.getResponse(carrierRemoteService.listCarriers(carrierDTO, tenantId), new TypeReference<Map<String, CarrierVO>>() {
        });
    }

    /**
     * 批量查询网店
     *
     * @param onlineShopDTO 网店
     * @return map 通过名称查询 key:onlineShopName ; 通过code查询 key:onlineShopCode
     */
    public Map<String, OnlineShopVO> listOnlineShops(OnlineShopDTO onlineShopDTO, Long tenantId) {
        return ResponseUtils.getResponse(onlineShopRelWarehouseRemoteService.listOnlineShops(onlineShopDTO, tenantId), new TypeReference<Map<String, OnlineShopVO>>() {
        });
    }

    /**
     * 批量查询服务点地址
     * @param posAddressDTO 服务点地址
     * @param tenantId 租户ID
     * @return string
     */
    public Map<String,PosAddressVO> listPosAddress(PosAddressDTO posAddressDTO,Long tenantId) {
        return ResponseUtils.getResponse(posRemoteService.listPosAddress(posAddressDTO, tenantId), new TypeReference<Map<String, PosAddressVO>>() {
        });
    }


    /**
     * 获取快递配送接单量到达上限的仓库
     *
     * @param organizationId 租户id
     * @return 快递配送接单量到达上限的仓库集合
     */
    public Set<String> expressLimitWarehouseCollection(final Long organizationId) {
        return ResponseUtils.getResponse(warehouseRemoteService.expressLimitWarehouseCollection(organizationId), new TypeReference<Set<String>>() {
        });
    }

    /**
     * 获取自提接单量到达上限的仓库
     *
     * @param organizationId 租户id
     * @return 自提接单量到达上限的仓库集合
     */
    public Set<String> pickUpLimitWarehouseCollection(final Long organizationId) {
        return ResponseUtils.getResponse(warehouseRemoteService.pickUpLimitWarehouseCollection(organizationId), new TypeReference<Set<String>>() {
        });
    }

    /**
     * 重置仓库快递配送接单量值
     *
     * @param organizationId 租户ID
     * @param warehouseCode  仓库编码
     */
    public Boolean resetWarehouseExpressLimit(final Long organizationId, final String warehouseCode) {
        return ResponseUtils.isFailed(warehouseRemoteService.resetWarehouseExpressLimit(organizationId, warehouseCode));
    }

    /**
     * 重置仓库自提接单量限制值
     *
     * @param organizationId 租户ID
     * @param warehouseCode  仓库编码
     */
    public Boolean resetWarehousePickUpLimit(final Long organizationId, final String warehouseCode) {
        return ResponseUtils.isFailed(warehouseRemoteService.resetWarehousePickUpLimit(organizationId, warehouseCode));
    }

    /**
     * 查询静态资源文件code&url映射
     *
     * @param staticResourceQueryDTO staticResourceQueryDTO
     * @return code&url映射
     */
    public Map<String, String> queryResourceCodeUrlMap(final Long organizationId, StaticResourceQueryDTO staticResourceQueryDTO) {
        return ResponseUtils.getResponse(staticResourceRemoteService.queryResourceCodeUrlMap(organizationId, staticResourceQueryDTO), new TypeReference<Map<String, String>>() {
        });
    }

    /**
     * 保存静态资源文件表
     *
     * @param staticResourceSaveDTOList staticResourceSaveDTOList
     */
    public Boolean saveResource(final Long organizationId, List<StaticResourceSaveDTO> staticResourceSaveDTOList) {
        return ResponseUtils.getResponse(staticResourceRemoteService.saveResource(organizationId, staticResourceSaveDTOList), Boolean.class);
    }

    /**
     * 查询平台信息匹配
     * @param organizationId 租户id
     * @param platformInfMapping 参数
     * @return PlatformInfMappingVO 结果
     */
    public List<PlatformInfMappingVO> getPlatformInfMapping(Long organizationId,
                                                      List<PlatformInfMappingDTO> platformInfMapping) {
        return ResponseUtils.getResponse(platformInfMappingRemoteService.getPlatformMapping(organizationId,platformInfMapping),new TypeReference<List<PlatformInfMappingVO>>(){});
    }

    /**
     * 根据平台code和网店名称查询网店code
     * @param organizationId 租户ID
     * @param platformCode 平台code
     * @param shopName 网店
     * @return List<OnlineShopVO> 结果
     */
   public List<OnlineShopVO> getOnlineShopCode(Long organizationId,String platformCode,String shopName) {
        return ResponseUtils.getResponse(onlineShopRemoteService.getOnlineShopCode(organizationId, platformCode, shopName), new TypeReference<List<OnlineShopVO>>() {});
   }

}
