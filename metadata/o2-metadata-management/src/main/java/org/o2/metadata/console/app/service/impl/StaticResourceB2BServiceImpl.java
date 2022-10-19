package org.o2.metadata.console.app.service.impl;

import io.choerodon.core.exception.CommonException;
import org.o2.cms.management.client.b2b.O2CmsManagementB2BClient;
import org.o2.core.O2CoreConstants;
import org.o2.metadata.console.infra.constant.MetadataConstants;
import org.o2.metadata.console.infra.convertor.StaticResourceConverter;
import org.o2.metadata.domain.staticresource.domain.StaticResourceConfigDO;
import org.o2.metadata.domain.staticresource.domain.StaticResourceSaveDO;
import org.o2.metadata.domain.staticresource.service.StaticResourceBusinessService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StaticResourceB2BServiceImpl implements StaticResourceBusinessService {
    private final O2CmsManagementB2BClient cmsManagementB2BClient;
    public StaticResourceB2BServiceImpl(O2CmsManagementB2BClient cmsManagementB2BClient){
        this.cmsManagementB2BClient = cmsManagementB2BClient;
    }

    @Override
    public void saveStaticResource(Long tenantId, List<StaticResourceSaveDO> staticResourceSaveDOList) {
        try{
            cmsManagementB2BClient.saveResource(tenantId, StaticResourceConverter.toB2BStaticResourceSaveDTOList(staticResourceSaveDOList));
        }catch (Exception e){
            throw new CommonException(MetadataConstants.ErrorCode.SAVE_STATIC_RESOURCE_FAIL);
        }
    }

    @Override
    public StaticResourceConfigDO getStaticResourceConfig(Long tenantId, String resourceCode) {
        StaticResourceConfigDO staticResourceConfigDO;
        try{
            staticResourceConfigDO = StaticResourceConverter.toStaticResourceConfigDO(cmsManagementB2BClient.getStaticResourceConfig(tenantId, resourceCode));
        }catch (Exception e){
            throw new CommonException(MetadataConstants.ErrorCode.QUERY_STATIC_RESOURCE_CONFIG_FAIL);
        }
        return staticResourceConfigDO;
    }

    @Override
    public String getBusinessTypeCode() {
        return O2CoreConstants.BusinessType.B2B;
    }
}
