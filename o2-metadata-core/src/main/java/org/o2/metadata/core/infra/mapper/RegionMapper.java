package org.o2.metadata.core.infra.mapper;

import io.choerodon.mybatis.common.BaseMapper;
import org.o2.metadata.core.domain.entity.Region;
import org.o2.metadata.core.domain.vo.RegionVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Set;

/**
 * @author tingting.wang@hand-china.com 2019-3-25
 */
public interface RegionMapper extends BaseMapper<Region> {

    /**
     * 查询地区，返回下一级
     *
     * @param countryIdOrCode
     * @param parentRegionId
     * @param enabledFlag
     * @param tenantId
     * @return
     */
    List<RegionVO> listChildren(@Param("countryIdOrCode") String countryIdOrCode,
                                @Param("parentRegionId") Long parentRegionId,
                                @Param("enabledFlag") Integer enabledFlag,
                                @Param("tenantId") Long tenantId);

    /**
     * 查询地区树
     *
     * @param countryIdOrCode 国家ID或编码
     * @param condition       查询条件
     * @param enabledFlag     筛选条件
     * @param tenantId        租户ID
     * @return 当前节点以及父级ID
     */
    Set<Region> selectRegion(@Param("countryIdOrCode") String countryIdOrCode,
                             @Param("condition") String condition,
                             @Param("enabledFlag") Integer enabledFlag,
                             @Param("tenantId") Long tenantId);

    /**
     * 批量查询地区
     *
     * @param parentRegionIdList 父级level_path
     * @param enabledFlag        筛选条件
     * @return List<Region>
     */
    Set<Region> selectRegionParent(@Param("parentRegionIdList") Set<Long> parentRegionIdList,
                                   @Param("enabledFlag") Integer enabledFlag);

    /**
     * 根据 levelPath 查询地区列表
     *
     * @param levelPathList 等级路径列表
     * @param tenantId 租户ID
     * @return 地区列表
     */
    List<Region> listRegionByLevelPath(@Param("levelPathList") List<String> levelPathList, @Param("tenantId") Long tenantId);

    /**
     * 查询地区以及子地区
     *
     * @param levelPath 等级路径
     * @return 地区以及子地区
     * @param tenantId 租户ID
     */
    List<Region> listRegionChildrenByLevelPath(@Param("levelPath") String levelPath, @Param("tenantId") Long tenantId);

    /**
     * 根据id 查询地区
     *
     * @param regionId 地区id
     * @return 地区对象
     */
    Region findRegionByPrimaryKey(@Param("regionId") Long regionId);
}

