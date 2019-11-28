package org.o2.metadata.app.service;

import org.o2.metadata.domain.entity.AddressMapping;
import org.o2.metadata.domain.vo.RegionTreeChildVO;

import java.util.List;

/**
 * 地址匹配应用服务
 *
 * @author tingting.wang@hand-china.com 2019-3-25
 */
public interface AddressMappingService {

    /**
     * 地址匹配逆向递归 树状数据结构（根据parent id 分组 减少没必要的递归）
     *
     * @param condition 查询条件
     * @param countryCode 国家编码
     * @return 查询结果集
     */

    List<RegionTreeChildVO> findAddressMappingGroupByCondition(AddressMapping condition, String countryCode);

}
