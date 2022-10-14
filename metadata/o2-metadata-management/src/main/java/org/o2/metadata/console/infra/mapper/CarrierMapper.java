package org.o2.metadata.console.infra.mapper;

import io.choerodon.mybatis.common.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.o2.metadata.console.api.dto.CarrierFreightDTO;
import org.o2.metadata.console.api.dto.CarrierQueryInnerDTO;
import org.o2.metadata.console.app.bo.CarrierLogisticsCostBO;
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
     * @return 承运商列表
     */
    List<Carrier> listCarrier(Carrier carrier);

    /**
     * 批量查询承运商
     *
     * @param carrierQueryInnerDTO 承运商
     * @param tenantId             租户ID
     * @return list
     */
    List<Carrier> batchSelect(@Param("carrierDTO") CarrierQueryInnerDTO carrierQueryInnerDTO, @Param("tenantId") Long tenantId);

    /**
     * 获取承运商对应的运费模板信息
     *
     * @param carrierFreightDTO 参数
     * @return 结果
     */
    List<CarrierLogisticsCostBO> listCarrierLogisticsCost(CarrierFreightDTO carrierFreightDTO);

}
