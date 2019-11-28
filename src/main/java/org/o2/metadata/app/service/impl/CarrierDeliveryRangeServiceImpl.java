package org.o2.metadata.app.service.impl;

import org.apache.commons.collections4.CollectionUtils;
import org.hzero.mybatis.helper.SecurityTokenHelper;
import org.o2.metadata.app.service.CarrierDeliveryRangeService;
import org.o2.metadata.domain.entity.CarrierDeliveryRange;
import org.o2.metadata.domain.repository.CarrierDeliveryRangeRepository;
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

    public CarrierDeliveryRangeServiceImpl(final CarrierDeliveryRangeRepository carrierDeliveryRangeRepository) {
        this.carrierDeliveryRangeRepository = carrierDeliveryRangeRepository;
    }

    @Override
    public List<CarrierDeliveryRange> batchMerge(final List<CarrierDeliveryRange> carrierDeliveryRanges) {
        final Map<String, Object> map = new HashMap<>(carrierDeliveryRanges.size());
        final List<CarrierDeliveryRange> updateList = new ArrayList<>();
        final List<CarrierDeliveryRange> insertList = new ArrayList<>();
        for (int i = 0; i < carrierDeliveryRanges.size(); i++) {
            final CarrierDeliveryRange carrierDeliveryRange = carrierDeliveryRanges.get(i);
            carrierDeliveryRange.baseValidate();
            // 数据库查重
            Assert.isTrue(!carrierDeliveryRange.exist(carrierDeliveryRangeRepository), "该省市区范围内已有承运商");
            // list查重
            final String key = carrierDeliveryRange.getRegionId() + "-"
                    + carrierDeliveryRange.getCityId() + "-" + carrierDeliveryRange.getDistrictId();
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
}
