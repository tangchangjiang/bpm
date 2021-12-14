package org.o2.metadata.console.infra.config;

import io.choerodon.core.swagger.ChoerodonRouteData;
import io.choerodon.swagger.annotation.ChoerodonExtraData;
import io.choerodon.swagger.swagger.extra.ExtraData;
import io.choerodon.swagger.swagger.extra.ExtraDataManager;
import org.springframework.core.env.Environment;

@ChoerodonExtraData
public class MetadataManagementExtraDataManager implements ExtraDataManager {

    private final Environment environment;

    public MetadataManagementExtraDataManager(final Environment environment){
        this.environment=environment;
    }

    @Override
    public ExtraData getData() {
        ChoerodonRouteData choerodonRouteData = new ChoerodonRouteData();
        choerodonRouteData.setName(this.environment.getProperty("o2.service.current.name", "o2-metadata-management"));
        choerodonRouteData.setPath(this.environment.getProperty("o2.service.current.path", "/o2md-m/**"));
        choerodonRouteData.setServiceId(this.environment.getProperty("o2.service.current.service-name", "${o2.service.metadata.name:o2-metadata-management}"));
        choerodonRouteData.setPackages("org.o2.metadata.console");
        extraData.put(ExtraData.ZUUL_ROUTE_DATA, choerodonRouteData);
        return extraData;
    }
}
