package org.o2.metadata.console.infra.feign.impl;

import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import lombok.extern.slf4j.Slf4j;
import org.o2.core.helper.JsonHelper;
import org.o2.metadata.console.api.dto.CurrencyDTO;
import org.o2.metadata.console.infra.feign.CurrencyRemoteService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class CurrencyRemoteServiceImpl implements CurrencyRemoteService {
    @Override
    public ResponseEntity<String> queryCurrency(Long organizationId, String currencyName, String currencyCode, Integer enabledFlag, Integer page, Integer size) {
        log.error("error query currency, params[organizationId = {}]", organizationId);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }
}
