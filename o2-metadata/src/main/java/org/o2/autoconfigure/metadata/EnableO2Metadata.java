package org.o2.autoconfigure.metadata;

import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * @author mark.bao@hand-china.com 2019/11/30
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@Import({MetadataAutoConfiguration.class})
public @interface EnableO2Metadata {
}
