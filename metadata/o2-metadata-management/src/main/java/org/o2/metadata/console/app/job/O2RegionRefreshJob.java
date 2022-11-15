package org.o2.metadata.console.app.job;

import lombok.extern.slf4j.Slf4j;
import org.hzero.boot.scheduler.infra.annotation.JobHandler;
import org.hzero.boot.scheduler.infra.enums.ReturnT;
import org.hzero.boot.scheduler.infra.handler.IJobHandler;
import org.hzero.boot.scheduler.infra.tool.SchedulerTool;
import org.o2.core.O2CoreConstants;
import org.o2.metadata.console.api.vo.RegionCacheVO;
import org.o2.metadata.console.app.service.O2SiteRegionFileService;
import org.o2.metadata.console.infra.constant.MetadataConstants;
import org.springframework.util.StringUtils;

import java.util.Map;

/**
 * 同步地区数据
 *
 * @author: yipeng.zhu@hand-china.com 2020-05-20 10:30
 **/
@Slf4j
@JobHandler(value = "o2RegionRefreshJob")
public class O2RegionRefreshJob implements IJobHandler {

    private final O2SiteRegionFileService o2SiteRegionFileService;

    public O2RegionRefreshJob(O2SiteRegionFileService o2SiteRegionFileService) {
        this.o2SiteRegionFileService = o2SiteRegionFileService;
    }

    @Override
    public ReturnT execute(Map<String, String> map, SchedulerTool tool) {
        final String tenantId = map.get(MetadataConstants.RefreshJobConstants.TENANT_ID);
        final String countryCode = map.get(MetadataConstants.RefreshJobConstants.COUNTRY_CODE);
        final String regionOwner = map.get(MetadataConstants.RefreshJobConstants.REGION_OWNER);
        final String businessTypeCode = map.getOrDefault(MetadataConstants.RefreshJobConstants.BUSINESS_TYPE_CODE, O2CoreConstants.BusinessType.B2C);

        if (!StringUtils.hasText(tenantId) || !StringUtils.hasText(countryCode)) {
            tool.error("Parameter [tenantId] and [countryCode] can't be null.Please check job configuration.");
            return ReturnT.FAILURE;
        }

        final RegionCacheVO vo = new RegionCacheVO();
        vo.setTenantId(Long.parseLong(tenantId));
        vo.setCountryCode(countryCode);
        o2SiteRegionFileService.createRegionStaticFile(vo, regionOwner, businessTypeCode);

        return ReturnT.SUCCESS;
    }

}
