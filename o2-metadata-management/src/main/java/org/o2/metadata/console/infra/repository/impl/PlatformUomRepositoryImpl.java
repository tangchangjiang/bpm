package org.o2.metadata.console.infra.repository.impl;

import org.o2.metadata.console.infra.repository.PlatformUomRepository;
import org.o2.metadata.console.api.vo.PlatformUomVO;
import org.o2.metadata.console.infra.mapper.PlatformUomMapper;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * PlatformUomRepositoryImpl
 *
 * @author peng.xu@hand-china.com 2019-07-09
 */
@Component
public class PlatformUomRepositoryImpl implements PlatformUomRepository {

    private PlatformUomMapper platformUomMapper;

    public PlatformUomRepositoryImpl(PlatformUomMapper platformUomMapper) {
        this.platformUomMapper = platformUomMapper;
    }

}
