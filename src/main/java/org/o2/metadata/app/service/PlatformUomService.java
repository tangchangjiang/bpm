package org.o2.metadata.app.service;

import org.o2.ext.metadata.domain.vo.PlatformUomVO;

import java.util.List;

/**
 * 平台值集服务
 *
 * @author peng.xu@hand-china.com 2019-07-09
 */
public interface PlatformUomService {

    /**
     * 根据父值集编码，获取子值集
     *
     * @param parentValue 父值集编码
     * @return 子值集列表
     */
    List<PlatformUomVO> getChildrenValues(String parentValue);

}
