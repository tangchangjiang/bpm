package org.o2.metadata.console.infra.repository;

import org.hzero.mybatis.base.BaseRepository;
import org.o2.metadata.console.api.dto.RegionQueryLovInnerDTO;
import org.o2.metadata.console.infra.entity.Region;

import java.util.List;

/**
 * @author tingting.wang@hand-china.com 2019-3-25
 */
public interface RegionRepository extends BaseRepository<Region> {

    /**
     * 查询地区树，返回父级ID
     *
     * @param countryIdOrCode 国家ID或CODE
     * @param condition       查询条件
     * @param enabledFlag     筛选条件
     * @param tenantId        租户ID
     * @return 当前节点以及父级ID
     */
    List<Region> listRegionWithParent(String countryIdOrCode, String condition, Integer enabledFlag, Long tenantId);

    /**
     *  查询地区sql值集
     * @param  regionQueryLov 查询条件
     * @param  tenantId 租户ID
     * @return map     */
    List<Region> listRegionLov(RegionQueryLovInnerDTO regionQueryLov, Long tenantId);
}
