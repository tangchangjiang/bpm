package org.o2.business.process.config;

import org.o2.business.process.exception.DefaultProcessErrorHandler;
import org.o2.business.process.infra.BusinessProcessRemoteService;
import org.o2.business.process.infra.fallback.BusinessProcessRemoteServiceImpl;
import org.o2.process.domain.engine.BusinessProcessExecParam;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;

/**
 * @author lei.tang02@hand-china.com 2020/8/27
 */
@EnableFeignClients(
        basePackageClasses = {BusinessProcessRemoteService.class, DefaultProcessErrorHandler.class}
)
public class BusinessFeignClientConfiguration {
    @Bean
    @ConditionalOnMissingBean
    public BusinessProcessRemoteServiceImpl businessProcessRemoteService() {
        return new BusinessProcessRemoteServiceImpl();
    }

    @Bean
    @ConditionalOnMissingBean
    public <T extends BusinessProcessExecParam> DefaultProcessErrorHandler<T> defaultProcessErrorHandler() {
        return new DefaultProcessErrorHandler<T>();
    }
}
