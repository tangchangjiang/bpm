package org.o2.feignclient.metadata.config;

import org.o2.feignclient.O2MetadataManagementClient;
import org.o2.feignclient.metadata.infra.feign.*;
import org.o2.feignclient.metadata.infra.feign.fallback.*;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;

/**
 * @author lei.tang02@hand-china.com 2020/8/27
 */
@EnableFeignClients(
        basePackageClasses = {SysParameterRemoteService.class, WarehouseRemoteService.class, OnlineShopRelWarehouseRemoteService.class}
)
public class MetadataFeignClientAutoConfiguration {
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
    @ConditionalOnMissingBean
    public CatalogVersionRemoteServiceImpl catalogVersionRemoteServiceFallback() {
        return new CatalogVersionRemoteServiceImpl();
    }

    @Bean
    @ConditionalOnMissingBean
    public CarrierRemoteServiceImpl carrierRemoteServiceFallback() {
        return new CarrierRemoteServiceImpl();
    }

    @Bean
    @ConditionalOnMissingBean
    O2MetadataManagementClient o2MetadataManagementClient(SysParameterRemoteService sysParameterRemoteService,
                                                          WarehouseRemoteService warehouseRemoteService,
                                                          OnlineShopRelWarehouseRemoteService onlineShopRelWarehouseRemoteService,
                                                          FreightRemoteService freightRemoteService,
                                                          CatalogVersionRemoteService catalogVersionRemoteService,
                                                          CarrierRemoteService carrierRemoteService) {
        return new O2MetadataManagementClient(sysParameterRemoteService,
                warehouseRemoteService,
                onlineShopRelWarehouseRemoteService,
                freightRemoteService,
                catalogVersionRemoteService,
                carrierRemoteService);
    }
}