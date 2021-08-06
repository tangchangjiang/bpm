package org.o2.metadata.app.service;

import org.o2.metadata.api.vo.CarrierVO;

import java.util.List;

/**
 *
 * 承运商
 *
 * @author yipeng.zhu@hand-china.com 2021-08-05
 **/
public interface CarrierService {
    /**
     * 查询承运商
     * @param tenantId 租户id
     * @return  list
     */
    List<CarrierVO> listCarriers(Long tenantId);

}
