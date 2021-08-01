package org.o2.metadata.console.app.service;

import org.o2.metadata.console.api.vo.RegionCacheVO;

/**
 * 同步地区静态文件
 *
 * @author yingpeng.zhu@hand-china.com 2020-5-20
 */
public interface O2SiteRegionFileService {
    /**
     * 同步地区静态文件
     *
     * @param regionCacheVO
     * @return OSS url
     * @date 2020-05-20
     */
    void createRegionStaticFile(final RegionCacheVO regionCacheVO);
}
