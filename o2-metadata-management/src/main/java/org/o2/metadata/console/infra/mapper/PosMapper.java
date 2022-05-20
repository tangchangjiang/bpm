package org.o2.metadata.console.infra.mapper;

import io.choerodon.mybatis.common.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.o2.metadata.console.api.dto.PosDTO;
import org.o2.metadata.console.infra.entity.Pos;
import org.o2.metadata.console.infra.entity.PosInfo;

import java.util.List;

/**
 * 服务点信息Mapper
 *
 * @author tingting.wang@hand-china.com 2019-3-25
 */
public interface PosMapper extends BaseMapper<Pos> {

    /**
     * 查询字段：
     * 编码 | 名称 | 服务点类型 | 门店类型 | 门店发货 | 门点自提 | 省 | 市 | 区
     * <p/>
     *
     * @param pos 服务点信息，包含地址省市区字段
     * @return 带详细地址信息的服务点列表
     */
    List<Pos> listPosWithAddressByCondition(PosDTO pos);

    /**
     * 查询未与网店关联的服务点
     *
     * @param onlineShopId 网店 id
     * @param posCode 服务点编码
     * @param posName 服务名称
     * @param tenantId 租户ID
     * @return 服务点列表
     */
    List<Pos> listUnbindPosList(@Param(value = "onlineShopId") Long onlineShopId,
                                @Param(value = "posCode") String posCode,
                                @Param(value = "posName") String posName,
                                @Param(value = "tenantId") Long tenantId);

    /**
     * 主键查询
     * @param tenantId
     * @param posId 服务点 id
     * @return 带详细地址和接派单时间的服务点信息
     */
    Pos getPosWithCarrierNameById(Long tenantId, Long posId);

    /**
     * 多条件查询服务点
     * @param pos 服务点
     * @return  list
     */
    List<Pos> listPosByCondition(Pos pos);

    /**
     * 查询服务点信息（提货信息）
     * @param posIds 服务点id
     * @param posCodes 服务点code
     * @param posTypeCode 服务店类型
     * @param tenantId 租户Id
     * @return list
     */
    List<PosInfo> listPosInfoByCode(@Param("posIds") List<Long> posIds,
                                    @Param("posCodes") List<String> posCodes,
                                    @Param("posTypeCode") String posTypeCode,
                                    @Param("tenantId") Long tenantId);

    /**
     * 根据服务点编码批量查询服务点信息
     * @param posCodes 服务点code
     * @param tenantId 租户Id
     * @return list
     */
    List<Pos> listPosByCode(@Param(value = "tenantId") Long tenantId,
                            @Param("posCodes") List<String> posCodes);

}
