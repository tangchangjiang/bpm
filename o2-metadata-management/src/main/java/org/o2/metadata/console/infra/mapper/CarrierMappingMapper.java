package org.o2.metadata.console.infra.mapper;

import io.choerodon.mybatis.common.BaseMapper;
import org.o2.metadata.console.api.vo.CarrierMappingVO;
import org.o2.metadata.console.domain.entity.CarrierMapping;

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
     * @param carrierMappingVO
     * @return 承运商匹配表
     */
    List<CarrierMappingVO> listCarrierMappingByCondition(CarrierMappingVO carrierMappingVO);

}
