package org.o2.metadata.domain.freight.repository;

import org.o2.metadata.domain.freight.domain.FreightInfoDO;

import java.util.List;

/**
 *
 * 运费模版信息
 *
 * @author yipeng.zhu@hand-china.com 2021-07-19
 **/
public interface FreightTemplateDomainRepository {
    /**
     * 获取运费模版信息
     * @param regionCode 地区ID
     * @param templateCode 模版ID
     * @param tenantId 租户
     * @return 运费信息
     */
    FreightInfoDO getFreightTemplate(String regionCode,String templateCode,Long tenantId);

    /**
     * 批量获取运费模版信息
     * @param templateCodes 模版ID
     * @param tenantId 租户
     * @return 运费信息
     */
    List<FreightInfoDO> listFreightTemplate(Long tenantId, List<String> templateCodes);
}
