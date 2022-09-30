package org.o2.metadata.client;

import com.fasterxml.jackson.core.type.TypeReference;
import org.hzero.core.util.ResponseUtils;
import org.o2.metadata.client.domain.co.RoleCO;
import org.o2.metadata.client.infra.feign.LovAdapterRemoteService;

import java.util.List;
import java.util.Map;

/**
 * Description
 *
 * @author yipeng.zhu@hand-china.com 2022/8/30
 */
public class RoleClient {
    private final LovAdapterRemoteService lovAdapterRemoteService;

    public RoleClient(LovAdapterRemoteService lovAdapterRemoteService) {
        this.lovAdapterRemoteService = lovAdapterRemoteService;
    }

    /**
     * 通过编码查询角色(批量)
     * @param tenantId 租户ID
     * @param roleCods 角色编码
     * @return  key:角色编码 value：角色
     */
    public Map<String, RoleCO> findRoleByCodes(Long tenantId, List<String> roleCods) {
        return ResponseUtils.getResponse(lovAdapterRemoteService.findRoleByCodes(tenantId, roleCods), new TypeReference<Map<String, RoleCO>>() {
        });
    }
}
