package org.o2.metadata.console.app.job;

import io.choerodon.mybatis.helper.LanguageHelper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.hzero.boot.scheduler.infra.annotation.JobHandler;
import org.hzero.boot.scheduler.infra.enums.ReturnT;
import org.hzero.boot.scheduler.infra.handler.IJobHandler;
import org.hzero.boot.scheduler.infra.tool.SchedulerTool;
import org.o2.core.O2CoreConstants;
import org.o2.metadata.console.api.dto.CountryRefreshDTO;
import org.o2.metadata.console.app.service.CountryRefreshService;
import org.o2.metadata.console.infra.constant.MetadataConstants;

import java.util.Map;

/**
 * 地区信息刷新job
 *
 * @author rui.ling@hand-china.com 2023/02/14
 */
@Slf4j
@JobHandler(value = "o2CountryRefreshJob")
public class O2CountryRefreshJob implements IJobHandler {

    private final CountryRefreshService countryRefreshService;

    public O2CountryRefreshJob(CountryRefreshService countryRefreshService) {
        this.countryRefreshService = countryRefreshService;
    }


    @Override
    public ReturnT execute(Map<String, String> map, SchedulerTool tool) {
        // job参数获取
        final String tenantId = map.get(MetadataConstants.RefreshJobConstants.TENANT_ID);
        final String lang = map.getOrDefault(MetadataConstants.RefreshJobConstants.LANG, LanguageHelper.language());
        final String businessTypeCode = map.getOrDefault(MetadataConstants.RefreshJobConstants.BUSINESS_TYPE_CODE,
                O2CoreConstants.BusinessType.B2C);
        final String bucketPrefix = map.getOrDefault(MetadataConstants.RefreshJobConstants.BUCKET_PREFIX,
                MetadataConstants.RefreshJobConstants.DEFAULT_BUCKET_PREFIX);

        if (StringUtils.isBlank(tenantId)) {
            tool.error("Parameter [tenantId] can't be null.Please check job configuration.");
            return ReturnT.FAILURE;
        }

        // 构建参数
        CountryRefreshDTO countryRefreshDTO = new CountryRefreshDTO();
        countryRefreshDTO.setTenantId(Long.parseLong(tenantId));
        countryRefreshDTO.setLang(lang);
        countryRefreshDTO.setBusinessTypeCode(businessTypeCode);
        countryRefreshDTO.setBucketPrefix(bucketPrefix.toLowerCase());

        // 刷新国家静态资源
        countryRefreshService.refreshCountryInfoFile(countryRefreshDTO);

        return ReturnT.SUCCESS;
    }
}
