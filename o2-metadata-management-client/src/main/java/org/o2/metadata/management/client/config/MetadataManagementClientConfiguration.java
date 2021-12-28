package org.o2.metadata.management.client.config;

import org.o2.metadata.management.client.*;
import org.o2.metadata.management.client.infra.feign.*;
import org.o2.metadata.management.client.infra.feign.fallback.*;
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
                PlatformRemoteService.class,
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
    public PlatformRemoteServiceImpl platformInfMappingRemoteServiceFallback() {
        return new PlatformRemoteServiceImpl();
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
    public AddressClient addressClient(AddressMappingRemoteService addressMappingService) { return new AddressClient( addressMappingService);
    }

    @Bean
    @ConditionalOnMissingBean
    public SystemParameterClient systemParameterClient(SysParameterRemoteService sysParameterRemoteService) { return new SystemParameterClient( sysParameterRemoteService);
    }

    @Bean
    @ConditionalOnMissingBean
    public WarehouseClient warehousesClient(WarehouseRemoteService warehouseRemoteService) { return new WarehouseClient( warehouseRemoteService);
    }

    @Bean
    @ConditionalOnMissingBean
    public FreightClient freightClient(FreightRemoteService freightRemoteService) { return new FreightClient( freightRemoteService);
    }

    @Bean
    @ConditionalOnMissingBean
    public OnlineShopClient onlineShopClient(OnlineShopRemoteService onlineShopRemoteService) { return new OnlineShopClient( onlineShopRemoteService);
    }

    @Bean
    @ConditionalOnMissingBean
    public CarrierClient carrierClient(CarrierRemoteService carrierRemoteService) { return new CarrierClient( carrierRemoteService);
    }

    @Bean
    @ConditionalOnMissingBean
    public CatalogClient catalogClient(CatalogVersionRemoteService catalogVersionRemoteService) { return new CatalogClient( catalogVersionRemoteService);
    }

    @Bean
    @ConditionalOnMissingBean
    public PosClient posClient(PosRemoteService posRemoteService) { return new PosClient( posRemoteService);
    }

    @Bean
    @ConditionalOnMissingBean
    public PlatformClient platformClient(PlatformRemoteService platformRemoteService) { return new PlatformClient( platformRemoteService);
    }

    @Bean
    @ConditionalOnMissingBean
    public StaticResourceClient staticResourceClient(StaticResourceRemoteService staticResourceRemoteService) { return new StaticResourceClient( staticResourceRemoteService);
    }

}
