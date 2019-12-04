package org.o2.metadata.infra.mapper;

import io.choerodon.mybatis.common.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.o2.metadata.domain.entity.OnlineShopRelPos;
import org.o2.metadata.domain.vo.OnlineShopRelPosVO;

import java.util.List;

/**
 * 网店关联服务点Mapper
 *
 * @author tingting.wang@hand-china.com 2019-3-25
 */
public interface OnlineShopRelPosMapper extends BaseMapper<OnlineShopRelPos> {


    /**
     * 条件查询
     *
     * @param onlineShopId 网点 id，不能为空
     * @param posCode      服务点编码，可为空
     * @param posName      服务点名称，可为空
     * @param tenantId      租户ID，可为空
     * @return 服务点网店关联关系列表
     */
    List<OnlineShopRelPosVO> listShopPosRelsByOption(@Param("onlineShopId") Long onlineShopId,
                                                     @Param("posCode") String posCode,
                                                     @Param("posName") String posName,
                                                     @Param("tenantId") Long tenantId);
}
