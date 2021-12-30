package org.o2.metadata.pipeline.config;

import io.choerodon.core.swagger.ChoerodonRouteData;
import io.choerodon.swagger.annotation.ChoerodonExtraData;
import io.choerodon.swagger.swagger.extra.ExtraData;
import io.choerodon.swagger.swagger.extra.ExtraDataManager;
import org.o2.core.common.O2Service;
import org.springframework.core.env.Environment;

@ChoerodonExtraData
public class PipelineManagementExtraDataManager implements ExtraDataManager {

    private final Environment environment;

    public PipelineManagementExtraDataManager(final Environment environment){
        this.environment=environment;
    }

    @Override
    public ExtraData getData() {
        ChoerodonRouteData choerodonRouteData = new ChoerodonRouteData();
        choerodonRouteData.setName(this.environment.getProperty("o2.service.current.name", O2Service.MetadataManagement.CODE));
        choerodonRouteData.setPath(this.environment.getProperty("o2.service.current.path", O2Service.MetadataManagement.PATH));
        choerodonRouteData.setServiceId(this.environment.getProperty("o2.service.current.service-name", O2Service.MetadataManagement.NAME));
        choerodonRouteData.setPackages("org.o2.metadata.pipeline");
        extraData.put(ExtraData.ZUUL_ROUTE_DATA, choerodonRouteData);
        return extraData;
    }
}
