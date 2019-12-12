package org.o2.metadata.console.config;

import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * @author youlong.peng@2019年12月12日11:26:50
 */
@ComponentScan({
        "org.o2.metadata.console.config",
        "org.o2.metadata.console.api",
        "org.o2.metadata.console.app"
})
@Configuration
@EnableDubbo(scanBasePackages = "org.o2.metadata.console.api.rpc")
public class MetadataConsoleAutoConfiguration {


}

