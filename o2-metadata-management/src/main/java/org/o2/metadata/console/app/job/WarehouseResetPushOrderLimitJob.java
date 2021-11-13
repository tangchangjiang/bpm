package org.o2.metadata.console.app.job;

import lombok.extern.slf4j.Slf4j;
import org.hzero.boot.scheduler.infra.annotation.JobHandler;
import org.o2.core.thread.ThreadJobPojo;
import org.o2.metadata.console.api.co.WarehouseRelAddressCO;
import org.o2.metadata.console.app.service.WarehouseService;
import org.o2.metadata.console.infra.entity.Warehouse;
import org.o2.metadata.console.infra.repository.WarehouseRepository;
import org.o2.scheduler.job.AbstractThreadJob;

import java.util.List;

/**
 * 仓库推单量重置
 *
 * @author wenjun.deng01@hand-china.com 2019-07-03
 */
@JobHandler("warehouseResetPushOrderLimitJob")
@Slf4j
public class WarehouseResetPushOrderLimitJob extends AbstractThreadJob<WarehouseRelAddressCO> {

    private final WarehouseService warehouseService;

    public WarehouseResetPushOrderLimitJob(WarehouseService warehouseService) {
        this.warehouseService = warehouseService;
    }


    @Override
    protected void doExecute(WarehouseRelAddressCO prepareData,
                             final ThreadJobPojo threadJobPojo) {
        warehouseService.resetWarehouseExpressLimit(prepareData.getWarehouseCode(), threadJobPojo.getTenantId());
    }

    @Override
    protected List<WarehouseRelAddressCO> prepareExecuteData(final ThreadJobPojo threadJobPojo) {
        return warehouseService.selectAllDeliveryWarehouse(threadJobPojo.getTenantId());
    }
}
