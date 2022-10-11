package org.o2.metadata.pipeline.config;

import org.o2.metadata.pipeline.infra.PipelineRemoteService;
import org.o2.metadata.pipeline.infra.fallback.PipelineRemoteServiceImpl;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;

/**
 * @author lei.tang02@hand-china.com 2020/8/27
 */
@EnableFeignClients(
        basePackageClasses = {PipelineRemoteService.class}
)
public class PipelineFeignClientConfiguration {
    @Bean
    @ConditionalOnMissingBean
    public PipelineRemoteServiceImpl pipelineRemoteService() {
        return new PipelineRemoteServiceImpl();
    }
}
