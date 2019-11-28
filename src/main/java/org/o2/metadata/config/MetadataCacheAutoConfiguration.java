package org.o2.metadata.config;


import org.o2.data.redis.client.RedisCacheClient;
import org.o2.metadata.app.service.FreightCacheService;
import org.o2.metadata.app.service.PosCacheService;
import org.o2.metadata.app.service.SysParameterCacheService;
import org.o2.metadata.app.service.impl.FreightCacheServiceImpl;
import org.o2.metadata.app.service.impl.PosCacheServiceImpl;
import org.o2.metadata.app.service.impl.SysParameterCacheServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 元数据缓存自动装配
 *
 * @author mark.bao@hand-china.com 2019-03-25
 */
@Configuration
public class MetadataCacheAutoConfiguration {

    @Bean
    public PosCacheService posCacheService(final RedisCacheClient redisCacheClient) {
        return new PosCacheServiceImpl(redisCacheClient);
    }

    @Bean
    public SysParameterCacheService sysParameterCacheService(final RedisCacheClient redisCacheClient) {
        return new SysParameterCacheServiceImpl(redisCacheClient);
    }

    @Bean
    public FreightCacheService freightCacheService(final RedisCacheClient redisCacheClient) {
        return new FreightCacheServiceImpl(redisCacheClient);
    }
}
