package org.o2.feignclient;

import org.hzero.core.util.ResponseUtils;
import org.o2.feignclient.metadata.domain.co.FreightInfoCO;
import org.o2.feignclient.metadata.domain.co.FreightTemplateCO;
import org.o2.feignclient.metadata.domain.dto.FreightDTO;
import org.o2.feignclient.metadata.infra.feign.FreightRemoteService;

/**
 *
 * 运费
 *
 * @author yipeng.zhu@hand-china.com 2021-09-23
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
     * 获取默认模版
     *
     * @param tenantId 租户id
     * @return 模版
     */
    public FreightTemplateCO getDefaultTemplate(Long tenantId) {
        return ResponseUtils.getResponse(freightRemoteService.getDefaultTemplate( tenantId), FreightTemplateCO.class);
    }
}
