package org.o2.metadata.console.app.service.impl;

import org.o2.metadata.console.app.service.PlatformUomService;
import org.o2.metadata.console.infra.repository.PlatformUomRepository;
import org.o2.metadata.console.api.vo.PlatformUomVO;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 平台值集服务默认实现
 *
 * @author peng.xu@hand-china.com 2019-07-09
 */
@Service
public class PlatformUomServiceImpl implements PlatformUomService {

    private PlatformUomRepository platformUomRepository;

    public PlatformUomServiceImpl(PlatformUomRepository platformUomRepository) {
        this.platformUomRepository = platformUomRepository;
    }

    @Override
    public List<PlatformUomVO> getChildrenValues(String parentValue) {
        return platformUomRepository.getChildrenValues(parentValue);
    }
}
