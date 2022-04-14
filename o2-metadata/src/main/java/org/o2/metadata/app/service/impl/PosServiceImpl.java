package org.o2.metadata.app.service.impl;

import org.apache.commons.lang3.ObjectUtils;
import org.o2.metadata.api.co.PosPickUpInfoCO;
import org.o2.metadata.api.co.PosStoreInfoCO;
import org.o2.metadata.api.dto.StoreQueryDTO;
import org.o2.metadata.app.service.PosService;
import org.o2.metadata.infra.convertor.PosConverter;
import org.o2.metadata.infra.entity.Pos;
import org.o2.metadata.infra.redis.PosRedis;
import org.springframework.stereotype.Service;

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
    public PosPickUpInfoCO getPosPickUpInfo(String posCode, Long tenantId) {
        Pos pos = posRedis.getPosPickUpInfo(posCode, tenantId);
        if (ObjectUtils.isEmpty(pos)) {
            return null;
        }
        PosPickUpInfoCO posPickUpInfoCO = new PosPickUpInfoCO();
        posPickUpInfoCO.setPosCode(pos.getPosCode());
        posPickUpInfoCO.setPosName(pos.getPosName());
        posPickUpInfoCO.setBusinessTime(pos.getBusinessTime());
        posPickUpInfoCO.setStreetName(pos.getStreetName());
        posPickUpInfoCO.setPhoneNumber(pos.getPhoneNumber());
        return posPickUpInfoCO;
    }

    @Override
    public List<PosStoreInfoCO> getStoreInfoList(StoreQueryDTO storeQueryDTO, Long tenantId) {
        List<Pos> posInfoList = posRedis.getStoreInfoList(storeQueryDTO, tenantId);
        return PosConverter.doToCoListObjects(posInfoList);
    }
}
