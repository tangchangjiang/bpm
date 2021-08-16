package org.o2.metadata.console.app.service.impl;

import io.choerodon.core.exception.CommonException;
import org.o2.metadata.console.app.service.CarrierMappingService;
import org.o2.metadata.console.infra.constant.CarrierConstants;
import org.o2.metadata.console.infra.entity.CarrierMapping;
import org.o2.metadata.console.infra.entity.Platform;
import org.o2.metadata.console.infra.mapper.PlatformMapper;
import org.o2.metadata.console.infra.repository.CarrierMappingRepository;
import org.springframework.stereotype.Service;


/**
 * 承运商匹配表应用服务默认实现
 *
 * @author tingting.wang@hand-china.com 2019-3-25
 */
@Service
public class CarrierMappingServiceImpl implements CarrierMappingService {

    private PlatformMapper platformMapper;

    private final CarrierMappingRepository carrierMappingRepository;

    public CarrierMappingServiceImpl(PlatformMapper platformMapper,
                                     final CarrierMappingRepository carrierMappingRepository) {
        this.platformMapper = platformMapper;
        this.carrierMappingRepository = carrierMappingRepository;
    }

    @Override
    public void insertCarrierMapping(Long organizationId, CarrierMapping carrierMapping) {

        carrierMapping.setTenantId(organizationId);
        // 非空字段校验
        carrierMapping.baseValidate();
        Platform query = new Platform();
        query.setPlatformCode(carrierMapping.getPlatformCode());
        query.setTenantId(carrierMapping.getTenantId());
        Platform platform = platformMapper.selectOne(query);
        if (null == platform) {
            throw new CommonException(CarrierConstants.ErrorCode.O2MD_ERROR_PLATFORM_NOT_EXISTS);
        }
        // 平台和承运商编码是否重复
        carrierMapping.setPlatformCode(platform.getPlatformCode());
        carrierMapping.setCarrierId(carrierMapping.getCarrierId());
        carrierMapping.setCarrierCode(carrierMapping.getCarrierCode());
        final boolean exist = carrierMapping.exist(carrierMappingRepository);
        if (exist) {
            throw new CommonException(CarrierConstants.ErrorCode.O2MD_ERROR_PLATFORM_CODE_DUPLICATE);
        }
        carrierMappingRepository.insertSelective(carrierMapping);
    }
}
