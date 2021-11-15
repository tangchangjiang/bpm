package org.o2.feignclient;

import com.fasterxml.jackson.core.type.TypeReference;
import org.hzero.core.util.ResponseUtils;
import org.o2.feignclient.metadata.domain.co.CurrencyCO;
import org.o2.feignclient.metadata.domain.co.FreightInfoCO;
import org.o2.feignclient.metadata.domain.dto.FreightDTO;
import org.o2.feignclient.metadata.infra.feign.FreightRemoteService;

import java.util.List;
import java.util.Map;

/**
 *
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
    public FreightInfoCO getFreightTemplate(FreightDTO freight, Long tenantId){
        return ResponseUtils.getResponse(freightRemoteService.getFreightTemplate(freight,tenantId), FreightInfoCO.class);
    }

    /**
     * 批量获取运费模板
     * @param freightList 运费参数
     * @return 运费结果
     */
    public Map<String, FreightInfoCO> listFreightTemplates(List<FreightDTO> freightList, Long tenantId) {
        return ResponseUtils.getResponse(freightRemoteService.listFreightTemplates(freightList, tenantId), new TypeReference<Map<String, FreightInfoCO>>() {
        });
    }
}
