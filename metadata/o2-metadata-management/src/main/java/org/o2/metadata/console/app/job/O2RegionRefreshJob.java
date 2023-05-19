package org.o2.metadata.console.app.job;

import io.choerodon.mybatis.helper.LanguageHelper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.hzero.boot.scheduler.infra.annotation.JobHandler;
import org.hzero.boot.scheduler.infra.tool.SchedulerTool;
import org.o2.core.O2CoreConstants;
import org.o2.core.helper.JsonHelper;
import org.o2.core.thread.ThreadJobPojo;
import org.o2.metadata.console.api.vo.RegionCacheVO;
import org.o2.metadata.console.app.service.LovAdapterService;
import org.o2.metadata.console.app.service.O2SiteRegionFileService;
import org.o2.metadata.console.infra.constant.MetadataConstants;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * 同步地区数据
 *
 * @author: yipeng.zhu@hand-china.com 2020-05-20 10:30
 **/
@Slf4j
@JobHandler(value = "o2RegionRefreshJob")
public class O2RegionRefreshJob extends AbstractMetadataBatchThreadJob {

    private final O2SiteRegionFileService o2SiteRegionFileService;

    public O2RegionRefreshJob(O2SiteRegionFileService o2SiteRegionFileService,
                              LovAdapterService lovAdapterService) {
        super(lovAdapterService);
        this.o2SiteRegionFileService = o2SiteRegionFileService;
    }

    @Override
    public void doExecute(SchedulerTool tool, List<Long> data, ThreadJobPojo threadJobPojo) {
        Map<String, String> map = Optional.ofNullable(threadJobPojo.getJobParams()).orElse(new HashMap<>());
        final String tenantIdParam = map.get(MetadataConstants.RefreshJobConstants.TENANT_ID);
        final String countryCode = map.get(MetadataConstants.RefreshJobConstants.COUNTRY_CODE);
        final String regionOwner = map.get(MetadataConstants.RefreshJobConstants.REGION_OWNER);
        final String lang = map.getOrDefault(MetadataConstants.RefreshJobConstants.LANG, LanguageHelper.language());
        final String businessTypeCode = map.getOrDefault(MetadataConstants.RefreshJobConstants.BUSINESS_TYPE_CODE, O2CoreConstants.BusinessType.B2C);

        if (StringUtils.isNotBlank(tenantIdParam)) {
            data = Collections.singletonList(Long.valueOf(tenantIdParam));
        }

        for (Long tenantId : data) {
            final RegionCacheVO regionCacheVO = new RegionCacheVO();
            regionCacheVO.setTenantId(tenantId);
            regionCacheVO.setCountryCode(countryCode);
            regionCacheVO.setLang(lang);
            try {
                o2SiteRegionFileService.createRegionStaticFile(regionCacheVO, regionOwner, businessTypeCode);
            } catch (Exception ex) {
                log.error("region refresh failed, param: {}", JsonHelper.objectToString(regionCacheVO), ex);
            }
        }
    }

}
