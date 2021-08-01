package org.o2.feignclient.metadata.config;

import org.o2.feignclient.O2MetadataManagementClient;
import org.o2.feignclient.metadata.infra.feign.*;
import org.o2.feignclient.metadata.infra.feign.fallback.*;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author lei.tang02@hand-china.com 2020/8/27
 */
@Configuration
@EnableFeignClients(
        basePackageClasses = {
                SysParameterRemoteService.class,
                WarehouseRemoteService.class,
                OnlineShopRelWarehouseRemoteService.class,
                StaticResourceRemoteService.class,
                FreightRemoteService.class
        }
)
public class MetadataManagementFeignClientAutoConfiguration {
    @Bean
    @ConditionalOnMissingBean
    public SysParameterRemoteServiceImpl sysParameterRemoteServiceFallback() {
        return new SysParameterRemoteServiceImpl();
    }

    @Bean
    @ConditionalOnMissingBean
    public WarehouseRemoteServiceImpl warehouseRemoteServiceFallback() {
        return new WarehouseRemoteServiceImpl();
    }

    @Bean
    @ConditionalOnMissingBean
    public OnlineShopRelWarehouseRemoteServiceImpl onlineShopRelWarehouseRemoteServiceFallback() {
        return new OnlineShopRelWarehouseRemoteServiceImpl();
    }

    @Bean
    @ConditionalOnMissingBean
    public FreightServiceRemoteServiceImpl freightServiceRemoteServiceFallback() {
        return new FreightServiceRemoteServiceImpl();
    }

    @Bean
    public StaticResourceRemoteService staticResourceRemoteServiceFallback() {
        return new StaticResourceRemoteServiceImpl();
    }

    @Bean
    @ConditionalOnMissingBean
    O2MetadataManagementClient o2MetadataManagementClient(SysParameterRemoteService sysParameterRemoteService,
                                                          WarehouseRemoteService warehouseRemoteService,
                                                          OnlineShopRelWarehouseRemoteService onlineShopRelWarehouseRemoteService,
                                                          FreightRemoteService freightRemoteService,
                                                          StaticResourceRemoteService staticResourceRemoteService) {
        return new O2MetadataManagementClient(sysParameterRemoteService,
                warehouseRemoteService,
                onlineShopRelWarehouseRemoteService,
                freightRemoteService, staticResourceRemoteService);
    }
}
