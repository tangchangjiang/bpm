package org.o2.metadata.client.infra.feign.fallback;

import lombok.extern.slf4j.Slf4j;
import org.o2.metadata.client.domain.dto.IamUserQueryInnerDTO;
import org.o2.metadata.client.infra.feign.IamUserRemoteService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

/**
 *
 * 用户信息
 *
 * @author yipeng.zhu@hand-china.com 2021-11-01
 **/
@Slf4j
@Service
public class IamUserRemoteServiceImpl implements IamUserRemoteService {
    @Override
    public ResponseEntity<String> listIamUser(Long organizationId, IamUserQueryInnerDTO queryInner) {
        log.error("Error listOnlineShopRelWarehouses, params[tenantId = {}, queryInner = {}]",organizationId, queryInner);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }
}
