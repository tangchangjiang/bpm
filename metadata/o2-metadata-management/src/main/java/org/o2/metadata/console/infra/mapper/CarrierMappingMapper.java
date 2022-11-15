package org.o2.metadata.console.infra.mapper;

import io.choerodon.mybatis.common.BaseMapper;
import org.o2.metadata.console.api.dto.CarrierMappingQueryDTO;
import org.o2.metadata.console.api.dto.CarrierMappingQueryInnerDTO;
import org.o2.metadata.console.infra.entity.CarrierMapping;

import java.util.List;

/**
 * 承运商匹配表Mapper
 *
 * @author tingting.wang@hand-china.com 2019-3-25
 */
public interface CarrierMappingMapper extends BaseMapper<CarrierMapping> {

    /**
     * 获取承运商匹配
     *
     * @param carrierMappingQueryDTO 承运商
     * @return 承运商匹配表
     */
    List<CarrierMapping> listCarrierMappingByCondition(CarrierMappingQueryDTO carrierMappingQueryDTO);

    /**
     * 获取承运商匹配
     *
     * @param queryInnerDTO 承运商
     * @return 承运商匹配表
     */
    List<CarrierMapping> listCarrierMappings(CarrierMappingQueryInnerDTO queryInnerDTO);

    List<CarrierMapping> listByCondition(CarrierMapping carrierMapping);
}
