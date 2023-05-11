package org.o2.metadata.queue.client.config;

import org.o2.metadata.queue.client.O2MetadataProducer;
import org.o2.queue.app.service.ProducerService;
import org.o2.queue.config.QueueAutoConfiguration;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.validation.Validator;

/**
 * 元数据队列config
 *
 * @author chao.yang05@hand-china.com 2023-05-10
 */
@Configuration
@AutoConfigureAfter(value = {QueueAutoConfiguration.class})
@ConditionalOnProperty(
        prefix = "o2.queue",
        name = {"enabled"},
        matchIfMissing = true
)
public class O2MetadataProducerAutoConfiguration {

    @Bean
    @ConditionalOnBean({ProducerService.class, Validator.class})
    public O2MetadataProducer o2MetadataProducer(ProducerService producerService, Validator validator) {
        return new O2MetadataProducer(producerService, validator);
    }
}
