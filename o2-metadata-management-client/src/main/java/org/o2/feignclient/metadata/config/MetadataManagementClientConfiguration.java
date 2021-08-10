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
                StaticResourceRemoteService.class,
                FreightRemoteService.class,
                CatalogVersionRemoteService.class,
                CarrierRemoteService.class,
                PosRemoteService.class,
                PlatformInfMappingRemoteService.class,
                OnlineShopRemoteService.class,
                AddressMappingRemoteService.class
        }
)
public class MetadataManagementClientConfiguration {
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
    public FreightServiceRemoteServiceImpl freightServiceRemoteServiceFallback() {
        return new FreightServiceRemoteServiceImpl();
    }

    @Bean
    @ConditionalOnMissingBean
    public StaticResourceRemoteServiceImpl staticResourceRemoteServiceFallback() {
        return new StaticResourceRemoteServiceImpl();
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
    public PosRemoteServiceImpl posRemoteServiceFallback() {
        return new PosRemoteServiceImpl();
    }

    @Bean
    @ConditionalOnMissingBean
    public PlatformInfMappingRemoteServiceImpl platformInfMappingRemoteServiceFallback() {
        return new PlatformInfMappingRemoteServiceImpl();
    }


    @Bean
    @ConditionalOnMissingBean
    public OnlineShopRemoteServiceImpl onlineShopRemoteServiceFallback() {
        return new OnlineShopRemoteServiceImpl();
    }

    @Bean
    @ConditionalOnMissingBean
    public AddressMappingRemoteServiceImpl addressMappingRemoteServiceFallback() {
        return new AddressMappingRemoteServiceImpl();
    }

    @Bean
    @ConditionalOnMissingBean
    public O2MetadataManagementClient o2MetadataManagementClient(SysParameterRemoteService sysParameterRemoteService,
                                                                 WarehouseRemoteService warehouseRemoteService,
                                                                 FreightRemoteService freightRemoteService,
                                                                 StaticResourceRemoteService staticResourceRemoteService,
                                                                 CatalogVersionRemoteService catalogVersionRemoteService,
                                                                 CarrierRemoteService carrierRemoteService,
                                                                 PosRemoteService posRemoteService,
                                                                 PlatformInfMappingRemoteService platformInfMappingRemoteService,
                                                                 OnlineShopRemoteService onlineShopRemoteService,
                                                                 AddressMappingRemoteService addressMappingService) {
        return new O2MetadataManagementClient(sysParameterRemoteService,
                warehouseRemoteService,
                freightRemoteService,
                staticResourceRemoteService,
                catalogVersionRemoteService,
                carrierRemoteService,
                posRemoteService,
                platformInfMappingRemoteService,
                onlineShopRemoteService,
                addressMappingService);
    }
}
