package org.o2.business.process.management.app.job;

import org.apache.commons.lang3.StringUtils;
import org.hzero.boot.scheduler.infra.tool.SchedulerTool;
import org.hzero.core.base.BaseConstants;
import org.hzero.core.util.Pair;
import org.o2.core.O2CoreConstants;
import org.o2.core.thread.ThreadJobPojo;
import org.o2.metadata.management.client.SQLLovClient;
import org.o2.scheduler.job.AbstractBatchThreadJob;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 流程器job抽象类
 *
 * @author chao.yang05@hand-china.com 2023-05-19
 */
public abstract class AbstractBpmBatchThreadJob extends AbstractBatchThreadJob<Long> {

    private final SQLLovClient sqlLovClient;

    protected AbstractBpmBatchThreadJob(SQLLovClient sqlLovClient) {
        this.sqlLovClient = sqlLovClient;
    }

    @Override
    public Pair<Boolean, List<Long>> prepareExecuteData(SchedulerTool tool, int times, ThreadJobPojo threadJobPojo) {
        Map<String, String> jobParams = Optional.ofNullable(threadJobPojo.getJobParams()).orElse(new HashMap<>());
        String tenantId = jobParams.get(O2CoreConstants.EntityDomain.FIELD_TENANT_ID);
        // 如果设置了租户Id，则不查询所有租户
        if (StringUtils.isNotBlank(tenantId)) {
            return new Pair<>(Boolean.TRUE, Collections.singletonList(Long.parseLong(tenantId)));
        }
        int page = (times - BaseConstants.Digital.ONE);
        int size = Math.toIntExact(threadJobPojo.getBatchSize());
        List<Map<String, Object>> tenantResult = sqlLovClient.queryLovValueMeaningPage(BaseConstants.DEFAULT_TENANT_ID, "O2MD.ALL_TENANT_ID",  new HashMap<>(), page, size);
        List<Long> tenantIds = tenantResult.stream().map(tenant
                -> Long.valueOf(String.valueOf(tenant.get(O2CoreConstants.EntityDomain.FIELD_TENANT_ID)))).collect(Collectors.toList());
        return new Pair<>(Boolean.TRUE, tenantIds);
    }
}
