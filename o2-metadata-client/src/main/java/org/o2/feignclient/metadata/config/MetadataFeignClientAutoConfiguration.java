package org.o2.feignclient.metadata.config;

import org.o2.feignclient.O2MetadataClient;
import org.o2.feignclient.metadata.infra.feign.FreightRemoteService;
import org.o2.feignclient.metadata.infra.feign.OnlineShopRemoteService;
import org.o2.feignclient.metadata.infra.feign.SysParameterRemoteService;
import org.o2.feignclient.metadata.infra.feign.WarehouseRemoteService;
import org.o2.feignclient.metadata.infra.feign.fallback.FreightRemoteServiceImpl;
import org.o2.feignclient.metadata.infra.feign.fallback.OnlineShopRemoteServiceImpl;
import org.o2.feignclient.metadata.infra.feign.fallback.SysParameterRemoteServiceImpl;
import org.o2.feignclient.metadata.infra.feign.fallback.WarehouseRemoteServiceImpl;
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
                OnlineShopRemoteService.class}
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
    O2MetadataClient o2MetadataClient(SysParameterRemoteService sysParameterRemoteService,
                                      WarehouseRemoteService warehouseRemoteService,
                                      FreightRemoteService freightRemoteService,
                                      OnlineShopRemoteService onlineShopRemoteService) {
        return new O2MetadataClient(sysParameterRemoteService, warehouseRemoteService, freightRemoteService, onlineShopRemoteService);
    }
}
