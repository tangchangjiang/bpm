package org.o2.rule.engine.client.config;

import org.hzero.core.message.MessageAccessor;
import org.o2.rule.engine.client.RuleEngineClient;
import org.o2.rule.engine.client.RuleEngineClientImpl;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 规则引擎客户端自动启动配置
 *
 * @author wei.cai@hand-china.com
 * @date 2022/10/09
 */
@Configuration
public class RuleEngineClientAutoConfiguration implements InitializingBean {

    @Override
    public void afterPropertiesSet() throws Exception {
        MessageAccessor.addBasenames("classpath:messages/o2re_client");
    }

    /**
     * 规则引擎客户端构造方法
     *
     * @return 规则引擎客户端
     */
    @Bean
    @ConditionalOnMissingBean
    public RuleEngineClient ruleEngineClient() {
        return new RuleEngineClientImpl();
    }

}
