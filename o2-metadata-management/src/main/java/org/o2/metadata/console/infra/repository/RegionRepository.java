package org.o2.metadata.console.infra.repository;

import org.hzero.mybatis.base.BaseRepository;
import org.o2.metadata.console.api.vo.RegionVO;
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
     * 查询地区
     *
     * @param countryIdOrCode 国家ID或CODE
     * @param enabledFlag     筛选条件
     * @param parentRegionId  级联父节点ID
     * @param tenantId        租户ID
     * @return 地区列表
     */
    List<RegionVO> listChildren(String countryIdOrCode, Long parentRegionId, Integer enabledFlag, Long tenantId);

    /**
     * 根据 levelPath 查询地区列表
     *
     * @param levelPathList 等级路径列表
     * @param tenantId 租户ID
     * @return 地区列表
     */
    List<Region> listRegionByLevelPath(List<String> levelPathList, Long tenantId);

    /**
     * 查询地区以及子地区
     *
     * @param levelPath 等级路径
     * @param tenantId 租户ID
     * @return 地区以及子地区
     */
    List<Region> listRegionChildrenByLevelPath(String levelPath, Long tenantId);
}
