package org.o2.metadata.infra.convertor;

import org.apache.commons.collections4.CollectionUtils;
import org.o2.metadata.api.co.PosPickUpInfoCO;
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
        posStoreInfoCO.setRegionCode(pos.getRegionCode());
        posStoreInfoCO.setCityCode(pos.getCityCode());
        posStoreInfoCO.setDistrictCode(pos.getDistrictCode());
        posStoreInfoCO.setRegionName(pos.getRegionName());
        posStoreInfoCO.setCityName(pos.getCityName());
        posStoreInfoCO.setDistrictName(pos.getDistrictName());
        posStoreInfoCO.setStreetName(pos.getStreetName());
        posStoreInfoCO.setLongitude(pos.getLongitude());
        posStoreInfoCO.setLatitude(pos.getLatitude());
        posStoreInfoCO.setPhoneNumber(pos.getPhoneNumber());
        posStoreInfoCO.setPickedUpFlag(pos.getPickedUpFlag());
        posStoreInfoCO.setWarehouseCode(pos.getWarehouseCode());
        posStoreInfoCO.setPickUpQuantity(pos.getPickUpQuantity());
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

    public static PosPickUpInfoCO doToPickUpInfoCoObject(Pos pos) {
        if (pos == null) {
            return null;
        }
        PosPickUpInfoCO posPickUpInfoCO = new PosPickUpInfoCO();
        posPickUpInfoCO.setPosCode(pos.getPosCode());
        posPickUpInfoCO.setPosName(pos.getPosName());
        posPickUpInfoCO.setBusinessTime(pos.getBusinessTime());
        posPickUpInfoCO.setRegionCode(pos.getRegionCode());
        posPickUpInfoCO.setCityCode(pos.getCityCode());
        posPickUpInfoCO.setDistrictCode(pos.getDistrictCode());
        posPickUpInfoCO.setRegionName(pos.getRegionName());
        posPickUpInfoCO.setCityName(pos.getCityName());
        posPickUpInfoCO.setDistrictName(pos.getDistrictName());
        posPickUpInfoCO.setStreetName(pos.getStreetName());
        posPickUpInfoCO.setPhoneNumber(pos.getPhoneNumber());
        return posPickUpInfoCO;
    }
}
