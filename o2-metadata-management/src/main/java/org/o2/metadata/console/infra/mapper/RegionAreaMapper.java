package org.o2.metadata.console.infra.mapper;

import io.choerodon.mybatis.common.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.o2.metadata.console.infra.entity.RegionArea;

import java.util.List;

/**
 * 大区定义Mapper
 *
 * @author houlin.cheng@hand-china.com 2020-08-07 16:51:37
 */
public interface RegionAreaMapper extends BaseMapper<RegionArea> {

    /**
     * 地区编码查询大区定义
     * @param tenantId 租户ID
     * @param regionCodes 地区编码
     * @return 大区定义
     */

    List<RegionArea> batchSelectByCode(@Param("regionCodes") List<String> regionCodes, @Param("tenantId") Long tenantId);
}
