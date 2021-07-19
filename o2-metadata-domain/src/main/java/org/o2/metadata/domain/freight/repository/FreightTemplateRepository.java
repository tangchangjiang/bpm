package org.o2.metadata.domain.freight.repository;

import org.o2.metadata.domain.freight.domain.FreightInfoDO;

/**
 *
 * 运费模版信息
 *
 * @author yipeng.zhu@hand-china.com 2021-07-19
 **/
public interface FreightTemplateRepository {
    /**
     *
     * @date 2021-07-19
     * @param
     * @return 
     */
    FreightInfoDO getFreightTemplate(String regionCode,String templateCode,Long tenantId);
}
