package org.o2.metadata.app.service;


import org.o2.metadata.api.dto.FreightDTO;

import java.math.BigDecimal;

/**
 * 运费计算服务
 *
 * @author peng.xu@hand-china.com 2019-06-18
 */
public interface FreightService {

    /**
     * 获取运费
     *
     * @param freight 运费参数
     * @return 运费
     */
    BigDecimal getFreightAmount(FreightDTO freight);
}
