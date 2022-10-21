package org.o2.rule.engine.management.config;

import org.hzero.core.message.MessageAccessor;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import springfox.documentation.service.Tag;
import springfox.documentation.spring.web.plugins.Docket;

/**
 * 规则引擎服务自动启动配置
 *
 * @author wei.cai@hand-china.com
 * @date 2022/10/09
 */
@Configuration
@ComponentScan({
        "org.o2.rule.engine.management.api",
        "org.o2.rule.engine.management.app",
        "org.o2.rule.engine.management.domain",
        "org.o2.rule.engine.management.infra"
})
public class RuleEngineManagerAutoConfiguration implements InitializingBean {

    /**
     * 构造器
     * @param docket swagger
     */
    public RuleEngineManagerAutoConfiguration(final Docket docket) {
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        MessageAccessor.addBasenames("classpath:messages/o2re_management");
    }

    @Bean
    @ConditionalOnMissingBean
    public RuleEngineManagementExtraDataManager ruleEngineManagementExtraDataManager(final Environment environment) {
        return new RuleEngineManagementExtraDataManager(environment);
    }
}
