package org.o2.metadata.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import springfox.documentation.service.Tag;
import springfox.documentation.spring.web.plugins.Docket;

/**
 * @author mark.bao@hand-china.com 2020/1/2
 */
@Configuration
@ComponentScan({
        "org.o2.metadata.api",
        "org.o2.metadata.app",
        "org.o2.metadata.infra",
        "org.o2.metadata.domain",
})
public class MetadataAutoConfiguration {

    public static final String SYS_PARAMETER_INTERNAL = "sys Parameter Internal";

    public static final String SYS_WAREHOUSE_INTERNAL = "warehouse Internal";

    public MetadataAutoConfiguration(final Docket docket) {
        docket.tags(new Tag(MetadataAutoConfiguration.SYS_PARAMETER_INTERNAL, "系统参数内部接口"))
                .tags(new Tag(MetadataAutoConfiguration.SYS_WAREHOUSE_INTERNAL, "仓库内部接口"));
    }

    @Bean
    public MetadataExtraDataManager metadataExtraDataManager(final Environment environment) {
        return new MetadataExtraDataManager(environment);
    }

}
