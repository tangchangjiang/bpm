package org.o2.metadata.console.app.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.hzero.mybatis.domian.Condition;
import org.hzero.mybatis.util.Sqls;
import org.o2.metadata.console.app.service.PosService;
import org.o2.metadata.console.app.service.WarehouseService;
import org.o2.metadata.console.app.service.WarehouseTenantInitService;
import org.o2.metadata.console.infra.constant.TenantInitConstants;
import org.o2.metadata.console.infra.entity.Pos;
import org.o2.metadata.console.infra.entity.Warehouse;
import org.o2.metadata.console.infra.redis.WarehouseRedis;
import org.o2.metadata.console.infra.repository.WarehouseRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    private final WarehouseService warehouseService;
    private final WarehouseRedis warehouseRedis;
    private final PosService posService;

    public WarehouseTenantInitServiceImpl(WarehouseRepository warehouseRepository,
                                          WarehouseService warehouseService,
                                          WarehouseRedis warehouseRedis,
                                          PosService posService) {
        this.warehouseRepository = warehouseRepository;
        this.warehouseService = warehouseService;
        this.warehouseRedis = warehouseRedis;
        this.posService = posService;
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
        handleData(targetWarehouses,sourceWarehouses,targetTenantId);
        log.info("initializeWarehouse finish");
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void tenantInitializeBusiness(long sourceTenantId, Long targetTenantId) {
        log.info("Business: initializeWarehouse start");
        // 1. 查询源租户
        Warehouse query = new Warehouse();
        query.setTenantId(sourceTenantId);
        query.setWarehouseCodes(TenantInitConstants.InitWarehouseBusiness.warehouses);
        List<Warehouse> sourceWarehouses = warehouseService.selectByCondition(query);
        if (CollectionUtils.isEmpty(sourceWarehouses)) {
            log.warn("Business data not exists in sourceTenantId[{}]", sourceTenantId);
            return;
        }
        // 2. 查询目标租户是否存在数据
        query.setTenantId(targetTenantId);
        List<Warehouse> oldWarehouses = warehouseService.selectByCondition(query);
        handleData(oldWarehouses,sourceWarehouses,targetTenantId);
        log.info("Business: initializeWarehouse finish");
    }

    /**
     *  更新目标库已存在的数据 插入需要初始化的数据
     * @param oldWarehouses  目标库已存在的数据
     * @param initializeWarehouses 目标库需要重新初始化的数据
     * @param targetTenantId  目标租户ID
     */
    private void handleData(List<Warehouse> oldWarehouses, List<Warehouse> initializeWarehouses, Long targetTenantId) {
        // 2.1 查询目标租户需要更新数据
        List<Warehouse> addList = new ArrayList<>(4);
        // 2.1 查询目标租户需要插入数据
        List<Warehouse> updateList = new ArrayList<>(4);
        // 获取源服务点编码
        List<String> posCodes = new ArrayList<>(initializeWarehouses.size());
        for (Warehouse warehouse : initializeWarehouses){
            posCodes.add(warehouse.getPosCode());
        }
        // 获目标租户服务点
        Pos query = new Pos();
        query.setTenantId(targetTenantId);
        query.setPosCodes(posCodes);
        List<Pos> targetPosList = posService.selectByCondition(query);
        // 目标租户服务点 编码和id 对应关系
        Map<String,Long> targetPosMap = new HashMap<>(targetPosList.size());
        for (Pos pos : targetPosList) {
            targetPosMap.put(pos.getPosCode(), pos.getPosId());
        }
        // 仓库编码
        List<String> warehouseCodeList = new ArrayList<>(4);
        for (Warehouse init : initializeWarehouses) {
            String initCode = init.getWarehouseCode();
            Long posId = targetPosMap.get(init.getPosCode());
            init.setPosId(posId);
            warehouseCodeList.add(initCode);
            boolean addFlag = true;
            if (oldWarehouses.isEmpty()) {
                addList.add(init);
                continue;
            }
            for (Warehouse old : oldWarehouses) {
                String oldCode = old.getWarehouseCode();
                if (initCode.equals(oldCode)) {
                    init.setTenantId(targetTenantId);
                    init.setObjectVersionNumber(old.getObjectVersionNumber());
                    init.setWarehouseId(old.getWarehouseId());
                    updateList.add(init);
                    addFlag = false;
                    break;
                }
            }
            if (addFlag) {
                addList.add(init);
            }
        }

        addList.forEach(warehouse -> {
            warehouse.setWarehouseId(null);
            warehouse.setTenantId(targetTenantId);
        });
        // 3. 更新目标库
        warehouseRepository.batchUpdateByPrimaryKey(updateList);
        warehouseRepository.batchInsert(addList);

        // 4. 更新目标数据缓存
        warehouseRedis.batchUpdateWarehouse(warehouseCodeList,targetTenantId);
    }

}
