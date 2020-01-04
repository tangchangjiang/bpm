package org.o2.metadata.config;

import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * @author youlong.peng@hand-china.com 2019年12月30日10:55:40
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import(MetadataAutoConfiguration.class)
public @interface EnableMetadata {
}
