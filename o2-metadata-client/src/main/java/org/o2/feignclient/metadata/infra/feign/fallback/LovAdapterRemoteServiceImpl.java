package org.o2.feignclient.metadata.infra.feign.fallback;

import lombok.extern.slf4j.Slf4j;
import org.o2.feignclient.metadata.infra.feign.LovAdapterRemoteService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.List;


/**
 *
 * 值集查询
 * @author yipeng.zhu@hand-china.com 2021-08-30
 **/
@Component
@Slf4j
public class LovAdapterRemoteServiceImpl implements LovAdapterRemoteService {

    @Override
    public ResponseEntity<String> queryLovValueMeaning(Long organizationId, String lovCode, String lovValue) {
        log.error("Error queryLovValueMeaning, params[lovCode = {}, organizationId = {}, lovValue= {}]", lovCode, organizationId,lovValue);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }

    @Override
    public ResponseEntity<String> findCurrencyByCodes(Long organizationId, List<String> currencyCodes) {
        log.error("Error findCurrencyByCodes, params[lovCode = {}, organizationId = {}]", organizationId,currencyCodes);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();

    }


}
