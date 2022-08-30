package org.o2.metadata.infra.lovadapter.repository;

import org.o2.metadata.api.co.RoleCO;

import java.util.List;
import java.util.Map;

/**
 * sql值集查询
 * @author yipeng.zhu@hand-china.com 2022/8/30
 */
public interface SqlLovQueryRepository {

    /**
     * 通过编码查询角色(批量)
     * @param organizationId 租户ID
     * @param roleCodes 角色编码
     * @return 角色
     */
    Map<String, RoleCO> findRoleByCodes(Long organizationId, List<String> roleCodes);
}
