package org.o2.feignclient.metadata.infra.constants;

/**
 * @author lei.tang02@hand-china.com 2020/8/27
 */
public interface O2Service {

    interface Metadata {
        String NAME = "${o2.service.metadata.name:o2-metadata-18092}";
    }
}
