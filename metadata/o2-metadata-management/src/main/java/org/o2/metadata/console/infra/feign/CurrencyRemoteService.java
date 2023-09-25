package org.o2.metadata.console.infra.feign;

import io.swagger.models.auth.In;
import org.hzero.common.HZeroService;
import org.o2.metadata.console.infra.feign.impl.CurrencyRemoteServiceImpl;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(
        name = HZeroService.Platform.NAME,
        path = "/v1",
        fallback = CurrencyRemoteServiceImpl.class
)
public interface CurrencyRemoteService {

    @GetMapping("/{organizationId}/currencys")
    ResponseEntity<String> queryCurrency(@PathVariable Long organizationId,
                                         @RequestParam String currencyCode,
                                         @RequestParam String currencyName,
                                         @RequestParam Integer enabledFlag,
                                         @RequestParam Integer page,
                                         @RequestParam Integer size);
}
