package org.o2.metadata.console.app.job;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.hzero.boot.scheduler.infra.annotation.JobHandler;
import org.hzero.boot.scheduler.infra.tool.SchedulerTool;
import org.o2.core.thread.ThreadJobPojo;
import org.o2.metadata.console.api.co.WarehouseRelAddressCO;
import org.o2.metadata.console.app.service.LovAdapterService;
import org.o2.metadata.console.app.service.WarehouseService;
import org.o2.metadata.console.infra.constant.MetadataConstants;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 仓库推单量重置
 *
 * @author wenjun.deng01@hand-china.com 2019-07-03
 */
@JobHandler("warehouseResetPushLimitJob")
@Slf4j
public class WarehouseResetPushLimitJob extends AbstractMetadataBatchThreadJob {

    private final WarehouseService warehouseService;

    public WarehouseResetPushLimitJob(WarehouseService warehouseService,
                                      LovAdapterService lovAdapterService) {
        super(lovAdapterService);
        this.warehouseService = warehouseService;
    }

    @Override
    public void doExecute(SchedulerTool tool, List<Long> data, ThreadJobPojo threadJobPojo) {
        Map<String, String> jobParams = Optional.ofNullable(threadJobPojo.getJobParams()).orElse(new HashMap<>());
        String tenantIdParam = jobParams.get(MetadataConstants.RefreshJobConstants.TENANT_ID);

        if (StringUtils.isNotBlank(tenantIdParam)) {
            data = Collections.singletonList(Long.valueOf(tenantIdParam));
        }

        for (Long tenantId : data) {
            try {
                List<WarehouseRelAddressCO> whRelAddrList = warehouseService.selectAllDeliveryWarehouse(tenantId);
                if (CollectionUtils.isEmpty(whRelAddrList)) {
                    continue;
                }
                List<String> codes = whRelAddrList.stream().map(WarehouseRelAddressCO::getWarehouseCode).collect(Collectors.toList());
                warehouseService.batchResetWhExpressLimit(codes, tenantId);
            } catch (Exception ex) {
                log.error("warehouse reset push limit failed, tenantId: {}", tenantId, ex);
            }
        }
    }
}
