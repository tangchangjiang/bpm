package org.o2.metadata.pipeline.infra.constants;

/**
 * @author lei.tang02@hand-china.com 2020/8/27
 */
public interface O2Service {

    interface Pipeline {
        String NAME = "${o2.service.pipeline.name:o2-metadata-management}";
    }
}
