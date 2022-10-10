package org.o2.metadata.client.config;

import org.o2.metadata.client.*;
import org.o2.metadata.client.infra.feign.*;
import org.o2.metadata.client.infra.feign.fallback.*;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;

/**
 * @author lei.tang02@hand-china.com 2020/8/27
 */
@EnableFeignClients(
        basePackageClasses = {SysParameterRemoteService.class,
                WarehouseRemoteService.class,
                FreightRemoteService.class,
                OnlineShopRemoteService.class,
                CarrierRemoteService.class,
                PosRemoteService.class}
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
    public FreightRemoteServiceImpl freightServiceRemoteServiceFallback() {
        return new FreightRemoteServiceImpl();
    }


    @Bean
    @ConditionalOnMissingBean
    public OnlineShopRemoteServiceImpl onlineShopRemoteServiceFallback() {
        return new OnlineShopRemoteServiceImpl();
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
    public WarehouseClient warehouseClient(WarehouseRemoteService warehouseRemoteService) {
        return new WarehouseClient(warehouseRemoteService);
    }

    @Bean
    @ConditionalOnMissingBean
    public FreightClient freightClient(FreightRemoteService freightRemoteService) {
        return new FreightClient(freightRemoteService);
    }

    @Bean
    @ConditionalOnMissingBean
    public OnlineShopClient onlineShopClientht(OnlineShopRemoteService onlineShopRemoteService) {
        return new OnlineShopClient(onlineShopRemoteService);
    }

    @Bean
    @ConditionalOnMissingBean
    public CarrierClient carrierClient(CarrierRemoteService carrierRemoteService) {
        return new CarrierClient(carrierRemoteService);
    }

    @Bean
    @ConditionalOnMissingBean
    public SystemParameterClient systemParameterClient(SysParameterRemoteService sysParameterRemoteService) {
        return new SystemParameterClient(sysParameterRemoteService);
    }

    @Bean
    @ConditionalOnMissingBean
    public PosClient posClient(PosRemoteService posRemoteService) {
        return new PosClient(posRemoteService);
    }

}