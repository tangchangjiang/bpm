package org.o2.metadata.console.infra.mapper;

import io.choerodon.mybatis.common.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.o2.metadata.console.api.dto.CarrierQueryInnerDTO;
import org.o2.metadata.console.infra.entity.Carrier;

import java.util.List;

/**
 * 承运商Mapper
 *
 * @author tingting.wang@hand-china.com 2019-3-25
 */
public interface CarrierMapper extends BaseMapper<Carrier> {

    /**
     * 查询承运商
     *
     * @param carrier 承运商
     * @param siteFlag 是否站点级查询
     * @return 承运商列表
     */
    List<Carrier> listCarrier(@Param("carrier") Carrier carrier, @Param("siteFlag") Integer siteFlag);

    /**
     * 批量查询承运商
     *
     * @param carrierQueryInnerDTO 承运商
     * @param tenantId             租户ID
     * @return list
     */
    List<Carrier> batchSelect(@Param("carrierDTO") CarrierQueryInnerDTO carrierQueryInnerDTO, @Param("tenantId") Long tenantId);

}
