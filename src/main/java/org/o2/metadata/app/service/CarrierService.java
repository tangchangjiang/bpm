package org.o2.metadata.app.service;

import org.o2.ext.metadata.domain.entity.Carrier;

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
     * @return 承运商集合
     */
    List<Carrier> batchUpdate(List<Carrier> carrierList);

    /**
     * 更新承运商
     *
     * @param carrierList 承运商
     * @return 承运商集合
     */
    List<Carrier> batchMerge(List<Carrier> carrierList);
}
