package org.o2.metadata.console.app.job;

import io.choerodon.mybatis.helper.LanguageHelper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.hzero.boot.scheduler.infra.annotation.JobHandler;
import org.hzero.boot.scheduler.infra.tool.SchedulerTool;
import org.o2.core.O2CoreConstants;
import org.o2.core.helper.JsonHelper;
import org.o2.core.thread.ThreadJobPojo;
import org.o2.metadata.console.api.dto.CountryRefreshDTO;
import org.o2.metadata.console.app.service.CountryRefreshService;
import org.o2.metadata.console.app.service.LovAdapterService;
import org.o2.metadata.console.infra.constant.MetadataConstants;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * 地区信息刷新job
 *
 * @author rui.ling@hand-china.com 2023/02/14
 */
@Slf4j
@JobHandler(value = "o2CountryRefreshJob")
public class O2CountryRefreshJob extends AbstractMetadataBatchThreadJob {

    private final CountryRefreshService countryRefreshService;

    public O2CountryRefreshJob(CountryRefreshService countryRefreshService,
                               LovAdapterService lovAdapterService) {
        super(lovAdapterService);
        this.countryRefreshService = countryRefreshService;
    }

    @Override
    public void doExecute(SchedulerTool tool, List<Long> data, ThreadJobPojo threadJobPojo) {
        // job参数获取
        Map<String, String> map = Optional.ofNullable(threadJobPojo.getJobParams()).orElse(new HashMap<>());
        String tenantIdParam = map.get(MetadataConstants.RefreshJobConstants.TENANT_ID);
        // 如果设置了租户Id，则不查询所有租户
        if (StringUtils.isNotBlank(tenantIdParam)) {
            data = Collections.singletonList(Long.valueOf(tenantIdParam));
        }

        final String lang = map.getOrDefault(MetadataConstants.RefreshJobConstants.LANG, LanguageHelper.language());
        final String businessTypeCode = map.getOrDefault(MetadataConstants.RefreshJobConstants.BUSINESS_TYPE_CODE,
                O2CoreConstants.BusinessType.B2C);
        for (Long tenantId : data) {
            // 构建参数
            CountryRefreshDTO countryRefreshDTO = new CountryRefreshDTO();
            countryRefreshDTO.setTenantId(tenantId);
            countryRefreshDTO.setLang(lang);
            countryRefreshDTO.setBusinessTypeCode(businessTypeCode);
            try {
                // 刷新国家静态资源
                countryRefreshService.refreshCountryInfoFile(countryRefreshDTO);
            } catch (Exception ex) {
                log.error("country refresh failed, param: {}", JsonHelper.objectToString(countryRefreshDTO), ex);
            }
        }
    }
}
