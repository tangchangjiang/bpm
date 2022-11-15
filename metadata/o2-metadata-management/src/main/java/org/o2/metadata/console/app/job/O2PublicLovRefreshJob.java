package org.o2.metadata.console.app.job;

import lombok.extern.slf4j.Slf4j;
import org.hzero.boot.scheduler.infra.annotation.JobHandler;
import org.hzero.boot.scheduler.infra.enums.ReturnT;
import org.hzero.boot.scheduler.infra.handler.IJobHandler;
import org.hzero.boot.scheduler.infra.tool.SchedulerTool;
import org.o2.core.O2CoreConstants;
import org.o2.metadata.console.api.vo.PublicLovVO;
import org.o2.metadata.console.app.service.O2PublicLovService;
import org.o2.metadata.console.infra.constant.MetadataConstants;
import org.springframework.util.StringUtils;

import java.util.Map;

/**
 * O2MD.PUBLIC_LOV  值集文件缓存刷新Job
 *
 * @author: kang.yang@hand-china.com 2021-08-20 14:17
 **/
@Slf4j
@JobHandler(value = "o2PublicLovRefreshJob")
public class O2PublicLovRefreshJob implements IJobHandler {

    private final O2PublicLovService o2PublicLovService;

    public O2PublicLovRefreshJob(O2PublicLovService o2PublicLovService) {
        this.o2PublicLovService = o2PublicLovService;
    }

    @Override
    public ReturnT execute(Map<String, String> map, SchedulerTool tool) {
        final String tenantId = map.get(MetadataConstants.RefreshJobConstants.TENANT_ID);
        final String lovCode = map.get(MetadataConstants.RefreshJobConstants.LOV_CODE);
        final String idpLovOwner = map.get(MetadataConstants.RefreshJobConstants.IDP_LOV_OWNER);
        final String businessTypeCode = map.getOrDefault(MetadataConstants.RefreshJobConstants.BUSINESS_TYPE_CODE, O2CoreConstants.BusinessType.B2C);

        if (!StringUtils.hasText(tenantId)) {
            tool.error("Parameter [tenantId] can't be null.Please check job configuration.");
            return ReturnT.FAILURE;
        }
        final PublicLovVO vo = new PublicLovVO();
        vo.setTenantId(Long.parseLong(tenantId));
        vo.setLovCode(lovCode);
        o2PublicLovService.createPublicLovFile(vo, idpLovOwner, businessTypeCode);

        return ReturnT.SUCCESS;
    }

}
