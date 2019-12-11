package org.o2.metadata.console.app.service.impl;

import org.apache.commons.collections4.CollectionUtils;
import org.hzero.mybatis.helper.SecurityTokenHelper;
import org.o2.metadata.console.app.service.CarrierService;
import org.o2.metadata.core.domain.entity.Carrier;
import org.o2.metadata.core.domain.repository.CarrierRepository;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 承运商应用服务默认实现
 *
 * @author tingting.wang@hand-china.com 2019-3-25
 */
@Service
public class CarrierServiceImpl implements CarrierService {
    private final CarrierRepository carrierRepository;

    public CarrierServiceImpl(final CarrierRepository carrierRepository) {
        this.carrierRepository = carrierRepository;
    }

    @Override
    public List<Carrier> batchUpdate(Long organizationId,final List<Carrier> carrierList) {
        carrierList.forEach(carrier -> {
            carrier.setTenantId(organizationId);
        });
        checkData(carrierList, true);
        return carrierRepository.batchUpdateByPrimaryKey(carrierList);
    }

    @Override
    public List<Carrier> batchMerge(Long organizationId,final List<Carrier> carrierList) {
        final Map<String, Object> map = new HashMap<>(carrierList.size());
        final List<Carrier> updateList = new ArrayList<>();
        final List<Carrier> insertList = new ArrayList<>();
        for (int i = 0; i < carrierList.size(); i++) {
            Carrier carrier = carrierList.get(i);
            carrier.setTenantId(organizationId);
            carrier.validate();
            // 数据库查重
            Assert.isTrue(!carrier.exist(carrierRepository), "存在相同的承运商");
            // list查重
            Assert.isTrue(map.get(carrier.getCarrierCode()) == null, "存在相同的承运商");
            if (carrier.getCarrierId() != null) {
                SecurityTokenHelper.validToken(carrier);
                updateList.add(carrier);
            } else {
                insertList.add(carrier);
            }
            map.put(carrier.getCarrierCode(), i);
        }
        final List<Carrier> resultList = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(updateList)) {
            resultList.addAll(carrierRepository.batchUpdateByPrimaryKey(updateList));
        }
        if (CollectionUtils.isNotEmpty(insertList)) {
            resultList.addAll(carrierRepository.batchInsertSelective(insertList));
        }
        return resultList;
    }

    /**
     * 校验查重
     *
     * @param carrieies
     * @param isCheckId
     */
    private void checkData(final List<Carrier> carrieies, final boolean isCheckId) {
        final Map<String, Object> map = new HashMap<>(carrieies.size());
        for (int i = 0; i < carrieies.size(); i++) {
            final Carrier carrier = carrieies.get(i);
            if (isCheckId) {
                Assert.notNull(carrier.getCarrierId(), "carrierId must not be null");
            }
            carrier.validate();
            // 数据库查重
            Assert.isTrue(!carrier.exist(carrierRepository), "could not exist same carrier");
            // list查重
            Assert.isTrue(map.get(carrier.getCarrierCode()) == null, "could not exist same carrier");
            map.put(carrier.getCarrierCode(), i);
        }
    }

}
