package org.o2.metadata.console.app.service;

import org.o2.metadata.console.api.dto.CarrierDTO;
import org.o2.metadata.console.api.vo.CarrierVO;
import org.o2.metadata.console.infra.entity.Carrier;

import java.util.List;
import java.util.Map;

/**
 * 承运商应用服务
 *
 * @author tingting.wang@hand-china.com 2019-3-25
 */
public interface CarrierService {
    /**
     * 更新承运商
     *
     * @param carrierList 承运商
     * @param organizationId 租户ID
     * @return 承运商集合
     */
    List<Carrier> batchUpdate(Long organizationId, List<Carrier> carrierList);

    /**
     * 更新承运商
     *
     * @param carrierList    承运商
     * @param organizationId    租户ID
     * @return 承运商集合
     */
    List<Carrier> batchMerge(Long organizationId, List<Carrier> carrierList);

    /**
     * 删除承运商
     * @param carrierList    承运商
     * @param organizationId    租户ID
     */
    void batchDelete(Long organizationId, List<Carrier> carrierList);

    /**
     * 批量查询承运商
     * @param carrierDTOList 承运商
     * @param organizationId 租户ID
     * @return map
     */
    Map<String, CarrierVO> listCarriers(List<CarrierDTO> carrierDTOList, Long organizationId);
}
