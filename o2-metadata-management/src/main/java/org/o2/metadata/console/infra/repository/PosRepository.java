package org.o2.metadata.console.infra.repository;


import org.hzero.mybatis.base.BaseRepository;
import org.o2.metadata.console.api.dto.PosDTO;
import org.o2.metadata.console.infra.entity.Pos;
import org.o2.metadata.console.infra.entity.PosInfo;

import java.util.List;

/**
 * 服务点信息资源库
 *
 * @author tingting.wang@hand-china.com 2019-3-25
 */
public interface PosRepository extends BaseRepository<Pos> {

    /**
     * 按服务点编码升序排序
     * <p>
     * 查询字段说明：
     * 编码 | 名称 | 服务点类型 | 门店类型 | 门店发货 | 门点自提 | 省 | 市 | 区
     * <p/>
     * 返回字段说明：
     * 编码 | 名称 | 服务点类型 | 门店类型 | 门店发货 | 门点自提 | 省 | 市 | 区 | 详细地址 | 服务点 id
     *
     * @param pos 服务点对象，包含地址信息
     * @return list of pos
     */
    List<Pos> listPosWithAddressByCondition(PosDTO pos);

    /**
     * 主键查询
     * @param tenantId 租户id
     * @param posId 服务点 id
     * @return 带详细地址和接派单时间的服务点信息
     */
    Pos getPosWithAddressAndPostTimeByPosId(Long tenantId, Long posId);

    /**
     * 查询未与网店关联的服务点
     *
     * @param shopId   网店 id
     * @param posCode  门店编码
     * @param posName  门店名称
     * @param tenantId 租户id
     * @return 服务点列表
     */
    List<Pos> listUnbindPosList(Long shopId, String posCode, String posName, Long tenantId);

    /**
     * 根据服务点编码查询
     *
     * @param posCode  服务点编码
     * @param tenantId 租户ID
     * @return 服务点信息
     */
    Pos getPosByCode(Long tenantId, String posCode);

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
    List<PosInfo> listPosInfoByCode(List<Long> posIds, List<String> posCodes, String posTypeCode, Long tenantId);
}
