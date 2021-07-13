package org.o2.feignclient.metadata.config;

import org.o2.feignclient.O2MetadataManagementClient;
import org.o2.feignclient.metadata.infra.feign.SysParameterRemoteService;
import org.o2.feignclient.metadata.infra.feign.fallback.SysParameterRemoteServiceImpl;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;

/**
 * @author lei.tang02@hand-china.com 2020/8/27
 */
@EnableFeignClients(
        basePackageClasses = {SysParameterRemoteService.class}
)
public class MetadataFeignClientConfiguration {
    @Bean
    @ConditionalOnMissingBean
    public SysParameterRemoteServiceImpl sysParameterRemoteServiceFallback() {
        return new SysParameterRemoteServiceImpl();
    }



    @Bean
    @ConditionalOnMissingBean
    O2MetadataManagementClient o2MetadataManagementClient(SysParameterRemoteService sysParameterRemoteService) {
        return new O2MetadataManagementClient(sysParameterRemoteService, warehouseRemoteService);
    }
}
