package org.o2.metadata.console.app.service;

import org.o2.metadata.console.api.vo.PublicLovVO;

/**
 * O2MD.PUBLIC_LOV  值集文件缓存刷新Job
 *
 * @author: kang.yang@hand-china.com 2021-08-20 14:17
 **/
public interface O2PublicLovService {

    /**
     * 同步O2MD.PUBLIC_LOV值集静态文件
     *
     * @param publicLovVO
     * @return OSS url
     * @date 2021-08-20
     */
    void createPublicLovFile(final PublicLovVO publicLovVO);
}
