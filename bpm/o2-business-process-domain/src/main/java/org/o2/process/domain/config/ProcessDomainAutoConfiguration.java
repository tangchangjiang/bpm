package org.o2.process.domain.config;

import org.hzero.core.message.MessageAccessor;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * @author wenjun.deng01@hand-china.com 2019-01-29
 */
@Configuration
@ComponentScan({
        "org.o2.process.domain.app",
        "org.o2.process.domain.engine",
        "org.o2.process.domain.infra"
})
public class ProcessDomainAutoConfiguration implements InitializingBean {

    @Override
    public void afterPropertiesSet() throws Exception {
        MessageAccessor.addBasenames("classpath:messages/bpm-domain");
    }
}
