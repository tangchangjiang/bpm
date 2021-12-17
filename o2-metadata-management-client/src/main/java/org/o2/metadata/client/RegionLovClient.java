package org.o2.metadata.client;

import com.fasterxml.jackson.core.type.TypeReference;
import org.hzero.core.util.ResponseUtils;
import org.o2.metadata.client.domain.co.PageCO;
import org.o2.metadata.client.domain.co.RegionCO;
import org.o2.metadata.client.domain.dto.RegionQueryLovInnerDTO;
import org.o2.metadata.client.infra.feign.LovAdapterRemoteService;

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
     * 根据编码以及租户ID批量查集值
     * @param innerDTO 查询条件
     * @param organizationId 租户ID
     * @return 值集集合
     */
    public List<RegionCO> queryRegion(Long organizationId, RegionQueryLovInnerDTO innerDTO) {
        return ResponseUtils.getResponse(lovAdapterRemoteService.queryRegion(organizationId, innerDTO), new TypeReference<List<RegionCO>>() {
        });
    }

    /**
     * 分页查询地区值集
     * @param organizationId 租户ID
     * @param page page 页码
     * @param size 大小
     * @param innerDTO 查询参数
     * @return page
     */
    public PageCO<RegionCO> queryRegionPage(Long organizationId, Integer page, Integer size, RegionQueryLovInnerDTO innerDTO) {
        return ResponseUtils.getResponse(lovAdapterRemoteService.queryRegionPage(organizationId, page, size, innerDTO), new TypeReference<PageCO<RegionCO>>() {
        });
    }
}
