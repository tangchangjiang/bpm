package org.o2.business.process.management.config;

import org.hzero.core.message.MessageAccessor;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import springfox.documentation.service.Tag;
import springfox.documentation.spring.web.plugins.Docket;

/**
 * @author wenjun.deng01@hand-china.com 2019-01-29
 */
@Configuration
@ComponentScan({
        "org.o2.business.process.management.api",
        "org.o2.business.process.management.app",
        "org.o2.business.process.management.domain",
        "org.o2.business.process.management.infra"
})
public class BusinessProcessManagerAutoConfiguration implements InitializingBean {
    public static final String BUSINESS_PROCESS = "BUSINESS_PROCESS";
    public static final String BUSINESS_PROCESS_NODE = "BUSINESS_PROCESS_NODE";
    public static final String BUSINESS_PROCESS_NODE_SITE = "BUSINESS_PROCESS_NODE_SITE";
    public static final String BUSINESS_PROCESS_NODE_PARAMETER = "BUSINESS_PROCESS_NODE_PARAMETER";
    public static final String BUSINESS_PROCESS_NODE_PARAMETER_SITE = "BUSINESS_PROCESS_NODE_PARAMETER_SITE";

    public BusinessProcessManagerAutoConfiguration(final Docket docket) {
        docket.tags(new Tag(BUSINESS_PROCESS, "业务流程定义"))
                .tags(new Tag(BUSINESS_PROCESS_NODE, "业务流程节点定义"))
                .tags(new Tag(BUSINESS_PROCESS_NODE_SITE, "业务流程节点定义（站点层）"))
                .tags(new Tag(BUSINESS_PROCESS_NODE_PARAMETER, "业务流程节点参数定义"))
                .tags(new Tag(BUSINESS_PROCESS_NODE_PARAMETER_SITE, "业务流程节点参数定义(站点层)"));
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        MessageAccessor.addBasenames("classpath:messages/bpm-management");
    }

    @Bean
    public BusinessProcessManagementExtraDataManager businessProcessManagementExtraDataManager(final Environment environment) {
        return new BusinessProcessManagementExtraDataManager(environment);
    }
}
