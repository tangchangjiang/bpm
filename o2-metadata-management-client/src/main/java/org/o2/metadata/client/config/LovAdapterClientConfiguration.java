package org.o2.metadata.client.config;

import org.o2.metadata.client.*;
import org.o2.metadata.client.infra.feign.IamUserRemoteService;
import org.o2.metadata.client.infra.feign.LovAdapterRemoteService;
import org.o2.metadata.client.infra.feign.fallback.IamUserRemoteServiceImpl;
import org.o2.metadata.client.infra.feign.fallback.LovAdapterRemoteServiceImpl;
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
               LovAdapterRemoteService.class, IamUserRemoteService.class
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
    public IamUserRemoteServiceImpl iamUserRemoteServiceFallback() {
        return new IamUserRemoteServiceImpl();
    }

    @Bean
    @ConditionalOnMissingBean
    public CurrencyLovClient o2CurrencyLovClient(LovAdapterRemoteService lovAdapterRemoteService) {
        return new CurrencyLovClient(lovAdapterRemoteService);
    }
    @Bean
    @ConditionalOnMissingBean
    public UomLovClient o2UomLovClient(LovAdapterRemoteService lovAdapterRemoteService) {
        return new UomLovClient(lovAdapterRemoteService);
    }
    @Bean
    @ConditionalOnMissingBean
    public UomTypeLovClient o2UomTypeLovClient(LovAdapterRemoteService lovAdapterRemoteService) {
        return new UomTypeLovClient(lovAdapterRemoteService);
    }

    @Bean
    @ConditionalOnMissingBean
    public IDPLovClient o2DuLiLovClient(LovAdapterRemoteService lovAdapterRemoteService) {
        return new IDPLovClient(lovAdapterRemoteService);
    }
    @Bean
    @ConditionalOnMissingBean
    public SQLLovClient sqlLovClient(LovAdapterRemoteService lovAdapterRemoteService) {
        return new SQLLovClient(lovAdapterRemoteService);
    }
    @Bean
    @ConditionalOnMissingBean
    public RegionLovClient o2RegionLovClient(LovAdapterRemoteService lovAdapterRemoteService) {
        return new RegionLovClient(lovAdapterRemoteService);
    }

    @Bean
    @ConditionalOnMissingBean
    public IamUserClient iamUserClient(IamUserRemoteService iamUserRemoteService) {
        return new IamUserClient(iamUserRemoteService);
    }


}
