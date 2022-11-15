package org.o2.metadata.console.infra.repository;

import org.hzero.mybatis.base.BaseRepository;
import org.o2.metadata.console.api.dto.CarrierFreightDTO;
import org.o2.metadata.console.api.dto.CarrierQueryInnerDTO;
import org.o2.metadata.console.app.bo.CarrierLogisticsCostBO;
import org.o2.metadata.console.infra.entity.Carrier;

import java.util.List;

/**
 * 承运商资源库
 *
 * @author tingting.wang@hand-china.com 2019-3-25
 */
public interface CarrierRepository extends BaseRepository<Carrier> {

    /**
     * 承运商资源库查询
     *
     * @param carrier 承运商资源库
     * @return 承运商资源库
     */
    List<Carrier> listCarrier(Carrier carrier);

    /**
     * 批量查询承运商
     *
     * @param carrierQueryInnerDTO 承运商
     * @param tenantId             租户ID
     * @return list
     */
    List<Carrier> batchSelect(CarrierQueryInnerDTO carrierQueryInnerDTO, Long tenantId);

    /**
     * 获取承运商对应的运费模板信息
     *
     * @param carrierFreightDTO 参数
     * @return 结果
     */
    List<CarrierLogisticsCostBO> listCarrierLogisticsCost(CarrierFreightDTO carrierFreightDTO);
}
