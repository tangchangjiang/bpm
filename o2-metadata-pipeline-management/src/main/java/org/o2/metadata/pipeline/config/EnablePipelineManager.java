package org.o2.metadata.pipeline.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.service.Tag;
import springfox.documentation.spring.web.plugins.Docket;

/**
 * @author wenjun.deng01@hand-china.com 2019-01-29
 */
@Configuration
@ComponentScan({
        "org.o2.metadata.pipeline.api",
        "org.o2.metadata.pipeline.app",
        "org.o2.metadata.pipeline.config",
        "org.o2.metadata.pipeline.domain",
        "org.o2.metadata.pipeline.infra",
        "org.o2.metadata.pipeline.job"
})
public class EnablePipelineManager {
    public static final String PIPELINE = "PIPELINE";
    public static final String PIPELINE_ACTION = "PIPELINE ACTION";
    public static final String PIPELINE_NODE = "PIPELINE_NODE";
    public static final String ACTION_PARAMETER = "ACTION PARAMETER";

    @Autowired
    public EnablePipelineManager(final Docket docket) {
        docket.tags(new Tag(PIPELINE, "流水线"))
                .tags(new Tag(PIPELINE_NODE, "流水线节点"))
                .tags(new Tag(ACTION_PARAMETER, "行为参数"))
                .tags(new Tag(PIPELINE_ACTION,"行为"));
    }
}
