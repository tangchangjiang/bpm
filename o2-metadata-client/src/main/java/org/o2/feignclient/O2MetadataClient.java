package org.o2.feignclient;

import com.fasterxml.jackson.core.type.TypeReference;

import org.hzero.core.util.ResponseUtils;
import org.o2.feignclient.metadata.domain.vo.SystemParameterVO;
import org.o2.feignclient.metadata.infra.feign.SysParameterRemoteService;
import org.o2.feignclient.metadata.infra.feign.WarehouseRemoteService;

import java.util.Map;
import java.util.Set;

/**
 * @author lei.tang02@hand-china.com 2020/8/27
 */
public class O2MetadataClient {

    private final SysParameterRemoteService sysParameterRemoteService;
    private final WarehouseRemoteService warehouseRemoteService;

    public O2MetadataClient(SysParameterRemoteService sysParameterRemoteService, WarehouseRemoteService warehouseRemoteService) {
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
     * 保存
     *
     * @param hashMap        map
     * @param warehouseCode  仓库编码
     * @param organizationId 租户Id
     */
    public Boolean saveWarehouse(final Long organizationId, final String warehouseCode, final Map<String, Object> hashMap) {
        return ResponseUtils.isFailed(warehouseRemoteService.saveWarehouse(organizationId, warehouseCode, hashMap));
    }


    /**
     * 更新
     *
     * @param warehouseCode
     * @param hashMap
     * @param organizationId
     */
    public Boolean updateWarehouse(final Long organizationId, final String warehouseCode, final Map<String, Object> hashMap) {
        return ResponseUtils.isFailed(warehouseRemoteService.updateWarehouse(organizationId, warehouseCode, hashMap));
    }

    /**
     * 保存仓库快递配送接单量限制
     *
     * @param organizationId  租户ID
     * @param warehouseCode   仓库编码
     * @param expressQuantity 快递配送接单量限制
     */
    public Boolean saveExpressQuantity(final Long organizationId, final String warehouseCode, final String expressQuantity) {
        return ResponseUtils.isFailed(warehouseRemoteService.saveExpressQuantity(organizationId, warehouseCode, expressQuantity));
    }

    /**
     * 保存仓库自提接单量限制
     *
     * @param organizationId 租户ID
     * @param warehouseCode  仓库编码
     * @param pickUpQuantity 自提单量限制
     */
    public Boolean savePickUpQuantity(final Long organizationId, final String warehouseCode, final String pickUpQuantity) {
        return ResponseUtils.isFailed(warehouseRemoteService.savePickUpQuantity(organizationId, warehouseCode, pickUpQuantity));
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
     * 仓库自提接单量增量更新
     *
     * @param organizationId 租户ID
     * @param warehouseCode  仓库编码
     * @param increment      自提单量增量
     */
    public Boolean updatePickUpValue(final Long organizationId, final String warehouseCode, final String increment) {
        return ResponseUtils.isFailed(warehouseRemoteService.updatePickUpValue(organizationId, warehouseCode, increment));
    }

    /**
     * 获取快递配送接单量限制
     *
     * @param organizationId 租户ID
     * @param warehouseCode  仓库编码
     * @return 快递配送接单量限制
     */
    public String getExpressLimit(final Long organizationId, final String warehouseCode) {
        return ResponseUtils.getResponse(warehouseRemoteService.getExpressLimit(organizationId, warehouseCode), String.class);
    }

    /**
     * 获取自提接单量限制
     *
     * @param organizationId 租户ID
     * @param warehouseCode  仓库编码
     * @return 自提接单量限制
     */
    public String getPickUpLimit(final Long organizationId, final String warehouseCode) {
        return ResponseUtils.getResponse(warehouseRemoteService.getPickUpLimit(organizationId, warehouseCode), String.class);
    }

    /**
     * 获取实际快递配送接单量
     *
     * @param organizationId 租户ID
     * @param warehouseCode  仓库编码
     * @return 实际快递配送接单量
     */
    public String getExpressValue(final Long organizationId, final String warehouseCode) {
        return ResponseUtils.getResponse(warehouseRemoteService.getExpressValue(organizationId, warehouseCode), String.class);
    }

    /**
     * 获取实际自提接单量
     *
     * @param organizationId 租户ID
     * @param warehouseCode  仓库编码
     * @return 实际自提接单量
     */
    public String getPickUpValue(final Long organizationId, final String warehouseCode) {
        return ResponseUtils.getResponse(warehouseRemoteService.getPickUpValue(organizationId, warehouseCode), String.class);
    }

    /**
     * 仓库缓存KEY
     *
     * @param organizationId 租户ID
     * @param warehouseCode  仓库编码
     * @return 仓库缓存KEY
     */
    public String warehouseCacheKey(final Long organizationId, final String warehouseCode) {
        return ResponseUtils.getResponse(warehouseRemoteService.warehouseCacheKey(organizationId, warehouseCode), String.class);
    }

    /**
     * 仓库limit缓存KEY
     *
     * @param organizationId 租户ID
     * @param limit          limit
     * @return 仓库limit缓存KEY
     */
    public String warehouseLimitCacheKey(final Long organizationId, final String limit) {
        return ResponseUtils.getResponse(warehouseRemoteService.warehouseLimitCacheKey(organizationId, limit), String.class);
    }

    /**
     * 是否仓库快递配送接单量到达上限
     *
     * @param organizationId 租户ID
     * @param warehouseCode  仓库编码
     * @return 结果(true : 到达上限)
     */
    public Boolean isWarehouseExpressLimit(final Long organizationId, final String warehouseCode) {
        return ResponseUtils.isFailed(warehouseRemoteService.isWarehouseExpressLimit(organizationId, warehouseCode));
    }

    /**
     * 是否仓库自提接单量到达上限
     *
     * @param organizationId 租户ID
     * @param warehouseCode  仓库编码
     * @return 结果(true : 到达上限)
     */
    public Boolean isWarehousePickUpLimit(final Long organizationId, final String warehouseCode) {
        return ResponseUtils.isFailed(warehouseRemoteService.isWarehousePickUpLimit(organizationId, warehouseCode));
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
}
