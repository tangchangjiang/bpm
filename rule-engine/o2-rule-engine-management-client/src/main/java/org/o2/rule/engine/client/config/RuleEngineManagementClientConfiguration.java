package org.o2.rule.engine.client.config;

import org.hzero.core.HZeroAutoConfiguration;
import org.hzero.core.message.MessageAccessor;
import org.o2.rule.engine.client.RuleEngineManagementClient;
import org.o2.rule.engine.client.infra.feign.O2RuleManagementRemoteService;
import org.o2.rule.engine.client.infra.feign.fallback.O2RuleManagementRemoteServiceImpl;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 规则引擎客户端自动启动配置
 *
 * @author wei.cai@hand-china.com
 * @date 2022/10/09
 */
@Configuration
@AutoConfigureAfter(HZeroAutoConfiguration.class)
@EnableFeignClients(
        basePackageClasses = {O2RuleManagementRemoteService.class}
)
public class RuleEngineManagementClientConfiguration implements InitializingBean {

    @Override
    public void afterPropertiesSet() throws Exception {
        MessageAccessor.addBasenames("classpath:messages/o2re_client");
    }

    /**
     * 远程调用Service
     *
     * @return 返回信息
     */
    @Bean
    @ConditionalOnMissingBean
    public O2RuleManagementRemoteServiceImpl o2RuleManagementRemoteServiceFallback() {
        return new O2RuleManagementRemoteServiceImpl();
    }

    @Bean
    @ConditionalOnMissingBean
    public RuleEngineManagementClient ruleEngineManagementClient(final O2RuleManagementRemoteService o2RuleManagementRemoteService) {
        return new RuleEngineManagementClient(o2RuleManagementRemoteService);
    }
}
