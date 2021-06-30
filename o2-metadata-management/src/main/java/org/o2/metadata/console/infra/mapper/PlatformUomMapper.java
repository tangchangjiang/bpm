package org.o2.metadata.console.infra.mapper;

import org.o2.metadata.console.api.vo.PlatformUomVO;

import java.util.List;

/**
 * 平台值集Mapper
 *
 * @author peng.xu@hand-china.com 2019-07-09
 */
public interface PlatformUomMapper {

    /**
     * 根据父值集编码，获取子值集
     *
     * @param parentValue 父值集编码
     * @return 子值集列表
     */
    List<PlatformUomVO> getChildrenValues(String parentValue);

}
