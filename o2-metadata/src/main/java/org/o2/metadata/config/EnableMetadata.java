package org.o2.metadata.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
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
})
public class EnableMetadata {

    public static final String SYS_PARAMETER_INTERNAL = "sys Parameter Internal";

    public static final String SYS_WAREHOUSE_INTERNAL = "warehouse Internal";

    @Autowired
    public EnableMetadata(final Docket docket) {
        docket.tags(new Tag(EnableMetadata.SYS_PARAMETER_INTERNAL, "系统参数内部接口"))
                .tags(new Tag(EnableMetadata.SYS_WAREHOUSE_INTERNAL, "库存内部接口"));
    }


}
