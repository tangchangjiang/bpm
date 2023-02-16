package org.o2.metadata.console.infra.redis;

import java.util.List;
import java.util.Map;

/**
 * 国家信息-Redis
 */
public interface RegionRedis {
    /**
     * 保存地区静态资源url
     *
     * @param tenantId       租户ID
     * @param resourceUrlMap 静态资源map
     */
    void saveRegionUrlToRedis(Long tenantId, String countryCode, Map<String, String> resourceUrlMap);

    /**
     * 查询地区静态资源url
     *
     * @param tenantId        租户ID
     * @param countryCodeList 国家编码集合
     * @return 静态资源map
     */
    Map<String, String> queryRegionUrlFromRedis(Long tenantId, String lang, List<String> countryCodeList);
}
