package org.o2.metadata.infra.convertor;

import org.apache.commons.collections4.CollectionUtils;
import org.o2.metadata.api.co.PosStoreInfoCO;
import org.o2.metadata.infra.entity.Pos;

import java.util.ArrayList;
import java.util.List;

/**
 * 服务点
 *
 * @author chao.yang05@hand-china.com 2022/4/14
 */
public class PosConverter {

    private PosConverter() {};

    public static PosStoreInfoCO doToCoObject(Pos pos) {
        if (pos == null) {
            return null;
        }
        PosStoreInfoCO posStoreInfoCO = new PosStoreInfoCO();
        posStoreInfoCO.setPosCode(pos.getPosCode());
        posStoreInfoCO.setPosName(pos.getPosName());
        posStoreInfoCO.setPosStatusCode(pos.getPosStatusCode());
        posStoreInfoCO.setPosTypeCode(pos.getPosTypeCode());
        posStoreInfoCO.setBusinessTypeCode(pos.getBusinessTypeCode());
        posStoreInfoCO.setBusinessTime(pos.getBusinessTime());
        posStoreInfoCO.setCityCode(pos.getCityCode());
        posStoreInfoCO.setRegionCode(pos.getRegionCode());
        posStoreInfoCO.setCityCode(pos.getCityCode());
        posStoreInfoCO.setRegionName(pos.getRegionName());
        posStoreInfoCO.setCityName(pos.getCityName());
        posStoreInfoCO.setDistrictName(pos.getDistrictName());
        posStoreInfoCO.setDistrictCode(pos.getDistrictCode());
        posStoreInfoCO.setStreetName(pos.getStreetName());
        posStoreInfoCO.setLongitude(pos.getLongitude());
        posStoreInfoCO.setLatitude(pos.getLatitude());
        posStoreInfoCO.setPhoneNumber(pos.getPhoneNumber());
        posStoreInfoCO.setPickedUpFlag(pos.getPickedUpFlag());
        return posStoreInfoCO;
    }

    public static List<PosStoreInfoCO> doToCoListObjects(List<Pos> posList) {
        List<PosStoreInfoCO> posStoreInfoCOList = new ArrayList<>();
        if (CollectionUtils.isEmpty(posList)) {
            return null;
        }
        for (Pos pos : posList) {
            posStoreInfoCOList.add(doToCoObject(pos));
        }
        return posStoreInfoCOList;
    }
}
