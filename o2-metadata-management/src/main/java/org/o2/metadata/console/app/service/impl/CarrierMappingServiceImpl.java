package org.o2.metadata.console.app.service.impl;

import com.google.common.base.Preconditions;
import org.o2.metadata.console.app.service.CarrierMappingService;
import org.o2.metadata.console.infra.entity.CarrierMapping;
import org.o2.metadata.console.infra.entity.Catalog;
import org.o2.metadata.console.infra.repository.CarrierMappingRepository;
import org.o2.metadata.console.infra.mapper.CatalogMapper;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 承运商匹配表应用服务默认实现
 *
 * @author tingting.wang@hand-china.com 2019-3-25
 */
@Service
public class CarrierMappingServiceImpl implements CarrierMappingService {

    private CatalogMapper catalogMapper;

    private final CarrierMappingRepository carrierMappingRepository;

    public CarrierMappingServiceImpl(CatalogMapper catalogMapper, final CarrierMappingRepository carrierMappingRepository) {
        this.catalogMapper = catalogMapper;
        this.carrierMappingRepository = carrierMappingRepository;
    }

    @Override
    public Map<String, Object> insertAll(Long organizationId,List<CarrierMapping> carrierMappings) {
        final Map<String, Object> resultMap = new HashMap<>(1);
        int j = 0;
        for (int i = 0; i < carrierMappings.size(); i++) {
            CarrierMapping carrierMapping = carrierMappings.get(i);
            carrierMapping.setTenantId(organizationId);
            // 非空字段校验
            carrierMapping.baseValidate();
            Catalog catalog = catalogMapper.selectOne(Catalog.builder().catalogCode(carrierMapping.getCatalogCode()).tenantId(carrierMapping.getTenantId()).build());
            Preconditions.checkArgument(null != catalog, "unrecognized catalogCode:" + carrierMapping.getCatalogCode() + "or tenantId:" + carrierMapping.getTenantId());
            // 平台和承运商编码是否重复
            carrierMapping.setCatalogId(catalog.getCatalogId());
            final boolean exist = carrierMapping.exist(carrierMappingRepository);
            if (exist) {
                j++;
            } else {
                if (carrierMapping.getCarrierMappingId() != null) {
                    carrierMappingRepository.updateByPrimaryKey(carrierMapping);
                } else {
                    carrierMappingRepository.insertSelective(carrierMapping);
                }
            }
        }
        if (j > 0) {
            resultMap.put("平台和承运商编码不可重复", carrierMappings.size() - j);
        }
        return resultMap;
    }
}
