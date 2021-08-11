package org.o2.metadata.console.infra.repository;

import org.hzero.mybatis.base.BaseRepository;
import org.o2.metadata.console.api.dto.CarrierMappingQueryDTO;
import org.o2.metadata.console.infra.entity.CarrierMapping;

import java.util.List;

/**
 * 承运商匹配表资源库
 *
 * @author tingting.wang@hand-china.com 2019-3-25
 */
public interface CarrierMappingRepository extends BaseRepository<CarrierMapping> {

    /**
     * 获取承运商匹配表
     *
     * @param carrierMappingQueryDTO  承运商匹配
     * @return list
     */
    List<CarrierMapping> listCarrierMappingByCondition(CarrierMappingQueryDTO carrierMappingQueryDTO);

}
