package org.o2.metadata.console.app.job;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.hzero.boot.scheduler.infra.annotation.JobHandler;
import org.hzero.boot.scheduler.infra.enums.ReturnT;
import org.hzero.boot.scheduler.infra.handler.IJobHandler;
import org.hzero.boot.scheduler.infra.tool.SchedulerTool;
import org.o2.metadata.console.infra.constant.MetadataConstants;
import org.o2.metadata.console.infra.entity.FreightTemplate;
import org.o2.metadata.console.infra.repository.FreightTemplateRepository;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Description
 *
 * @author lei.tong01@hand-china.com
 * @since 2023/6/27
 */
@Slf4j
@JobHandler(value = "o2FreightTemplateRefreshDbJob")
public class O2FreightTemplateRefreshDbJob implements IJobHandler {

    private final FreightTemplateRepository freightTemplateRepository;

    public O2FreightTemplateRefreshDbJob(FreightTemplateRepository freightTemplateRepository) {
        this.freightTemplateRepository = freightTemplateRepository;
    }

    @Override
    public ReturnT execute(Map<String, String> map, SchedulerTool tool) {
        final String tenantIdParam = map.get(MetadataConstants.RefreshJobConstants.TENANT_ID);
        Long tenantId = StringUtils.isNotBlank(tenantIdParam) ? Long.valueOf(tenantIdParam) : null;
        List<FreightTemplate> templates = freightTemplateRepository.selectTemplateForRefresh(tenantId);
        if (CollectionUtils.isNotEmpty(templates)) {
            List<FreightTemplate> filters = templates.stream().filter(freightTemplate ->
                    CollectionUtils.isEmpty(freightTemplate.getMultiLangList())).collect(Collectors.toList());
            freightTemplateRepository.batchUpdateByPrimaryKey(filters);
        }
        return ReturnT.SUCCESS;
    }
}
