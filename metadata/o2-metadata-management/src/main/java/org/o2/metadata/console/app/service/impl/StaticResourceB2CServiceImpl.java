package org.o2.metadata.console.app.service.impl;

import io.choerodon.core.exception.CommonException;
import org.o2.cms.management.client.O2CmsManagementClient;
import org.o2.core.O2CoreConstants;
import org.o2.metadata.console.infra.constant.MetadataConstants;
import org.o2.metadata.console.infra.convertor.StaticResourceConverter;
import org.o2.metadata.domain.staticresource.domain.StaticResourceConfigDO;
import org.o2.metadata.domain.staticresource.domain.StaticResourceSaveDO;
import org.o2.metadata.domain.staticresource.service.StaticResourceBusinessService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StaticResourceB2CServiceImpl implements StaticResourceBusinessService {
    private final O2CmsManagementClient cmsManagementClient;
    public StaticResourceB2CServiceImpl(O2CmsManagementClient cmsManagementClient){
        this.cmsManagementClient = cmsManagementClient;
    }

    @Override
    public void saveStaticResource(Long tenantId, List<StaticResourceSaveDO> staticResourceSaveDOList) {
        try {
            cmsManagementClient.saveResource(tenantId, StaticResourceConverter.toB2CStaticResourceSaveDTOList(staticResourceSaveDOList));
        }catch (Exception e){
            throw new CommonException(MetadataConstants.ErrorCode.SAVE_STATIC_RESOURCE_FAIL);
        }
    }

    @Override
    public StaticResourceConfigDO getStaticResourceConfig(Long tenantId, String resourceCode) {
        StaticResourceConfigDO staticResourceConfigDO;
        try {
            staticResourceConfigDO = StaticResourceConverter.toStaticResourceConfigDO(cmsManagementClient.getStaticResourceConfig(tenantId, resourceCode));
        }catch (Exception e){
            throw new CommonException(MetadataConstants.ErrorCode.QUERY_STATIC_RESOURCE_CONFIG_FAIL);
        }
        return staticResourceConfigDO;
    }

    @Override
    public String getBusinessTypeCode() {
        return O2CoreConstants.BusinessType.B2C;
    }
}
