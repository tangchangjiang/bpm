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
    public static final String PIPELINE = "PIPELINE";
    public static final String PIPELINE_ACTION = "PIPELINE ACTION";
    public static final String PIPELINE_NODE = "PIPELINE_NODE";
    public static final String ACTION_PARAMETER = "ACTION PARAMETER";

    public BusinessProcessManagerAutoConfiguration(final Docket docket) {
        docket.tags(new Tag(PIPELINE, "流水线"))
                .tags(new Tag(PIPELINE_NODE, "流水线节点"))
                .tags(new Tag(ACTION_PARAMETER, "行为参数"))
                .tags(new Tag(PIPELINE_ACTION,"行为"));
    }
    @Override
    public void afterPropertiesSet() throws Exception {
        MessageAccessor.addBasenames("classpath:messages/pipeline");
    }

    @Bean
    public BusinessProcessManagementExtraDataManager pipelineManagementExtraDataManager(final Environment environment){
        return new BusinessProcessManagementExtraDataManager(environment);
    }
}
