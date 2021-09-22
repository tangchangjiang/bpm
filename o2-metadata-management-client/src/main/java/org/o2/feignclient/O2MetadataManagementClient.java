package org.o2.feignclient;


import com.fasterxml.jackson.core.type.TypeReference;
import io.choerodon.core.domain.Page;
import org.hzero.core.util.ResponseUtils;
import org.o2.feignclient.metadata.domain.co.*;
import org.o2.feignclient.metadata.domain.dto.*;
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
    private final FreightRemoteService freightRemoteService;
    private final StaticResourceRemoteService staticResourceRemoteService;
    private final CatalogVersionRemoteService catalogVersionRemoteService;
    private final CarrierRemoteService carrierRemoteService;
    private final PosRemoteService posRemoteService;
    private final PlatformRemoteService platformRemoteService;
    private final OnlineShopRemoteService onlineShopRemoteService;
    private final AddressMappingRemoteService addressMappingRemoteService;


    public O2MetadataManagementClient(SysParameterRemoteService sysParameterRemoteService,
                                      WarehouseRemoteService warehouseRemoteService,
                                      FreightRemoteService freightRemoteService,
                                      StaticResourceRemoteService staticResourceRemoteService, CatalogVersionRemoteService catalogVersionRemoteService,
                                      CarrierRemoteService carrierRemoteService,
                                      PosRemoteService posRemoteService, PlatformRemoteService platformRemoteService, OnlineShopRemoteService onlineShopRemoteService,
                                      AddressMappingRemoteService addressMappingRemoteService) {
        this.sysParameterRemoteService = sysParameterRemoteService;
        this.warehouseRemoteService = warehouseRemoteService;
        this.freightRemoteService = freightRemoteService;
        this.staticResourceRemoteService = staticResourceRemoteService;
        this.catalogVersionRemoteService = catalogVersionRemoteService;
        this.carrierRemoteService = carrierRemoteService;
        this.posRemoteService = posRemoteService;
        this.platformRemoteService = platformRemoteService;
        this.onlineShopRemoteService = onlineShopRemoteService;
        this.addressMappingRemoteService = addressMappingRemoteService;
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

    /**
     * 查询仓库
     *
     * @param innerDTO 入参
     * @param tenantId       租户ID
     */
    public Map<String, WarehouseCO> listWarehouses(WarehouseQueryInnerDTO innerDTO, Long tenantId) {
        return ResponseUtils.getResponse(warehouseRemoteService.listWarehouses(innerDTO, tenantId), new TypeReference<Map<String, WarehouseCO>>() {
        });
    }

    /**
     * 页面查询仓库
     *
     * @param innerDTO 入参
     * @param tenantId       租户ID
     */
    public Page<WarehouseCO> pageWarehouses(WarehousePageQueryInnerDTO innerDTO, Long tenantId) {
        return ResponseUtils.getResponse(warehouseRemoteService.pageWarehouses(tenantId, innerDTO), new TypeReference<Page<WarehouseCO>>() {
        });
    }
    /**
     * 从redis查询网店关联有效的仓库
     *
     * @param onlineShopCode 网店编码
     * @param tenantId        租户ID
     * @return map<warehouseCode, OnlineShopRelWarehouseCO>
     */
    public Map<String, OnlineShopRelWarehouseCO> listOnlineShopRelWarehouses(String onlineShopCode, Long tenantId) {
        return ResponseUtils.getResponse(onlineShopRemoteService.listOnlineShopRelWarehouses(onlineShopCode, tenantId), new TypeReference<Map<String, OnlineShopRelWarehouseCO>>() {
        });
    }

    /**
     * 获取模版
     *
     * @param freight 运费参数
     * @return 运费结果
     */
    public FreightInfoCO getFreightTemplate(FreightDTO freight, Long tenantId) {
        return ResponseUtils.getResponse(freightRemoteService.getFreightTemplate(freight, tenantId), FreightInfoCO.class);
    }

    /**
     * 获取默认模版
     *
     * @param tenantId 租户id
     * @return 模版
     */
    public FreightTemplateCO getDefaultTemplate(Long tenantId) {
        return ResponseUtils.getResponse(freightRemoteService.getDefaultTemplate( tenantId), FreightTemplateCO.class);
    }

    /**
     * 批量查询目录版本
     * @param catalogVersionQueryInnerDTO 目录版本集合
     * @param tenantId 租户ID
     * @return map key:编码 value:名称
     */
    public Map<String, String> listCatalogVersions(CatalogVersionQueryInnerDTO catalogVersionQueryInnerDTO, Long tenantId) {
        return ResponseUtils.getResponse(catalogVersionRemoteService.listCatalogVersions(catalogVersionQueryInnerDTO, tenantId), new TypeReference<Map<String, String>>() {
        });
    }

    /**
     * 批量查询承运商
     *
     * @param carrierQueryInnerDTO 承运商
     * @param tenantId   租户ID
     * @return map key:carrierCode
     */
    public Map<String, CarrierCO> listCarriers(CarrierQueryInnerDTO carrierQueryInnerDTO, Long tenantId) {
        return ResponseUtils.getResponse(carrierRemoteService.listCarriers(carrierQueryInnerDTO, tenantId), new TypeReference<Map<String, CarrierCO>>() {
        });
    }

    /**
     * 批量查询承运商匹配规则
     *
     * @param carrierMappingQueryInnerDTO 承运商
     * @param tenantId   租户ID
     * @return map key:carrierCode
     */
    public Map<String, CarrierMappingCO> listCarrierMappings(CarrierMappingQueryInnerDTO carrierMappingQueryInnerDTO, Long tenantId) {
        return ResponseUtils.getResponse(carrierRemoteService.listCarrierMappings(carrierMappingQueryInnerDTO, tenantId), new TypeReference<Map<String, CarrierMappingCO>>() {
        });
    }

    /**
     * 批量查询网店
     *
     * @param onlineShopQueryInnerDTO 网店
     * @return map 通过名称查询 key:onlineShopName ; 通过code查询 key:onlineShopCode
     */
    public Map<String, OnlineShopCO> listOnlineShops(OnlineShopQueryInnerDTO onlineShopQueryInnerDTO, Long tenantId) {
        return ResponseUtils.getResponse(onlineShopRemoteService.listOnlineShops(onlineShopQueryInnerDTO, tenantId), new TypeReference<Map<String, OnlineShopCO>>() {
        });
    }

    /**
     * 目录版本+ 目录 批量查询网店
     *
     * @param onlineShopCatalogVersionList 网店
     * @return map key:catalogCode-catalogVersionCode
     */
    public Map<String, List<OnlineShopCO>> listOnlineShops(List<OnlineShopCatalogVersionDTO> onlineShopCatalogVersionList, Long tenantId) {
        return ResponseUtils.getResponse(onlineShopRemoteService.listOnlineShops(onlineShopCatalogVersionList, tenantId), new TypeReference<Map<String,  List<OnlineShopCO>>>() {
        });
    }

    /**
     * 批量查询服务点地址
     * @param posAddressQueryInnerDTO 服务点地址
     * @param tenantId 租户ID
     * @return string
     */
    public Map<String, PosAddressCO> listPosAddress(PosAddressQueryInnerDTO posAddressQueryInnerDTO, Long tenantId) {
        return ResponseUtils.getResponse(posRemoteService.listPosAddress(posAddressQueryInnerDTO, tenantId), new TypeReference<Map<String, PosAddressCO>>() {
        });
    }

    /**
     * 批量查询地址匹配
     *
     * @param queryInnerDTO 地址匹配
     * @param tenantId   租户ID
     * @return map key:carrierCode
     */
    public Map<String, AddressMappingCO> listAddressMappings(AddressMappingQueryInnerDTO queryInnerDTO, Long tenantId) {
        return ResponseUtils.getResponse(addressMappingRemoteService.listAllAddressMappings(queryInnerDTO, tenantId), new TypeReference<Map<String, AddressMappingCO>>() {
        });
    }


    /**
     * 查询临近省
     * @param tenantId 租户ID
     * @return LIST
     */
    public List<NeighboringRegionCO> listNeighboringRegions(Long tenantId) {
        return ResponseUtils.getResponse(addressMappingRemoteService.listNeighboringRegions(tenantId), new TypeReference<List<NeighboringRegionCO>>() {
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
     * 仓库快递配送接单量增量更新
     *
     * @param organizationId 租户ID
     * @param warehouseCode  仓库编码
     * @param increment      快递配送接单量增量 1 成功 -1 失败
     */
    public Integer updateExpressValue(final Long organizationId, final String warehouseCode, final String increment) {
        return ResponseUtils.getResponse(warehouseRemoteService.updateExpressValue(organizationId, warehouseCode, increment),Integer.class);
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
     * 查询平台信息
     * @param tenantId 租户id
     * @param platformQueryInnerDTO 平台入参
     * @return key : platformCode（平台编码）
     */
    public Map<String, PlatformCO> listPlatforms(PlatformQueryInnerDTO platformQueryInnerDTO, Long tenantId) {
        return ResponseUtils.getResponse(platformRemoteService.listPlatforms(platformQueryInnerDTO,tenantId),new TypeReference<Map<String,PlatformCO>>(){});
    }

    /**
     * 获取静态资源配置
     * @param resourceCode 资源编码
     * @param tenantId 租户ID
     * @return StaticResourceConfigCO 配置
     */
    public StaticResourceConfigCO getStaticResourceConfig(String resourceCode,Long tenantId) {
        return ResponseUtils.getResponse(staticResourceRemoteService.getStaticResourceConfig(tenantId,resourceCode),StaticResourceConfigCO.class);
    }

    /**
     * 获取json_key 和 resource_url
     * @param tenantId 租户ID
     * @param staticResourceListDTO 查询条件
     * @return List<StaticResourceAndConfigCO> 结果
     */
    public List<StaticResourceAndConfigCO> getStaticResourceAndConfig(Long tenantId,StaticResourceListDTO staticResourceListDTO){
        return ResponseUtils.getResponse(staticResourceRemoteService.getStaticResourceAndConfig(tenantId, staticResourceListDTO), new TypeReference<List<StaticResourceAndConfigCO>>() {});
    }

    /**
     * 获取启用&支持站点校验的静态资源配置列表
     * @param tenantId 租户ID
     * @return 结果
     */
    public List<StaticResourceConfigCO> listStaticResourceConfig(Long tenantId) {
        return ResponseUtils.getResponse(staticResourceRemoteService.listStaticResourceConfig(tenantId), new TypeReference<List<StaticResourceConfigCO>>() {
        });
    }


}
