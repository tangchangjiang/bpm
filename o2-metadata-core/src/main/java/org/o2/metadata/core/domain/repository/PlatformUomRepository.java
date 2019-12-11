package org.o2.metadata.core.domain.repository;


import org.o2.metadata.core.domain.vo.PlatformUomVO;
import java.util.List;

/**
 * PlatformUomRepository
 *
 * @author peng.xu@hand-china.com 2019-07-09
 */
public interface PlatformUomRepository {

    /**
     * 根据父值集编码，获取子值集
     *
     * @param parentValue 父值集编码
     * @return 子值集列表
     */
    List<PlatformUomVO> getChildrenValues(String parentValue);
}
