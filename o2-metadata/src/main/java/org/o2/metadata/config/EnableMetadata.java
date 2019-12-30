package org.o2.metadata.config;

import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Component;
import springfox.documentation.service.Tag;
import springfox.documentation.spring.web.plugins.Docket;

/**
 * @author youlong.peng@hand-china.com 2019年12月30日10:55:40
 */
@Component
@ComponentScan({
        "org.o2.metadata.config",
        "org.o2.metadata.api"
})
@EnableDubbo(scanBasePackages = "org.o2.metadata.api.rpc")
public class EnableMetadata {

    public static final String SYS_PARAMETER = "System Parameter";
    public static final String CATALOG = "Catalog";

    @Autowired
    public EnableMetadata(final Docket docket) {
        docket.tags(new Tag(EnableMetadata.SYS_PARAMETER, "系统参数"))
                .tags(new Tag(EnableMetadata.CATALOG, "目录"));
    }
}
