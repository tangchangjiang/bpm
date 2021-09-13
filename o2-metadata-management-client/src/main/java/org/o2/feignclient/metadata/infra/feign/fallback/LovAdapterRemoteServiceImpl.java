package org.o2.feignclient.metadata.infra.feign.fallback;

import lombok.extern.slf4j.Slf4j;
import org.o2.feignclient.metadata.infra.feign.LovAdapterRemoteService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 *
 * 值集查询
 * @author yipeng.zhu@hand-china.com 2021-08-30
 **/
@Component
@Slf4j
public class LovAdapterRemoteServiceImpl implements LovAdapterRemoteService {
    @Override
    public ResponseEntity<String> findCurrencyByCodes(Long organizationId, List<String> currencyCodes) {
        log.error("Error batchSelectNameByCode, params[currencyCodes = {}, organizationId = {}]", currencyCodes, organizationId);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }

    @Override
    public ResponseEntity<String> findUomByCodes(Long organizationId, List<String> uomCodes) {
        log.error("Error findUomByCodes, params[uomCodes = {}, organizationId = {}]", uomCodes, organizationId);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }

    @Override
    public ResponseEntity<String> findUomTypeByCodes(Long organizationId, List<String> uomTypeCodes) {
        log.error("Error findUomTypeByCodes, params[uomCodes = {}, organizationId = {}]", uomTypeCodes, organizationId);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }

    @Override
    public ResponseEntity<String> queryLovValue(Long organizationId, String lovCode) {
        log.error("Error queryLovValue, params[lovCode = {}, organizationId = {}]", lovCode, organizationId);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }

    @Override
    public ResponseEntity<String> queryLovValueMeaning(Long organizationId, String lovCode, String lovValue) {
        log.error("Error queryLovValueMeaning, params[lovCode = {}, organizationId = {}, lovValue= {}]", lovCode, organizationId,lovValue);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }

    @Override
    public ResponseEntity<String> queryLovValueMeaning(Long organizationId, String lovCode, Map<String, String> queryLovValueMap) {
        log.error("Error queryLovValueMeaning, params[lovCode = {}, organizationId = {}, map = {} ]", lovCode, organizationId,queryLovValueMap);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }

    @Override
    public ResponseEntity<String> queryUrlLovPage(Long organizationId, String lovCode, Integer page, Integer size, Map<String, String> queryLovValueMap) {
        log.error("Error queryUrlLovPage, params[lovCode = {}, organizationId = {},page = {},size = {}, map = {} ]", lovCode, organizationId, page, size,queryLovValueMap);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }
}
