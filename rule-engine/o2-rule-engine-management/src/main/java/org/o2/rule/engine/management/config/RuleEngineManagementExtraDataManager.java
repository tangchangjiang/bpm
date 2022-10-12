package org.o2.rule.engine.management.config;

import io.choerodon.core.swagger.ChoerodonRouteData;
import io.choerodon.swagger.annotation.ChoerodonExtraData;
import io.choerodon.swagger.swagger.extra.ExtraData;
import io.choerodon.swagger.swagger.extra.ExtraDataManager;
import org.o2.core.common.O2Service;
import org.springframework.core.env.Environment;

/**
 * 规则引擎服务信息配置
 *
 * @author wei.cai@hand-china.com
 * @date 2022/10/09
 */
@ChoerodonExtraData
public class RuleEngineManagementExtraDataManager implements ExtraDataManager {

    private final Environment environment;

    /**
     * 构造方法
     * @param environment 环境信息
     */
    public RuleEngineManagementExtraDataManager(final Environment environment) {
        this.environment = environment;
    }

    @Override
    public ExtraData getData() {
        ChoerodonRouteData choerodonRouteData = new ChoerodonRouteData();
        choerodonRouteData.setName(this.environment.getProperty("o2.service.current.name", O2Service.RuleEngineManagement.CODE));
        choerodonRouteData.setPath(this.environment.getProperty("o2.service.current.path", O2Service.RuleEngineManagement.PATH));
        choerodonRouteData.setServiceId(this.environment.getProperty("o2.service.current.service-name", O2Service.RuleEngineManagement.ID));
        choerodonRouteData.setPackages("org.o2.rule.engine.management.api");
        extraData.put(ExtraData.ZUUL_ROUTE_DATA, choerodonRouteData);
        return extraData;
    }
}
