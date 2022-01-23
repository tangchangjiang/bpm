package org.o2.metadata.console.app.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.hzero.mybatis.domian.Condition;
import org.hzero.mybatis.util.Sqls;
import org.o2.metadata.console.app.service.WarehouseTenantInitService;
import org.o2.metadata.console.infra.entity.Warehouse;
import org.o2.metadata.console.infra.repository.WarehouseRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.List;

/**
 * 仓库租户初始化
 *
 * @author zhanpeng.jiang@hand-china.com
 * @date 2022/01/23 19:53
 */
@Slf4j
@Service
public class WarehouseTenantInitServiceImpl implements WarehouseTenantInitService {

    /**
     * 虚拟仓编码
     */
    private static final String VIRTUAL_WAREHOUSE = "VIRTUAL_POS";
    private final WarehouseRepository warehouseRepository;

    public WarehouseTenantInitServiceImpl(WarehouseRepository warehouseRepository) {
        this.warehouseRepository = warehouseRepository;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void tenantInitialize(long sourceTenantId, Long targetTenantId) {
        log.info("initializeWarehouse start");
        // 1. 查询源租户
        final List<Warehouse> sourceWarehouses = warehouseRepository.selectByCondition(Condition.builder(Warehouse.class)
                .andWhere(Sqls.custom()
                        .andEqualTo(Warehouse.FIELD_TENANT_ID, sourceTenantId)
                        .andEqualTo(Warehouse.FIELD_WAREHOUSE_CODE, VIRTUAL_WAREHOUSE)
                ).build());

        if (CollectionUtils.isEmpty(sourceWarehouses)) {
            log.warn("VIRTUAL_POS not exists in sourceTenantId[{}]", sourceTenantId);
            return;
        }

        // 2. 查询目标租户是否存在数据
        final List<Warehouse> targetWarehouses = warehouseRepository.selectByCondition(Condition.builder(Warehouse.class)
                .andWhere(Sqls.custom()
                        .andEqualTo(Warehouse.FIELD_TENANT_ID, targetTenantId)
                        .andEqualTo(Warehouse.FIELD_WAREHOUSE_CODE, VIRTUAL_WAREHOUSE)
                ).build());

        // 3. 删除目标租户数据&缓存
        if (CollectionUtils.isNotEmpty(targetWarehouses)) {
            warehouseRepository.batchDeleteByPrimaryKey(targetWarehouses);
        }

        // 4. 插入源租户数据到目标租户
        final Warehouse virtualWarehouse = sourceWarehouses.get(0);
        virtualWarehouse.setTenantId(targetTenantId);
        virtualWarehouse.setWarehouseId(null);
        warehouseRepository.insert(virtualWarehouse);
    }
}
