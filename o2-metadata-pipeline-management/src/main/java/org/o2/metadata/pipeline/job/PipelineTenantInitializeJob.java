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

    private static final String TENANT_IDS = "tenantIds";

    private final PipelineTenantInitService pipelineTenantInitService;

    public PipelineTenantInitializeJob(PipelineTenantInitService pipelineTenantInitService) {
        this.pipelineTenantInitService = pipelineTenantInitService;
    }

    @Override
    public ReturnT execute(Map<String, String> map, SchedulerTool tool) {
        final String tenantIds = map.get(TENANT_IDS);
        if (StringUtils.isEmpty(tenantIds)) {
            tool.error("Parameter [tenantIds] can't be null.Please check job configuration.");
            return ReturnT.FAILURE;
        }
        List<String> tenantList = Arrays.asList(tenantIds.split(BaseConstants.Symbol.COMMA));
        pipelineTenantInitService.tenantInitialize(tenantList);
        return ReturnT.SUCCESS;
    }
}
