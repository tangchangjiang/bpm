package org.o2.autoconfigure.metadata;

import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.o2.core.resource.annotation.EnableO2Server;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * @author mark.bao@hand-china.com 2019/11/30
 */
@EnableO2Server
@ComponentScan({
        "org.o2.metadata.api",
        "org.o2.metadata.app",
        "org.o2.metadata.config",
        "org.o2.metadata.domain",
        "org.o2.metadata.infra"
})
@EnableDubbo(scanBasePackages = "org.o2.metadata.api.rpc")
@Configuration
public class MetadataAutoConfiguration {
}
