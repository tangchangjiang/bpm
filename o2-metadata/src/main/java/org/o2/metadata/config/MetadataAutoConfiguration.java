package org.o2.metadata.config;

import org.apache.dubbo.config.spring.context.annotation.DubboComponentScan;
import org.o2.context.metadata.api.IPosContext;
import org.o2.context.metadata.api.ISysParameterContext;
import org.o2.data.redis.client.RedisCacheClient;
import org.o2.metadata.api.rpc.PosContextImpl;
import org.o2.metadata.api.rpc.SysParameterContextImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author mark.bao@hand-china.com 2020/1/2
 */
@DubboComponentScan("org.o2.metadata.api.rpc")
@Configuration
public class MetadataAutoConfiguration {

    @Bean
    public IPosContext posContext(final RedisCacheClient redisCacheClient) {
        return new PosContextImpl(redisCacheClient);
    }

    @Bean
    public ISysParameterContext sysParameterContext(final RedisCacheClient redisCacheClient) {
        return new SysParameterContextImpl(redisCacheClient);
    }
}
