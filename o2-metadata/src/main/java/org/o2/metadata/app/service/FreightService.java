package org.o2.metadata.app.service;


import org.o2.metadata.api.dto.FreightDTO;
import org.o2.metadata.api.vo.FreightInfoCO;


/**
 * 运费计算服务
 *
 * @author peng.xu@hand-china.com 2019-06-18
 */
public interface FreightService {

    /**
     * 获取运费
     * @param  freight
     * @return 运费
     */
    FreightInfoCO getFreightTemplate(FreightDTO freight);
}
