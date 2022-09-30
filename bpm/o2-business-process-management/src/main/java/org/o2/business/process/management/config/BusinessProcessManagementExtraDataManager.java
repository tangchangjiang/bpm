package org.o2.business.process.management.config;

import io.choerodon.core.swagger.ChoerodonRouteData;
import io.choerodon.swagger.annotation.ChoerodonExtraData;
import io.choerodon.swagger.swagger.extra.ExtraData;
import io.choerodon.swagger.swagger.extra.ExtraDataManager;
import org.o2.core.common.O2Service;
import org.springframework.core.env.Environment;

/**
 * @author xuanxiao
 * @date 2022/08/10
 */
@ChoerodonExtraData
public class BusinessProcessManagementExtraDataManager implements ExtraDataManager {

    private final Environment environment;

    public BusinessProcessManagementExtraDataManager(final Environment environment) {
        this.environment = environment;
    }

    @Override
    public ExtraData getData() {
        ChoerodonRouteData choerodonRouteData = new ChoerodonRouteData();
        choerodonRouteData.setName(this.environment.getProperty("o2.service.current.name", O2Service.BusinessProcessManagement.CODE));
        choerodonRouteData.setPath(this.environment.getProperty("o2.service.current.path", O2Service.BusinessProcessManagement.PATH));
        choerodonRouteData.setServiceId(this.environment.getProperty("o2.service.current.service-name", O2Service.BusinessProcessManagement.ID));
        choerodonRouteData.setPackages("org.o2.business.process.management.api");
        extraData.put(ExtraData.ZUUL_ROUTE_DATA, choerodonRouteData);
        return extraData;
    }
}
