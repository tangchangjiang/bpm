package org.o2.metadata.console.infra.repository;

import java.util.List;

/**
 * 跨schema查询
 *
 * @author yong.nie@hand-china.com
 * @date 2020/3/30 10:50
 **/

public interface AcrossSchemaRepository {

    /**
     * 查询仓库关联sku
     *
     * @param warehouseCode 仓库编码
     * @param tenantId 租户ID
     * @return List sku编码
     **/
    List<String> selectSkuByWarehouse(String warehouseCode, Long tenantId);

}
