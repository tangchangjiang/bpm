package org.o2.metadata.console.app.service.impl;

import com.google.common.collect.Maps;
import org.apache.commons.collections4.CollectionUtils;
import org.hzero.mybatis.helper.SecurityTokenHelper;
import org.o2.metadata.console.api.dto.RegionQueryLovDTO;
import org.o2.metadata.console.app.service.CarrierDeliveryRangeService;
import org.o2.metadata.console.infra.entity.CarrierDeliveryRange;
import org.o2.metadata.console.infra.entity.Region;
import org.o2.metadata.console.infra.repository.CarrierDeliveryRangeRepository;
import org.o2.metadata.console.infra.repository.RegionRepository;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 承运商送达范围应用服务默认实现
 *
 * @author tingting.wang@hand-china.com 2019-3-25
 */
@Service
public class CarrierDeliveryRangeServiceImpl implements CarrierDeliveryRangeService {
    private final CarrierDeliveryRangeRepository carrierDeliveryRangeRepository;
    private final RegionRepository regionRepository;

    public CarrierDeliveryRangeServiceImpl(final CarrierDeliveryRangeRepository carrierDeliveryRangeRepository,
                                           RegionRepository regionRepository) {
        this.carrierDeliveryRangeRepository = carrierDeliveryRangeRepository;
        this.regionRepository = regionRepository;
    }

    @Override
    public List<CarrierDeliveryRange> listCarrierDeliveryRanges(CarrierDeliveryRange carrierDeliveryRange) {
        List<CarrierDeliveryRange> list =  carrierDeliveryRangeRepository.list(carrierDeliveryRange);
        if (list.isEmpty()) {
            return list;
        }
        List<String> regionCodes = new ArrayList<>();
        for (CarrierDeliveryRange deliveryRange : list) {
            regionCodes.add(deliveryRange.getRegionCode());
            regionCodes.add(deliveryRange.getCityCode());
            regionCodes.add(deliveryRange.getDistrictCode());

        }
        RegionQueryLovDTO dto = new RegionQueryLovDTO();
        dto.setRegionCodes(regionCodes);
        dto.setTenantId(carrierDeliveryRange.getTenantId());
        List<Region> regionList = regionRepository.listRegionLov(dto,carrierDeliveryRange.getTenantId());
        if (regionList.isEmpty()) {
            return list;
        }
        Map<String,String> map = Maps.newHashMapWithExpectedSize(regionCodes.size());
        for (Region region : regionList) {
            map.put(region.getRegionCode(),region.getRegionName());
        }
        map.put(regionList.get(0).getCountryCode(),regionList.get(0).getCountryName());
        for (CarrierDeliveryRange bean : list) {
            bean.setRegionName(map.get(bean.getRegionCode()));
            bean.setCityName(map.get(bean.getCityCode()));
            bean.setDistrictName(map.get(bean.getDistrictCode()));
            bean.setCountryName(map.get(bean.getCountryCode()));

        }
        return list;
    }

    @Override
    public List<CarrierDeliveryRange> batchMerge(Long organizationId,final List<CarrierDeliveryRange> carrierDeliveryRanges) {
        final Map<String, Object> map = new HashMap<>(carrierDeliveryRanges.size());
        final List<CarrierDeliveryRange> updateList = new ArrayList<>();
        final List<CarrierDeliveryRange> insertList = new ArrayList<>();
        for (int i = 0; i < carrierDeliveryRanges.size(); i++) {
            final CarrierDeliveryRange carrierDeliveryRange = carrierDeliveryRanges.get(i);
            carrierDeliveryRange.setTenantId(organizationId);
            carrierDeliveryRange.baseValidate();
            // 数据库查重
            Assert.isTrue(!carrierDeliveryRange.exist(carrierDeliveryRangeRepository), "该省市区范围内已有承运商");
            // list查重
            final String key = carrierDeliveryRange.getRegionCode() + "-"
                    + carrierDeliveryRange.getCityCode() + "-" + carrierDeliveryRange.getDistrictCode();
            Assert.isTrue(map.get(key) == null, "该省市区范围内已有承运商");
            if (carrierDeliveryRange.getDeliveryRangeId() != null) {
                SecurityTokenHelper.validToken(carrierDeliveryRange);
                updateList.add(carrierDeliveryRange);
            } else {
                insertList.add(carrierDeliveryRange);
            }
            map.put(key, i);
        }
        final List<CarrierDeliveryRange> resultList = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(updateList)) {
            resultList.addAll(carrierDeliveryRangeRepository.batchUpdateByPrimaryKey(updateList));
        }
        if (CollectionUtils.isNotEmpty(insertList)) {
            resultList.addAll(carrierDeliveryRangeRepository.batchInsertSelective(insertList));
        }
        return resultList;
    }

    @Override
    public CarrierDeliveryRange carrierDeliveryRangeDetail(Long deliveryRangeId, Long tenantId) {
        CarrierDeliveryRange query = new CarrierDeliveryRange();
        query.setDeliveryRangeId(deliveryRangeId);
        query.setTenantId(tenantId);
        List<CarrierDeliveryRange> list = this.listCarrierDeliveryRanges(query);
        return list.isEmpty()  ? null : list.get(0);
    }
}
