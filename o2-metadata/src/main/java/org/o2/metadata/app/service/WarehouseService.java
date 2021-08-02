package org.o2.metadata.app.service;

import org.o2.metadata.api.vo.WarehouseVO;

import java.util.List;

/**
 * @author lei.tang02@hand-china.com 2020/8/27
 */
public interface WarehouseService {
    /**
     * 获取仓库信息
     * @param warehouseCodes 仓库编码
     * @param organizationId 租户ID
     * @return 仓库
     */
    List<WarehouseVO> listWarehouses(List<String> warehouseCodes, Long organizationId);
}
