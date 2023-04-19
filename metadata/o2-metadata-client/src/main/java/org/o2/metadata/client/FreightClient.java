package org.o2.metadata.client;

import com.fasterxml.jackson.core.type.TypeReference;
import org.hzero.core.util.ResponseUtils;
import org.o2.metadata.client.domain.co.FreightInfoCO;
import org.o2.metadata.client.domain.dto.FreightDTO;
import org.o2.metadata.client.infra.feign.FreightRemoteService;

import java.util.List;
import java.util.Map;

/**
 * 运费
 *
 * @author yipeng.zhu@hand-china.com 2021-11-15
 **/
public class FreightClient {
    private final FreightRemoteService freightRemoteService;

    public FreightClient(FreightRemoteService freightRemoteService) {
        this.freightRemoteService = freightRemoteService;
    }

    /**
     * 获取模版
     *
     * @param freight 运费参数
     * @return 运费结果
     */
    public FreightInfoCO getFreightTemplate(FreightDTO freight, Long tenantId) {
        return ResponseUtils.getResponse(freightRemoteService.getFreightTemplate(freight, tenantId), FreightInfoCO.class);
    }

    /**
     * 批量获取运费模板
     *
     * @param freightList 运费参数
     * @return 运费结果 key:运费模板编码 value：运费
     */
    public Map<String, FreightInfoCO> listFreightTemplates(List<FreightDTO> freightList, Long tenantId) {
        return ResponseUtils.getResponse(freightRemoteService.listFreightTemplates(freightList, tenantId), new TypeReference<Map<String,
                FreightInfoCO>>() {
        });
    }

    /**
     * 获取运费模板（多租户）
     *
     * @param freightMap 运费参数map  tenantId:FreightDTO
     * @return 运费结果map  tenantId:FreightInfoCO
     */
    public Map<Long, FreightInfoCO> getFreightTemplateBatchTenant(Map<Long, FreightDTO> freightMap) {
        return ResponseUtils.getResponse(freightRemoteService.getFreightTemplateBatchTenant(freightMap), new TypeReference<Map<Long, FreightInfoCO>>() {
        });
    }

    /**
     * 批量获取运费模板（多租户）
     *
     * @param freightMap 运费参数map tenantId:list
     * @return 运费结果map  tenantId:templateCode:FreightInfoCO
     */
    public Map<Long, Map<String, FreightInfoCO>> listFreightTemplatesBatchTenant(Map<Long, List<FreightDTO>> freightMap) {
        return ResponseUtils.getResponse(freightRemoteService.listFreightTemplatesBatchTenant(freightMap), new TypeReference<Map<Long, Map<String, FreightInfoCO>>>() {
        });
    }
}
