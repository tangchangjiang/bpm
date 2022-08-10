package org.o2.business.process.management.infra.repository.impl;

import lombok.RequiredArgsConstructor;
import org.o2.business.process.management.domain.repository.BusinessProcessRedisRepository;
import org.springframework.stereotype.Component;

/**
 * @author zhilin.ren@hand-china.com 2022/08/10 15:01
 */
@Component
@RequiredArgsConstructor
public class BusinessProcessRedisRepositoryImpl implements BusinessProcessRedisRepository {



    @Override
    public void updateNodeStatus(String hashKey, String field, String value) {

    }
}
