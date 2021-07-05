package org.o2.metadata.console.app.service;

import org.o2.metadata.console.domain.entity.PosRelCarrier;

import java.util.List;

/**
 * 服务点关联承运商应用服务
 *
 * @author tingting.wang@hand-china.com 2019-3-25
 */
public interface PosRelCarrierService {
    /**
     * 批量新增或更新服务点关联承运商
     *
     * @param posRelCarriers 服务点关联承运商
     * @param organizationId 租户ID
     * @return 结果集
     */
    List<PosRelCarrier> batchMerge(Long organizationId, List<PosRelCarrier> posRelCarriers);
}
