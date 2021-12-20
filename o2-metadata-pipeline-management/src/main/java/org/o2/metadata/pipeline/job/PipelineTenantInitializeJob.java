package org.o2.metadata.pipeline.job;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.hzero.boot.scheduler.infra.annotation.JobHandler;
import org.hzero.boot.scheduler.infra.enums.ReturnT;
import org.hzero.boot.scheduler.infra.handler.IJobHandler;
import org.hzero.boot.scheduler.infra.tool.SchedulerTool;
import org.hzero.core.base.BaseConstants;
import org.o2.metadata.pipeline.app.service.PipelineTenantInitService;


import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * description
 *
 * @author zhanpeng.jiang@hand-china.com
 * @date 2021/12/18 13:04
 */
@Slf4j
@JobHandler(value = "pipelineTenantInitializeJob")
public class PipelineTenantInitializeJob implements IJobHandler {

    private static final String TARGET_TENANT_IDS = "targetTenants";

    private static final String SOURCE_TENANT_ID = "sourceTenantId";

    private final PipelineTenantInitService pipelineTenantInitService;

    public PipelineTenantInitializeJob(PipelineTenantInitService pipelineTenantInitService) {
        this.pipelineTenantInitService = pipelineTenantInitService;
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
        pipelineTenantInitService.tenantInitialize(sourceTenantId, tenantList);
        return ReturnT.SUCCESS;
    }
}
