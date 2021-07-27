package org.o2.metadata.console.job;

import lombok.extern.slf4j.Slf4j;
import org.hzero.boot.scheduler.infra.annotation.JobHandler;
import org.hzero.boot.scheduler.infra.enums.ReturnT;
import org.hzero.boot.scheduler.infra.handler.IJobHandler;
import org.hzero.boot.scheduler.infra.tool.SchedulerTool;
import org.o2.metadata.console.app.service.O2SiteRegionFileService;
import org.o2.metadata.console.api.vo.RegionCacheVO;
import org.springframework.util.StringUtils;

import java.util.Map;

/**
 * 同步地区数据
 *
 * @author: yipeng.zhu@hand-china.com 2020-05-20 10:30
 **/
@Slf4j
@JobHandler(value = "o2SiteRegionFileRefreshJob")
public class O2SiteRegionFileRefreshJob implements IJobHandler {
    private static final String TENANT_ID = "tenantId";
    private static final String COUNTRY_CODE = "countryCode";

    private final O2SiteRegionFileService o2SiteRegionFileService;

    public O2SiteRegionFileRefreshJob(O2SiteRegionFileService o2SiteRegionFileService) {
        this.o2SiteRegionFileService = o2SiteRegionFileService;
    }

    @Override
    public ReturnT execute(Map<String, String> map, SchedulerTool tool) {
        final String organizationId = map.get(TENANT_ID);
        final String countryCode = map.get(COUNTRY_CODE);
        if (!StringUtils.hasText(organizationId) || !StringUtils.hasText(countryCode)) {
            tool.error("Parameter [tenantId] and [countryCode] can't be null.Please check job configuration.");
            return ReturnT.FAILURE;
        }

        final RegionCacheVO vo = new RegionCacheVO();
        vo.setTenantId(Long.parseLong(organizationId));
        vo.setCountryCode(countryCode);
        o2SiteRegionFileService.createRegionStaticFile(vo);
        return ReturnT.SUCCESS;
    }
}
