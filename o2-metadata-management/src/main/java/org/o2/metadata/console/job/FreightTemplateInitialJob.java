package org.o2.metadata.console.job;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.hzero.boot.scheduler.infra.annotation.JobHandler;
import org.hzero.boot.scheduler.infra.enums.ReturnT;
import org.hzero.boot.scheduler.infra.handler.IJobHandler;
import org.hzero.boot.scheduler.infra.tool.SchedulerTool;
import org.hzero.mybatis.domian.Condition;
import org.hzero.mybatis.util.Sqls;
import org.o2.metadata.console.app.service.FreightTemplateService;
import org.o2.metadata.console.infra.entity.FreightTemplate;
import org.o2.metadata.console.infra.repository.FreightTemplateRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Map;

/**
 * 运费模板初始化
 *
 * @author peixin.zhao@hand-china.com
 */
@Slf4j
@JobHandler(value = "freightTemplateInitialJob")
public class FreightTemplateInitialJob implements IJobHandler {
    private static final String TENANT_ID = "tenantId";

    @Autowired
    private FreightTemplateRepository freightTemplateRepository;
    @Autowired
    private FreightTemplateService freightTemplateService;

    @Override
    public ReturnT execute(Map<String, String> map, SchedulerTool tool) {
        log.info("FreightTemplateInitialJob begin");
        final String organizationId = map.get(TENANT_ID);
        if (StringUtils.isBlank(organizationId)) {
            tool.error("Parameter [tenantId] can't be null. Please check job configuration.");
            return ReturnT.FAILURE;
        }
     // 查询租户下的所有模板
        final List<FreightTemplate> list = freightTemplateRepository.selectByCondition(Condition.builder(FreightTemplate.class)
                .andWhere(Sqls.custom().andEqualTo(FreightTemplate.FIELD_TENANT_ID,   organizationId)).build());
        if(CollectionUtils.isEmpty(list)){
            log.error("该租户下没有可用的运维模板");
            return ReturnT.FAILURE;
        }
        for (FreightTemplate template:list ) {
            freightTemplateService.refreshCache(template.getTemplateId());
            log.info("FreightTemplateInitialJob init templateId:{},templateCode:{},templateName:{} success",template.getTemplateId(),template.getTemplateCode(),template.getTemplateName());
        }
        log.info("FreightTemplateInitialJob success!!!");
        return ReturnT.SUCCESS;
    }
}
