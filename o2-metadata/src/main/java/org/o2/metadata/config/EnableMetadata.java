package org.o2.metadata.config;

import org.o2.context.metadata.api.ISysParameterContext;
import org.o2.context.metadata.api.IWarehouseContext;
import org.o2.context.metadata.config.MetadataContextProvider;
import org.o2.data.redis.client.RedisCacheClient;
import org.o2.metadata.api.rpc.SysParameterContextImpl;
import org.o2.metadata.api.rpc.WarehouseContextImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author mark.bao@hand-china.com 2020/1/2
 */
@Configuration
public class EnableMetadata {

    @Bean
    public ISysParameterContext sysParameterContext(final RedisCacheClient redisCacheClient,
                                                    final MetadataContextProvider metadataContextProvider) {
        final ISysParameterContext sysParameterContext = new SysParameterContextImpl(redisCacheClient);
        metadataContextProvider.sysParameterContextService().setRef(sysParameterContext);
        metadataContextProvider.sysParameterContextService().export();
        return sysParameterContext;
    }

    @Bean
    public IWarehouseContext warehouseContext(final RedisCacheClient redisCacheClient,
                                              final MetadataContextProvider metadataContextProvider) {
        final IWarehouseContext warehouseContext = new WarehouseContextImpl(redisCacheClient);
        metadataContextProvider.warehouseContextService().setRef(warehouseContext);
        metadataContextProvider.warehouseContextService().export();
        return warehouseContext;
    }

}
