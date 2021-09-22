package org.o2.feignclient;

import com.fasterxml.jackson.core.type.TypeReference;
import org.hzero.core.util.ResponseUtils;
import org.o2.feignclient.metadata.domain.co.PageCO;
import org.o2.feignclient.metadata.domain.co.RegionCO;
import org.o2.feignclient.metadata.infra.feign.LovAdapterRemoteService;

import java.util.List;
import java.util.Map;

/**
 *
 * 地区值集查询
 *
 * @author yipeng.zhu@hand-china.com 2021-09-22
 **/
public class O2RegionLovClient {
    private LovAdapterRemoteService lovAdapterRemoteService;

    public O2RegionLovClient(LovAdapterRemoteService lovAdapterRemoteService) {
        this.lovAdapterRemoteService = lovAdapterRemoteService;
    }

    /**
     * 根据编码以及租户ID批量查集值
     * @param queryLovValueMap 查询条件
     * @param organizationId 租户ID
     * @return 值集集合
     */
    List<RegionCO> queryRegion(Long organizationId, Map<String, String> queryLovValueMap) {
        return ResponseUtils.getResponse(lovAdapterRemoteService.queryRegion(organizationId, queryLovValueMap), new TypeReference<List<RegionCO>>() {
        });
    }

    /**
     * 分页查询地区值集
     * @param organizationId 租户ID
     * @param page page 页码
     * @param size 大小
     * @param queryParam 查询参数
     * @return page
     */
    PageCO<RegionCO> queryRegionPage(Long organizationId, Integer page, Integer size, Map<String, String> queryParam) {
        return ResponseUtils.getResponse(lovAdapterRemoteService.queryRegionPage(organizationId, page, size, queryParam), new TypeReference<PageCO<RegionCO>>() {
        });
    }
}
