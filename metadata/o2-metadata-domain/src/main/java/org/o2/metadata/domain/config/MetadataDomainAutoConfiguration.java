package org.o2.metadata.domain.config;

import org.hzero.core.message.MessageAccessor;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * @author lei.tang02@hand-china.com 2019-09-16
 */
@ComponentScan({
        "org.o2.metadata.domain.config",
        "org.o2.metadata.domain.systemparameter",
        "org.o2.metadata.domain.warehouse",
        "org.o2.metadata.domain.onlineshop",
        "org.o2.metadata.domain.carrier"
})
@Configuration
public class MetadataDomainAutoConfiguration implements InitializingBean {

    @Override
    public void afterPropertiesSet() throws Exception {
        MessageAccessor.addBasenames("classpath:messages/o2md_domain");
    }
}

