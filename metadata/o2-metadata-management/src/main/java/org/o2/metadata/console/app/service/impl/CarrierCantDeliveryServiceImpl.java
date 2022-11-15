package org.o2.metadata.console.app.service.impl;

import com.google.common.collect.Maps;
import io.choerodon.core.exception.CommonException;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.hzero.core.base.BaseConstants;
import org.hzero.mybatis.domian.Condition;
import org.hzero.mybatis.helper.UniqueHelper;
import org.hzero.mybatis.util.Sqls;
import org.o2.metadata.console.api.dto.RegionQueryLovInnerDTO;
import org.o2.metadata.console.app.service.CarrierCantDeliveryService;
import org.o2.metadata.console.infra.constant.CarrierConstants;
import org.o2.metadata.console.infra.entity.CarrierCantDelivery;
import org.o2.metadata.console.infra.entity.Region;
import org.o2.metadata.console.infra.repository.CarrierCantDeliveryRepository;
import org.o2.metadata.console.infra.repository.RegionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 承运商不可送达范围应用服务默认实现
 *
 * @author zhilin.ren@hand-china.com 2022-10-10 13:44:09
 */
@Service
@RequiredArgsConstructor
public class CarrierCantDeliveryServiceImpl implements CarrierCantDeliveryService {
    private final CarrierCantDeliveryRepository carrierCantDeliveryRepository;

    private final RegionRepository regionRepository;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public CarrierCantDelivery save(CarrierCantDelivery carrierCantDelivery) {
        // 合法性校验
        validLegality(carrierCantDelivery, 1);

        if (carrierCantDelivery.getCarrierCantDeliveryId() == null) {
            carrierCantDeliveryRepository.insertSelective(carrierCantDelivery);
        } else {
            carrierCantDeliveryRepository.updateOptional(carrierCantDelivery,
                    CarrierCantDelivery.FIELD_CARRIER_CODE,
                    CarrierCantDelivery.FIELD_TENANT_ID,
                    CarrierCantDelivery.FIELD_COUNTRY_CODE,
                    CarrierCantDelivery.FIELD_REGION_CODE,
                    CarrierCantDelivery.FIELD_CITY_CODE,
                    CarrierCantDelivery.FIELD_DISTRICT_CODE
            );
        }
        return carrierCantDelivery;
    }

    @Override
    public List<CarrierCantDelivery> list(CarrierCantDelivery query) {

        List<CarrierCantDelivery> list = carrierCantDeliveryRepository.list(query);

        if (list.isEmpty()) {
            return list;
        }
        List<String> regionCodes = new ArrayList<>();
        for (CarrierCantDelivery carrierCantDelivery : list) {
            regionCodes.add(carrierCantDelivery.getRegionCode());
            regionCodes.add(carrierCantDelivery.getCityCode());
            regionCodes.add(carrierCantDelivery.getDistrictCode());

        }
        RegionQueryLovInnerDTO dto = new RegionQueryLovInnerDTO();
        dto.setRegionCodes(regionCodes);
        dto.setTenantId(query.getTenantId());
        List<Region> regionList = regionRepository.listRegionLov(dto, query.getTenantId());
        if (regionList.isEmpty()) {
            return list;
        }
        Map<String, String> map = Maps.newHashMapWithExpectedSize(regionCodes.size());
        for (Region region : regionList) {
            map.put(region.getRegionCode(), region.getRegionName());
        }
        map.put(regionList.get(0).getCountryCode(), regionList.get(0).getCountryName());
        for (CarrierCantDelivery bean : list) {
            bean.setRegionName(map.get(bean.getRegionCode()));
            bean.setCityName(map.get(bean.getCityCode()));
            bean.setDistrictName(map.get(bean.getDistrictCode()));
            bean.setCountryName(map.get(bean.getCountryCode()));
        }
        return list;
    }

    /**
     * @param carrierCantDelivery 参数
     * @param length              1:市，2:区
     */
    protected void validLegality(CarrierCantDelivery carrierCantDelivery, int length) {

        // 仅有省的情况
        if (StringUtils.isNotBlank(carrierCantDelivery.getRegionCode()) && StringUtils.isAllBlank(carrierCantDelivery.getCityCode(),
                carrierCantDelivery.getDistrictCode())) {

            int result = carrierCantDeliveryRepository.selectCountByCondition(Condition.builder(CarrierCantDelivery.class)
                    .andWhere(Sqls.custom()
                            .andEqualTo(CarrierCantDelivery.FIELD_CARRIER_CODE, carrierCantDelivery.getCarrierCode())
                            .andEqualTo(CarrierCantDelivery.FIELD_TENANT_ID, carrierCantDelivery.getTenantId())
                            .andEqualTo(CarrierCantDelivery.FIELD_COUNTRY_CODE, carrierCantDelivery.getCountryCode())
                            .andEqualTo(CarrierCantDelivery.FIELD_REGION_CODE, carrierCantDelivery.getRegionCode())
                            .andNotEqualTo(CarrierCantDelivery.FIELD_CARRIER_CANT_DELIVERY_ID, carrierCantDelivery.getCarrierCantDeliveryId(), true)
                    ).build());
            if (result > 0) {
                // 存在，不允许新建
                throw new CommonException(CarrierConstants.ErrorCode.ERROR_EXISTS_REGION_DATA);
            }
            return;
        }

        if (BaseConstants.Digital.TWO == length) {
            if (StringUtils.isBlank(carrierCantDelivery.getDistrictCode())) {
                // 仅存在省市的情况
                int result = carrierCantDeliveryRepository.selectCountByCondition(Condition.builder(CarrierCantDelivery.class)
                        .andWhere(Sqls.custom()
                                .andEqualTo(CarrierCantDelivery.FIELD_CARRIER_CODE, carrierCantDelivery.getCarrierCode())
                                .andEqualTo(CarrierCantDelivery.FIELD_TENANT_ID, carrierCantDelivery.getTenantId())
                                .andEqualTo(CarrierCantDelivery.FIELD_COUNTRY_CODE, carrierCantDelivery.getCountryCode())
                                .andEqualTo(CarrierCantDelivery.FIELD_REGION_CODE, carrierCantDelivery.getRegionCode())
                                .andEqualTo(CarrierCantDelivery.FIELD_CITY_CODE, carrierCantDelivery.getCityCode())
                                .andNotEqualTo(CarrierCantDelivery.FIELD_CARRIER_CANT_DELIVERY_ID, carrierCantDelivery.getCarrierCantDeliveryId(),
                                        true)
                        ).build());
                if (result > 0) {
                    // 存在，不允许新建
                    throw new CommonException(CarrierConstants.ErrorCode.ERROR_EXISTS_CITY_DATA);
                }
                return;
            }

            // 校验是否存在 cityCode != null and districtCode== null 的数据
            int result = carrierCantDeliveryRepository.selectCountByCondition(Condition.builder(CarrierCantDelivery.class)
                    .andWhere(Sqls.custom()
                            .andEqualTo(CarrierCantDelivery.FIELD_CARRIER_CODE, carrierCantDelivery.getCarrierCode())
                            .andEqualTo(CarrierCantDelivery.FIELD_TENANT_ID, carrierCantDelivery.getTenantId())
                            .andEqualTo(CarrierCantDelivery.FIELD_COUNTRY_CODE, carrierCantDelivery.getCountryCode())
                            .andEqualTo(CarrierCantDelivery.FIELD_REGION_CODE, carrierCantDelivery.getRegionCode())
                            .andEqualTo(CarrierCantDelivery.FIELD_CITY_CODE, carrierCantDelivery.getCityCode())
                            .andIsNull(CarrierCantDelivery.FIELD_DISTRICT_CODE)
                            .andNotEqualTo(CarrierCantDelivery.FIELD_CARRIER_CANT_DELIVERY_ID, carrierCantDelivery.getCarrierCantDeliveryId(), true)
                    ).build());
            if (result > 0) {
                // 存在，不允许新建
                throw new CommonException(CarrierConstants.ErrorCode.ERROR_EXISTS_CITY_DATA);
            }

            // 唯一性校验
            if (!UniqueHelper.valid(carrierCantDelivery, CarrierCantDelivery.O2MD_CARRIER_CANT_DELIVERY_U1)) {
                throw new CommonException(CarrierConstants.ErrorCode.ERROR_EXISTS_DISTRICT_DATA);
            }

        } else if (BaseConstants.Digital.ONE == length) {
            if (StringUtils.isBlank(carrierCantDelivery.getCityCode())) {
                return;
            }

            // 校验是否存在 regionCode != null and cityCode== null 的数据
            int result = carrierCantDeliveryRepository.selectCountByCondition(Condition.builder(CarrierCantDelivery.class)
                    .andWhere(Sqls.custom()
                            .andEqualTo(CarrierCantDelivery.FIELD_CARRIER_CODE, carrierCantDelivery.getCarrierCode())
                            .andEqualTo(CarrierCantDelivery.FIELD_TENANT_ID, carrierCantDelivery.getTenantId())
                            .andEqualTo(CarrierCantDelivery.FIELD_COUNTRY_CODE, carrierCantDelivery.getCountryCode())
                            .andEqualTo(CarrierCantDelivery.FIELD_REGION_CODE, carrierCantDelivery.getRegionCode())
                            .andIsNull(CarrierCantDelivery.FIELD_CITY_CODE)
                            .andIsNull(CarrierCantDelivery.FIELD_DISTRICT_CODE)
                            .andNotEqualTo(CarrierCantDelivery.FIELD_CARRIER_CANT_DELIVERY_ID, carrierCantDelivery.getCarrierCantDeliveryId(), true)
                    ).build());
            if (result > 0) {
                // 存在，不允许新建
                throw new CommonException(CarrierConstants.ErrorCode.ERROR_EXISTS_REGION_DATA);
            }
            validLegality(carrierCantDelivery, ++length);
        }
    }
}
