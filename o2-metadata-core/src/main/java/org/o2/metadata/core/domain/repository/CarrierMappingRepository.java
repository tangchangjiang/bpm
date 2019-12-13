package org.o2.metadata.core.domain.repository;

import org.o2.metadata.core.domain.entity.CarrierMapping;
import org.o2.metadata.core.domain.vo.CarrierMappingVO;
import org.hzero.mybatis.base.BaseRepository;
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