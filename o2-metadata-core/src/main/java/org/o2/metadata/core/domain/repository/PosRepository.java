package org.o2.metadata.core.domain.repository;


import org.o2.metadata.core.domain.entity.Pos;
import org.o2.metadata.core.domain.vo.PosVO;
import org.hzero.mybatis.base.BaseRepository;
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
    List<PosVO> listPosWithAddressByCondition(PosVO pos);

    /**
     * 主键查询
     *
     * @param posId 服务点 id
     * @return 带详细地址和接派单时间的服务点信息
     */
    Pos getPosWithAddressAndPostTimeByPosId(Long posId);

    /**
     * 查询未与网店关联的服务点
     *
     * @param shopId  网店 id
     * @param posCode
     * @param posName
     * @param tenantId
     * @return 服务点列表
     */
    List<Pos> listUnbindPosList(Long shopId, String posCode, String posName, Long tenantId);

    /**
     * 根据服务点编码查询
     *
     * @param posCode 服务点编码
     * @param tenantId 租户ID
     * @return 服务点信息
     */
    Pos getPosByCode(Long tenantId, String posCode);
}
