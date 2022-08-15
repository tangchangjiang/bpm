package org.o2.business.process.management.app.job;

import lombok.extern.slf4j.Slf4j;
import org.hzero.boot.scheduler.infra.annotation.JobHandler;
import org.hzero.boot.scheduler.infra.enums.ReturnT;
import org.hzero.boot.scheduler.infra.handler.IJobHandler;
import org.hzero.boot.scheduler.infra.tool.SchedulerTool;
import org.hzero.mybatis.domian.Condition;
import org.hzero.mybatis.util.Sqls;
import org.o2.business.process.management.app.service.BusinessProcessRedisService;
import org.o2.business.process.management.domain.entity.BusinessNode;
import org.o2.business.process.management.domain.entity.BusinessProcess;
import org.o2.business.process.management.domain.repository.BusinessNodeRepository;
import org.o2.business.process.management.domain.repository.BusinessProcessRepository;
import org.o2.core.O2CoreConstants;

import java.util.List;
import java.util.Map;

/**
 * 业务流程缓存刷新job
 * @author tangcj
 * @version V1.0
 * @date 2022/8/15 10:40
 */
@Slf4j
@JobHandler(value = "businessProcessCacheRefreshJob")
public class BusinessProcessCacheRefreshJob implements IJobHandler {

    private final BusinessNodeRepository businessNodeRepository;

    private final BusinessProcessRepository businessProcessRepository;

    private final BusinessProcessRedisService businessProcessRedisService;

    public BusinessProcessCacheRefreshJob(BusinessNodeRepository businessNodeRepository, BusinessProcessRepository businessProcessRepository, BusinessProcessRedisService businessProcessRedisService) {
        this.businessNodeRepository = businessNodeRepository;
        this.businessProcessRepository = businessProcessRepository;
        this.businessProcessRedisService = businessProcessRedisService;
    }

    @Override
    public ReturnT execute(Map<String, String> map, SchedulerTool tool) {
        if(!map.containsKey(O2CoreConstants.EntityDomain.FIELD_TENANT_ID)){
            tool.info("tenantId do not be null");
            return ReturnT.FAILURE;
        }
        Long tenantId = Long.parseLong(map.get(O2CoreConstants.EntityDomain.FIELD_TENANT_ID));
        List<BusinessNode> nodeList = businessNodeRepository.selectByCondition(Condition.builder(BusinessNode.class).andWhere(Sqls.custom()
                .andEqualTo(BusinessNode.FIELD_TENANT_ID, tenantId)).build());

        List<BusinessProcess> businessProcessList = businessProcessRepository.selectByCondition(Condition.builder(BusinessProcess.class).andWhere(Sqls.custom()
                .andEqualTo(BusinessProcess.FIELD_TENANT_ID, tenantId)).build());

        businessProcessRedisService.batchUpdateNodeStatus(nodeList, tenantId);
        businessProcessRedisService.batchUpdateProcessConfig(businessProcessList, tenantId);
        return ReturnT.SUCCESS;
    }
}
