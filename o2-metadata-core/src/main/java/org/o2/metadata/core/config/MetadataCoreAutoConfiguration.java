package org.o2.metadata.core.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * @author lei.tang02@hand-china.com 2019-09-16
 */
@ComponentScan({
        "org.o2.metadata.core.config",
        "org.o2.metadata.core.domain",
        "org.o2.metadata.core.infra"
})
@Configuration
public class MetadataCoreAutoConfiguration {


}

