package org.o2.metadata.console.app.job;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.hzero.boot.scheduler.infra.annotation.JobHandler;
import org.hzero.boot.scheduler.infra.enums.ReturnT;
import org.hzero.boot.scheduler.infra.handler.IJobHandler;
import org.hzero.boot.scheduler.infra.tool.SchedulerTool;
import org.hzero.core.base.BaseConstants;
import org.o2.metadata.console.app.service.MetadataTenantInitService;


import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * 元数据多租户初始化Job
 *
 * @author zhanpeng.jiang@hand-china.com
 * @date 2021/12/17 10:26
 */
@Slf4j
@JobHandler(value = "metadataTenantInitializeJob")
public class MetadataTenantInitializeJob implements IJobHandler {

    private static final String TARGET_TENANT_IDS = "targetTenants";

    private static final String SOURCE_TENANT_ID = "sourceTenant";

    private final MetadataTenantInitService metadataTenantInitService;

    public MetadataTenantInitializeJob(MetadataTenantInitService metadataTenantInitService) {
        this.metadataTenantInitService = metadataTenantInitService;
    }

    @Override
    public ReturnT execute(Map<String, String> map, SchedulerTool tool) {
        final String targetTenants = map.get(TARGET_TENANT_IDS);
        final String sourceTenantIdStr = map.getOrDefault(SOURCE_TENANT_ID, String.valueOf(BaseConstants.DEFAULT_TENANT_ID));
        if (StringUtils.isEmpty(targetTenants)) {
            tool.error("Parameter [targetTenants] can't be null.Please check job configuration.");
            return ReturnT.FAILURE;
        }
        List<String> tenantList = Arrays.asList(targetTenants.split(BaseConstants.Symbol.COMMA));
        final long sourceTenantId = Long.parseLong(sourceTenantIdStr);
        metadataTenantInitService.tenantInitialize(sourceTenantId, tenantList);
        return ReturnT.SUCCESS;
    }
}
