package org.o2.metadata.console.autoconfig;

import org.o2.metadata.console.infra.strategy.BusinessTypeStrategyDispatcher;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MetadataManagementBusinessConfiguration implements InitializingBean {
    @Override
    public void afterPropertiesSet() throws Exception {
        BusinessTypeStrategyDispatcher.init();
    }
}
