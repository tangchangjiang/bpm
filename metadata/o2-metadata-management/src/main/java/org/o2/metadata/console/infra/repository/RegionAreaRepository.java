package org.o2.metadata.console.infra.repository;

import org.hzero.mybatis.base.BaseRepository;
import org.o2.metadata.console.infra.entity.RegionArea;

import java.util.List;

/**
 * 大区定义资源库
 *
 * @author houlin.cheng@hand-china.com 2020-08-07 16:51:37
 */
public interface RegionAreaRepository extends BaseRepository<RegionArea> {
    /**
     * 地区编码查询大区定义
     * @param tenantId 租户ID
     * @param regionCodes 地区编码
     * @return 大区定义
     */
    List<RegionArea> batchSelectByCode(List<String> regionCodes, Long tenantId);
}
