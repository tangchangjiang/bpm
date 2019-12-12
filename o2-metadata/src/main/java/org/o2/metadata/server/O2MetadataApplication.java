package org.o2.metadata.server;

import org.o2.autoconfigure.metadata.EnableO2Metadata;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;


/**
 * 元数据服务
 * @author jiu.yang@hand-china.com
 * @date 2019/12/11 21:22
 */

@EnableO2Metadata
@EnableDiscoveryClient
@SpringBootApplication
public class O2MetadataApplication {

    public static void main(final String... args) {
        SpringApplication.run(O2MetadataApplication.class, args);
    }
}

