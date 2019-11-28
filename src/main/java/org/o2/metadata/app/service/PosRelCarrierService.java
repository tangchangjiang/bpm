package org.o2.metadata.app.service;

import org.o2.metadata.domain.entity.PosRelCarrier;

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
     * @param posRelCarrieies 服务点关联承运商
     * @return 结果集
     */
    List<PosRelCarrier> batchMerge(List<PosRelCarrier> posRelCarrieies);
}