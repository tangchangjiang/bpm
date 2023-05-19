package org.o2.business.process.management.app.job;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.hzero.boot.scheduler.infra.annotation.JobHandler;
import org.hzero.boot.scheduler.infra.tool.SchedulerTool;
import org.hzero.mybatis.domian.Condition;
import org.hzero.mybatis.util.Sqls;
import org.o2.business.process.management.app.service.BusinessProcessRedisService;
import org.o2.business.process.management.domain.entity.BusinessNode;
import org.o2.business.process.management.domain.entity.BusinessProcess;
import org.o2.business.process.management.domain.repository.BusinessNodeRepository;
import org.o2.business.process.management.domain.repository.BusinessProcessRepository;
import org.o2.core.O2CoreConstants;
import org.o2.core.thread.ThreadJobPojo;
import org.o2.metadata.management.client.SQLLovClient;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * 业务流程缓存刷新job
 *
 * @author tangcj
 * @version V1.0
 * @date 2022/8/15 10:40
 */
@Slf4j
@JobHandler(value = "businessProcessCacheRefreshJob")
public class BusinessProcessCacheRefreshJob extends AbstractBpmBatchThreadJob {

    private final BusinessNodeRepository businessNodeRepository;

    private final BusinessProcessRepository businessProcessRepository;

    private final BusinessProcessRedisService businessProcessRedisService;

    public BusinessProcessCacheRefreshJob(BusinessNodeRepository businessNodeRepository, BusinessProcessRepository businessProcessRepository,
                                          BusinessProcessRedisService businessProcessRedisService,
                                          SQLLovClient sqlLovClient) {
        super(sqlLovClient);
        this.businessNodeRepository = businessNodeRepository;
        this.businessProcessRepository = businessProcessRepository;
        this.businessProcessRedisService = businessProcessRedisService;
    }

    @Override
    public void doExecute(SchedulerTool tool, List<Long> data, ThreadJobPojo threadJobPojo) {
        // job参数获取
        Map<String, String> map = Optional.ofNullable(threadJobPojo.getJobParams()).orElse(new HashMap<>());
        String tenantIdParam = map.get(O2CoreConstants.EntityDomain.FIELD_TENANT_ID);
        // 如果设置了租户Id，则不查询所有租户
        if (StringUtils.isNotBlank(tenantIdParam)) {
            data = Collections.singletonList(Long.valueOf(tenantIdParam));
        }

        for (Long tenantId : data) {
            List<BusinessNode> nodeList = businessNodeRepository.selectByCondition(Condition.builder(BusinessNode.class).andWhere(Sqls.custom()
                    .andEqualTo(BusinessNode.FIELD_TENANT_ID, tenantId)).build());

            List<BusinessProcess> businessProcessList =
                    businessProcessRepository.selectByCondition(Condition.builder(BusinessProcess.class).andWhere(Sqls.custom()
                            .andEqualTo(BusinessProcess.FIELD_TENANT_ID, tenantId)).build());

            businessProcessRedisService.batchUpdateNodeStatus(nodeList, tenantId);
            businessProcessRedisService.batchUpdateProcessConfig(businessProcessList, tenantId);
        }
    }
}
