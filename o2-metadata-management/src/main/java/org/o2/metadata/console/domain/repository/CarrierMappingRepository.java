package org.o2.metadata.console.domain.repository;

import org.hzero.mybatis.base.BaseRepository;
import org.o2.metadata.console.api.vo.CarrierMappingVO;
import org.o2.metadata.console.domain.entity.CarrierMapping;

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
     * @param carrierMappingVO
     * @return
     */
    List<CarrierMappingVO> listCarrierMappingByCondition(CarrierMappingVO carrierMappingVO);

}
