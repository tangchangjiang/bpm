package org.o2.metadata.config;

import org.o2.context.metadata.api.IPosContext;
import org.o2.context.metadata.api.ISysParameterContext;
import org.o2.context.metadata.config.MetadataContextProvider;
import org.o2.data.redis.client.RedisCacheClient;
import org.o2.metadata.api.rpc.PosContextImpl;
import org.o2.metadata.api.rpc.SysParameterContextImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author mark.bao@hand-china.com 2020/1/2
 */
@Configuration
public class EnableMetadata {

    @Bean
    public IPosContext posContext(final RedisCacheClient redisCacheClient,
                                  final MetadataContextProvider metadataContextProvider) {
        final IPosContext posContext = new PosContextImpl(redisCacheClient);
        metadataContextProvider.posContextService().setRef(posContext);
        metadataContextProvider.posContextService().export();
        return posContext;
    }

    @Bean
    public ISysParameterContext sysParameterContext(final RedisCacheClient redisCacheClient,
                                                    final MetadataContextProvider metadataContextProvider) {
        final ISysParameterContext sysParameterContext = new SysParameterContextImpl(redisCacheClient);
        metadataContextProvider.sysParameterContextService().setRef(sysParameterContext);
        metadataContextProvider.sysParameterContextService().export();
        return sysParameterContext;
    }
}
