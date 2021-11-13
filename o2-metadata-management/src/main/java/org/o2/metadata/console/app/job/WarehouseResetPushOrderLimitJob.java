package org.o2.metadata.console.app.job;

import lombok.extern.slf4j.Slf4j;
import org.hzero.boot.scheduler.infra.annotation.JobHandler;
import org.o2.core.thread.ThreadJobPojo;
import org.o2.metadata.console.app.service.WarehouseService;
import org.o2.metadata.console.infra.entity.Warehouse;
import org.o2.metadata.console.infra.repository.WarehouseRepository;
import org.o2.scheduler.job.AbstractThreadJob;

import java.util.List;

/**
 * 推单量计算
 *
 * @author wenjun.deng01@hand-china.com 2019-07-03
 */
@JobHandler("warehouseResetPushOrderLimitJob")
@Slf4j
public class WarehouseResetPushOrderLimitJob extends AbstractThreadJob<Warehouse> {

    private final WarehouseService warehouseService;
    private final WarehouseRepository warehouseRepository;

    public WarehouseResetPushOrderLimitJob(WarehouseService warehouseService, WarehouseRepository warehouseRepository) {
        this.warehouseService = warehouseService;
        this.warehouseRepository = warehouseRepository;
    }


    @Override
    protected void doExecute(Warehouse prepareData,
                             final ThreadJobPojo threadJobPojo) {
        warehouseService.resetWarehouseExpressLimit(prepareData.getWarehouseCode(), threadJobPojo.getTenantId());
    }

    @Override
    protected List<Warehouse> prepareExecuteData(final ThreadJobPojo threadJobPojo) {
        return warehouseRepository.selectAllDeliveryWarehouse(threadJobPojo.getTenantId());
    }
}
