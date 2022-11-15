package org.o2.metadata.console.app.service.impl;

import io.choerodon.core.oauth.DetailsHelper;
import org.o2.metadata.console.app.bo.SourcingConfigUpdateBO;
import org.o2.metadata.console.app.service.SourcingCacheUpdateService;
import org.o2.metadata.console.infra.constant.WarehouseConstants;
import org.o2.queue.app.service.ProducerService;
import org.o2.queue.domain.context.ProducerContext;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * @author tangcj
 * @version V1.0
 * @date 2022/6/8 10:52
 */
@Service
public class SourcingCacheUpdateServiceImpl implements SourcingCacheUpdateService {

    private final ProducerService producerService;

    public SourcingCacheUpdateServiceImpl(ProducerService producerService) {
        this.producerService = producerService;
    }

    @Override
    public void refreshSourcingCache(Long tenantId, String className) {
        refreshSourcingCache(tenantId, className, Arrays.asList(WarehouseConstants.WarehouseEventManagement.WAREHOUSE_CACHE_NAME, WarehouseConstants.WarehouseEventManagement.SHOP_REL_WAREHOUSE_CACHE_NAME));
    }

    @Override
    public void refreshSourcingNearRegion(Long tenantId, String className) {
        refreshSourcingCache(tenantId, className, Collections.singletonList(WarehouseConstants.WarehouseEventManagement.NEAR_REGION_CACHE_NAME));
    }

    private void refreshSourcingCache(Long tenantId, String className, List<String> cacheNames) {
        // 寻源弃用，kafka不支持多pod实时更新
        if (false) {
            final ProducerContext<SourcingConfigUpdateBO> context = new ProducerContext<>();
            SourcingConfigUpdateBO data = new SourcingConfigUpdateBO();
            data.setCacheNames(cacheNames);
            data.setOperationTime(LocalDateTime.now());
            data.setOperationClassName(className);
            data.setOperator(DetailsHelper.getUserDetails().getUserId());
            context.setQueueCode(WarehouseConstants.WarehouseEventManagement.O2SE_CONFIG_CACHE_UPDATE_EVT);
            context.setData(data);
            producerService.produce(tenantId, context);
        }
    }

}
