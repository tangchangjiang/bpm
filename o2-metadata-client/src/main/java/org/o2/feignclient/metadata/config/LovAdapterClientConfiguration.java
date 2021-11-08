package org.o2.feignclient.metadata.config;

import org.o2.feignclient.CurrencyLovClient;
import org.o2.feignclient.O2LovAdapterClient;
import org.o2.feignclient.RegionLovClient;
import org.o2.feignclient.UomLovClient;
import org.o2.feignclient.metadata.infra.feign.LovAdapterRemoteService;
import org.o2.feignclient.metadata.infra.feign.fallback.LovAdapterRemoteServiceImpl;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 *
 * 值集
 *
 * @author yipeng.zhu@hand-china.com 2021-08-30
 **/
@Configuration
@EnableFeignClients(
        basePackageClasses = {
               LovAdapterRemoteService.class
        }
)
public class LovAdapterClientConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public LovAdapterRemoteServiceImpl lovAdapterRemoteServiceFallback() {
        return new LovAdapterRemoteServiceImpl();
    }

    @Bean
    @ConditionalOnMissingBean
    public O2LovAdapterClient o2LovAdapterClient(LovAdapterRemoteService lovAdapterRemoteService) {
        return new O2LovAdapterClient(lovAdapterRemoteService);
    }

    @Bean
    @ConditionalOnMissingBean
    public CurrencyLovClient o2CurrencyLovClient(LovAdapterRemoteService lovAdapterRemoteService) {
        return new CurrencyLovClient(lovAdapterRemoteService);
    }

    @Bean
    @ConditionalOnMissingBean
    public RegionLovClient regionLovClient(LovAdapterRemoteService lovAdapterRemoteService) {
        return new RegionLovClient(lovAdapterRemoteService);
    }

    @Bean
    @ConditionalOnMissingBean
    public UomLovClient uomLovClient(LovAdapterRemoteService lovAdapterRemoteService) {
        return new UomLovClient(lovAdapterRemoteService);
    }
}
