package org.o2.feignclient;

import com.fasterxml.jackson.core.type.TypeReference;
import org.hzero.core.util.ResponseUtils;
import org.o2.feignclient.metadata.domain.co.RegionCO;
import org.o2.feignclient.metadata.domain.dto.RegionQueryLovInnerDTO;
import org.o2.feignclient.metadata.infra.feign.LovAdapterRemoteService;

import java.util.List;

/**
 *
 * 地区值集查询
 *
 * @author yipeng.zhu@hand-china.com 2021-09-22
 **/
public class RegionLovClient {
    private LovAdapterRemoteService lovAdapterRemoteService;

    public RegionLovClient(LovAdapterRemoteService lovAdapterRemoteService) {
        this.lovAdapterRemoteService = lovAdapterRemoteService;
    }

    /**
     * 批量查集地区信息
     * @param innerDTO 查询条件
     * @param organizationId 租户ID
     * @return 值集集合
     */
    public List<RegionCO> queryRegion(Long organizationId, RegionQueryLovInnerDTO innerDTO) {
        return ResponseUtils.getResponse(lovAdapterRemoteService.queryRegion(organizationId, innerDTO), new TypeReference<List<RegionCO>>() {
        });
    }
}
