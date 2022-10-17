package org.o2.rule.engine.client.config;

import org.hzero.core.HZeroAutoConfiguration;
import org.hzero.core.message.MessageAccessor;
import org.o2.rule.engine.client.RuleEngineClient;
import org.o2.rule.engine.client.RuleEngineClientImpl;
import org.o2.rule.engine.client.app.service.RuleEngineService;
import org.o2.rule.engine.client.app.service.RuleObjectService;
import org.o2.rule.engine.client.app.service.impl.RuleEngineServiceImpl;
import org.o2.rule.engine.client.app.service.impl.RuleObjectServiceImpl;
import org.o2.rule.engine.client.domain.repository.RuleRepository;
import org.o2.rule.engine.client.infra.feign.O2RuleRemoteService;
import org.o2.rule.engine.client.infra.feign.fallback.O2RuleRemoteServiceImpl;
import org.o2.rule.engine.client.infra.repository.impl.RuleRepositoryImpl;
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
        basePackageClasses = {O2RuleRemoteService.class}
)
public class RuleEngineClientAutoConfiguration implements InitializingBean {

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
    public O2RuleRemoteService o2RuleRemoteServiceFallback() {
        return new O2RuleRemoteServiceImpl();
    }

    /**
     * 规则仓库
     *
     * @param o2RuleRemoteService 远程调用
     * @return 规则仓库
     */
    @Bean
    @ConditionalOnMissingBean
    public RuleRepository ruleRepository(O2RuleRemoteService o2RuleRemoteService) {
        return new RuleRepositoryImpl(o2RuleRemoteService);
    }

    /**
     * 规则对象Service
     *
     * @return 规则对象Service
     */
    @Bean
    @ConditionalOnMissingBean
    public RuleObjectService ruleObjectService() {
        return new RuleObjectServiceImpl();
    }

    /**
     * 规则引擎Service
     *
     * @param ruleRepository    ruleRepository
     * @param ruleObjectService ruleObjectService
     * @return 规则引擎Service
     */
    @Bean
    @ConditionalOnMissingBean
    public RuleEngineService o2RuleEngineService(final RuleRepository ruleRepository,
                                                 final RuleObjectService ruleObjectService) {
        return new RuleEngineServiceImpl(ruleRepository, ruleObjectService);
    }

    /**
     * 规则引擎客户端构造方法
     *
     * @param ruleEngineService 规则引擎Service
     * @return 规则引擎客户端
     */
    @Bean
    @ConditionalOnMissingBean
    public RuleEngineClient o2RuleEngineClient(final RuleEngineService ruleEngineService) {
        return new RuleEngineClientImpl(ruleEngineService);
    }

}
