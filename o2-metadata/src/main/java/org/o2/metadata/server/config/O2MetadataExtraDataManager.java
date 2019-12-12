package org.o2.metadata.server.config;

import io.choerodon.core.swagger.ChoerodonRouteData;
import io.choerodon.swagger.annotation.ChoerodonExtraData;
import io.choerodon.swagger.swagger.extra.ExtraData;
import io.choerodon.swagger.swagger.extra.ExtraDataManager;
import org.o2.core.resource.route.O2RouteProperties;

/**
 * @author mark.bao@hand-china.com 2019-09-10
 */
@ChoerodonExtraData
public class O2MetadataExtraDataManager implements ExtraDataManager {
    private final O2RouteProperties o2RouteProperties;

    public O2MetadataExtraDataManager(O2RouteProperties o2RouteProperties) {
        this.o2RouteProperties = o2RouteProperties;
    }

    @Override
    public ExtraData getData() {
        ChoerodonRouteData choerodonRouteData = new ChoerodonRouteData();
        choerodonRouteData.setName(o2RouteProperties.getName());
        choerodonRouteData.setPath(o2RouteProperties.getPath());
        choerodonRouteData.setServiceId(o2RouteProperties.getServiceId());
        extraData.put(ExtraData.ZUUL_ROUTE_DATA, choerodonRouteData);
        return extraData;
    }
}
