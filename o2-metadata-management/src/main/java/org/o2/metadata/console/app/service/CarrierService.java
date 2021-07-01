package org.o2.metadata.console.app.service;

import org.o2.metadata.console.domain.entity.Carrier;

import java.util.List;

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
}
