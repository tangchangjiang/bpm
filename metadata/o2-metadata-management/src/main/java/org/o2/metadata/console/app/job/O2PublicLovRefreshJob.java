package org.o2.metadata.console.app.job;

import io.choerodon.mybatis.helper.LanguageHelper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.hzero.boot.scheduler.infra.annotation.JobHandler;
import org.hzero.boot.scheduler.infra.tool.SchedulerTool;
import org.o2.core.O2CoreConstants;
import org.o2.core.helper.JsonHelper;
import org.o2.core.thread.ThreadJobPojo;
import org.o2.metadata.console.api.vo.PublicLovVO;
import org.o2.metadata.console.app.service.LovAdapterService;
import org.o2.metadata.console.app.service.O2PublicLovService;
import org.o2.metadata.console.infra.constant.MetadataConstants;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * O2MD.PUBLIC_LOV  值集文件缓存刷新Job
 *
 * @author: kang.yang@hand-china.com 2021-08-20 14:17
 **/
@Slf4j
@JobHandler(value = "o2PublicLovRefreshJob")
public class O2PublicLovRefreshJob extends AbstractMetadataBatchThreadJob {

    private final O2PublicLovService o2PublicLovService;

    public O2PublicLovRefreshJob(O2PublicLovService o2PublicLovService,
                                 LovAdapterService lovAdapterService) {
        super(lovAdapterService);
        this.o2PublicLovService = o2PublicLovService;
    }

    @Override
    public void doExecute(SchedulerTool tool, List<Long> data, ThreadJobPojo threadJobPojo) {
        Map<String, String> map = Optional.ofNullable(threadJobPojo.getJobParams()).orElse(new HashMap<>());
        final String tenantIdParam = map.get(MetadataConstants.RefreshJobConstants.TENANT_ID);
        final String idpLovOwner = map.get(MetadataConstants.RefreshJobConstants.IDP_LOV_OWNER);
        final String businessTypeCode = map.getOrDefault(MetadataConstants.RefreshJobConstants.BUSINESS_TYPE_CODE, O2CoreConstants.BusinessType.B2C);

        if (StringUtils.isNotBlank(tenantIdParam)) {
            data = Collections.singletonList(Long.valueOf(tenantIdParam));
        }
        List<String> language = this.getLang(threadJobPojo);
        for (Long tenantId : data) {
            // 构建参数
            final PublicLovVO publicLovVO = new PublicLovVO();
            publicLovVO.setTenantId(tenantId);
            publicLovVO.setLovCode(MetadataConstants.PublicLov.PUB_LOV_CODE);
            for (String lang : language) {
                publicLovVO.setLang(lang);
                try {
                    o2PublicLovService.createPublicLovFile(publicLovVO, idpLovOwner, businessTypeCode);
                } catch (Exception ex) {
                    log.error("public lov refresh failed, param: {}", JsonHelper.objectToString(publicLovVO), ex);
                }

            }
        }
    }

}
