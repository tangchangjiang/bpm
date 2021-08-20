package org.o2.metadata.console.job;

import lombok.extern.slf4j.Slf4j;
import org.hzero.boot.scheduler.infra.annotation.JobHandler;
import org.hzero.boot.scheduler.infra.enums.ReturnT;
import org.hzero.boot.scheduler.infra.handler.IJobHandler;
import org.hzero.boot.scheduler.infra.tool.SchedulerTool;
import org.o2.metadata.console.api.vo.PublicLovVO;
import org.o2.metadata.console.app.service.O2SitePublicLovService;
import org.springframework.util.StringUtils;

import java.util.Map;

/**
 * O2MD.PUBLIC_LOV  值集文件缓存刷新Job
 *
 * @author: kang.yang@hand-china.com 2021-08-20 14:17
 **/
@Slf4j
@JobHandler(value = "o2SitePublicLovFileRefreshJob")
public class O2SitePublicLovFileRefreshJob implements IJobHandler {
    private static final String TENANT_ID = "tenantId";
    private static final String LOV_CODE = "lovCode";

    private final O2SitePublicLovService o2SitePublicLovService;

    public O2SitePublicLovFileRefreshJob(O2SitePublicLovService o2SitePublicLovService) {
        this.o2SitePublicLovService = o2SitePublicLovService;
    }


    @Override
    public ReturnT execute(Map<String, String> map, SchedulerTool tool) {
        final String tenantId = map.get(TENANT_ID);
        final String lovCode = map.get(LOV_CODE);
        if (!StringUtils.hasText(tenantId) || !StringUtils.hasText(lovCode)) {
            tool.error("Parameter [tenantId] and [lovCode] can't be null.Please check job configuration.");
            return ReturnT.FAILURE;
        }
        final PublicLovVO vo = new PublicLovVO();
        vo.setTenantId(Long.parseLong(tenantId));
        vo.setLovCode(lovCode);
        o2SitePublicLovService.createPublicLovFile(vo);

        return ReturnT.SUCCESS;
    }


}
