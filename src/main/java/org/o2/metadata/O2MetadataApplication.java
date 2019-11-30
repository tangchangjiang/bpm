package org.o2.metadata;

import org.o2.autoconfigure.metadata.EnableO2Metadata;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * @author mark.bao@hand-china.com 2019-09-10
 */
@EnableO2Metadata
@EnableDiscoveryClient
@SpringBootApplication
public class O2MetadataApplication {

    public static void main(final String... args) {
        SpringApplication.run(O2MetadataApplication.class, args);
    }
}
