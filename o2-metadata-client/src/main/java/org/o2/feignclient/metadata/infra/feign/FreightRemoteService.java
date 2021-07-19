package org.o2.feignclient.metadata.infra.feign;

import org.o2.feignclient.metadata.domain.dto.FreightDTO;
import org.o2.feignclient.metadata.infra.constants.O2Service;
import org.o2.feignclient.metadata.infra.feign.fallback.FreightServiceRemoteServiceImpl;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 *
 * 运费
 *
 * @author yipeng.zhu@hand-china.com 2021-07-14
 **/
@FeignClient(
        value = O2Service.metadata.NAME,
        path = "/v1",
        fallback = FreightServiceRemoteServiceImpl.class
)
public interface FreightRemoteService {
    /**
     * 获取运费
     *
     * @param freight 运费参数
     * @return 运费
     */
    @PostMapping("/{organizationId}/freight-internal/template")
    ResponseEntity<String> getFreightTemplate(@RequestBody FreightDTO freight);
}
