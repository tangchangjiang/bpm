package org.o2.metadata.infra.redis;

import org.o2.metadata.infra.entity.FreightInfo;

import java.util.List;

/**
 *
 * 运费模版信息
 *
 * @author yipeng.zhu@hand-china.com 2021-07-19
 **/
public interface FreightRedis {
    /**
     * 获取运费模版信息
     * @param regionCode 地区ID
     * @param templateCode 模版ID
     * @param tenantId 租户
     * @return 运费信息
     */
    FreightInfo getFreightTemplate(String regionCode, String templateCode, Long tenantId);

    List<FreightInfo> listFreightTemplate(Long tenantId, List<String> templateCodes);
}
