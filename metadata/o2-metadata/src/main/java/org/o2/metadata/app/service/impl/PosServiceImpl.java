package org.o2.metadata.app.service.impl;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.o2.metadata.api.co.PosStoreInfoCO;
import org.o2.metadata.api.dto.StoreQueryDTO;
import org.o2.metadata.app.service.PosService;
import org.o2.metadata.infra.convertor.PosConverter;
import org.o2.metadata.infra.entity.Pos;
import org.o2.metadata.infra.redis.PosRedis;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author chao.yang05@hand-china.com 2022/4/14
 */
@Service
public class PosServiceImpl implements PosService {

    private final PosRedis posRedis;

    public PosServiceImpl(PosRedis posRedis) {
        this.posRedis = posRedis;
    }

    @Override
    public PosStoreInfoCO getPosPickUpInfo(String posCode, Long tenantId) {
        Pos pos = posRedis.getPosPickUpInfo(posCode, tenantId);
        if (ObjectUtils.isEmpty(pos)) {
            return null;
        }
        return PosConverter.doToCoObject(pos);
    }

    @Override
    public List<PosStoreInfoCO> getStoreInfoList(StoreQueryDTO storeQueryDTO, Long tenantId) {
        List<Pos> posInfoList = posRedis.getStoreInfoList(storeQueryDTO, tenantId);
        List<PosStoreInfoCO> result = new ArrayList<>();
        if (CollectionUtils.isEmpty(posInfoList)) {
            return result;
        }
        return PosConverter.doToCoListObjects(posInfoList);
    }
}
