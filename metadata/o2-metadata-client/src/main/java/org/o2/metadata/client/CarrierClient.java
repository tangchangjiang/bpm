package org.o2.metadata.client;

import com.fasterxml.jackson.core.type.TypeReference;
import org.hzero.core.util.ResponseUtils;
import org.o2.metadata.client.domain.co.CarrierCO;
import org.o2.metadata.client.infra.feign.CarrierRemoteService;

import java.util.List;

/**
 * 承运商
 *
 * @author yipeng.zhu@hand-china.com 2021-11-15
 **/
public class CarrierClient {

    private final CarrierRemoteService carrierRemoteService;

    public CarrierClient(CarrierRemoteService carrierRemoteService) {
        this.carrierRemoteService = carrierRemoteService;
    }

    /**
     * 查询承运商
     *
     * @param organizationId 租户id
     * @return list
     */
    @Deprecated
    public List<CarrierCO> listCarriers(final Long organizationId) {
        return ResponseUtils.getResponse(carrierRemoteService.listCarriers(organizationId), new TypeReference<List<CarrierCO>>() {
        });
    }

    /**
     * 查询承运商
     *
     * @return 承运商
     */
    public List<CarrierCO> listCarriers() {
        return ResponseUtils.getResponse(carrierRemoteService.listCarriers(), new TypeReference<List<CarrierCO>>() {
        });
    }
}
