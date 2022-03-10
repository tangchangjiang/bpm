package org.o2.metadata.console.app.service.impl;

import jodd.util.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.hzero.mybatis.domian.Condition;
import org.hzero.mybatis.util.Sqls;
import org.o2.initialize.domain.context.TenantInitContext;
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

import java.util.*;

/**
 * 仓库租户初始化
 *
 * @author zhanpeng.jiang@hand-china.com
 * @date 2022/01/23 19:53
 */
@Slf4j
@Service
public class WarehouseTenantInitServiceImpl implements WarehouseTenantInitService {

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
    public void tenantInitialize(TenantInitContext context) {
        log.info("initializeWarehouse start");
        String warehouse = context.getParamMap().get(TenantInitConstants.InitBaseParam.BASE_WAREHOUSE);
        if (StringUtil.isBlank(warehouse)) {
            log.info("base_warehouse  is null");
            return;
        }
        // 1. 查询源租户
        final List<Warehouse> sourceWarehouses = warehouseRepository.selectByCondition(Condition.builder(Warehouse.class)
                .andWhere(Sqls.custom()
                        .andEqualTo(Warehouse.FIELD_TENANT_ID, context.getSourceTenantId())
                        .andIn(Warehouse.FIELD_WAREHOUSE_CODE, Arrays.asList(warehouse.split(",")))
                ).build());

        if (CollectionUtils.isEmpty(sourceWarehouses)) {
            log.warn("VIRTUAL_POS not exists in sourceTenantId[{}]", context.getSourceTenantId());
            return;
        }
        // 2. 查询目标租户是否存在数据
        final List<Warehouse> targetWarehouses = warehouseRepository.selectByCondition(Condition.builder(Warehouse.class)
                .andWhere(Sqls.custom()
                        .andEqualTo(Warehouse.FIELD_TENANT_ID, context.getTargetTenantId())
                        .andIn(Warehouse.FIELD_WAREHOUSE_CODE, Arrays.asList(warehouse.split(",")))
                ).build());
        handleData(targetWarehouses,sourceWarehouses,Arrays.asList(warehouse.split(",")), context);
        log.info("initializeWarehouse finish");
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void tenantInitializeBusiness(TenantInitContext context) {
        log.info("Business: initializeWarehouse start");
        String warehouse = context.getParamMap().get(TenantInitConstants.InitBusinessParam.BUSINESS_WAREHOUSE);
        if (StringUtil.isBlank(warehouse)) {
            log.info("business_warehouse  is null");
            return;
        }
        // 1. 查询源租户
        Warehouse query = new Warehouse();
        query.setTenantId(context.getSourceTenantId());
        query.setWarehouseCodes(Arrays.asList(context.getParamMap().get(TenantInitConstants.InitBusinessParam.BUSINESS_WAREHOUSE).split(",")));
        List<Warehouse> sourceWarehouses = warehouseService.selectByCondition(query);
        if (CollectionUtils.isEmpty(sourceWarehouses)) {
            log.warn("Business data not exists in sourceTenantId[{}]", context.getSourceTenantId());
            return;
        }
        // 2. 查询目标租户是否存在数据
        query.setTenantId(context.getTargetTenantId());
        List<Warehouse> oldWarehouses = warehouseService.selectByCondition(query);
        handleData(oldWarehouses,sourceWarehouses,Arrays.asList(warehouse.split(",")), context);
        log.info("Business: initializeWarehouse finish");
    }

    /**
     *  更新目标库已存在的数据 插入需要初始化的数据
     * @param oldWarehouses  目标库已存在的数据
     * @param initializeWarehouses 目标库需要重新初始化的数据
     * @param context 参数
     */
    private void handleData(List<Warehouse> oldWarehouses, List<Warehouse> initializeWarehouses, List<String> warehouses, TenantInitContext context) {
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
        query.setTenantId(context.getTargetTenantId());
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
            // 虚拟仓库 服务点ID 默认1
            if (warehouses.contains(initCode)) {
                init.setPosId(TenantInitConstants.WarehouseBasis.POS_ID);
            } else {
                Long posId = targetPosMap.get(init.getPosCode());
                init.setPosId(posId);
            }
            warehouseCodeList.add(initCode);
            boolean addFlag = true;
            if (oldWarehouses.isEmpty()) {
                addList.add(init);
                continue;
            }
            for (Warehouse old : oldWarehouses) {
                String oldCode = old.getWarehouseCode();
                if (initCode.equals(oldCode)) {
                    init.setTenantId(context.getTargetTenantId());
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
            warehouse.setTenantId(context.getTargetTenantId());
        });
        // 3. 更新目标库
        warehouseRepository.batchUpdateByPrimaryKey(updateList);
        warehouseRepository.batchInsert(addList);

        // 4. 更新目标数据缓存
        warehouseRedis.batchUpdateWarehouse(warehouseCodeList,context.getTargetTenantId());
    }

}
