package org.o2.metadata.console.app.service.impl;

import org.apache.commons.collections4.CollectionUtils;
import org.hzero.mybatis.domian.Condition;
import org.hzero.mybatis.helper.SecurityTokenHelper;
import org.hzero.mybatis.util.Sqls;
import org.o2.metadata.console.app.service.CarrierService;
import org.o2.metadata.core.domain.entity.Carrier;
import org.o2.metadata.core.domain.entity.CarrierDeliveryRange;
import org.o2.metadata.core.domain.entity.PosRelCarrier;
import org.o2.metadata.core.domain.repository.CarrierDeliveryRangeRepository;
import org.o2.metadata.core.domain.repository.CarrierRepository;
import org.o2.metadata.core.domain.repository.PosRelCarrierRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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
    private final CarrierDeliveryRangeRepository carrierDeliveryRangeRepository;
    private final PosRelCarrierRepository posRelCarrierRepository;


    public CarrierServiceImpl(final CarrierRepository carrierRepository,
                              final CarrierDeliveryRangeRepository carrierDeliveryRangeRepository,
                              final PosRelCarrierRepository posRelCarrierRepository) {
        this.carrierRepository = carrierRepository;
        this.carrierDeliveryRangeRepository = carrierDeliveryRangeRepository;
        this.posRelCarrierRepository = posRelCarrierRepository;
    }

    @Override
    public List<Carrier> batchUpdate(Long organizationId, final List<Carrier> carrierList) {
        carrierList.forEach(carrier -> {
            carrier.setTenantId(organizationId);
        });
        checkData(carrierList, true);
        return carrierRepository.batchUpdateByPrimaryKey(carrierList);
    }

    @Override
    public List<Carrier> batchMerge(Long organizationId, final List<Carrier> carrierList) {
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

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void batchDelete(Long organizationId, List<Carrier> carrierList) {
        for (final Carrier carrier : carrierList) {
            carrier.setTenantId(organizationId);
            if (carrier.getCarrierId() != null) {
                Long carrierId = carrier.getCarrierId();
                //删除承运商送达范围
                final List<CarrierDeliveryRange> list = carrierDeliveryRangeRepository.selectByCondition(Condition.builder(CarrierDeliveryRange.class)
                        .andWhere(Sqls.custom().andEqualTo(CarrierDeliveryRange.FIELD_TENANT_ID, organizationId)
                                .andEqualTo(CarrierDeliveryRange.FIELD_CARRIER_ID, carrierId)).build());
                carrierDeliveryRangeRepository.batchDeleteByPrimaryKey(list);
                //删除服务点关联承运商
                final List<PosRelCarrier> posRelCarrierList = posRelCarrierRepository.selectByCondition(Condition.builder(PosRelCarrier.class).andWhere(Sqls.custom()
                        .andEqualTo(PosRelCarrier.FIELD_TENANT_ID, organizationId).andEqualTo(PosRelCarrier.FIELD_CARRIER_ID, carrierId)).build());
                posRelCarrierRepository.batchDeleteByPrimaryKey(posRelCarrierList);
            }
        }
        carrierRepository.batchDeleteByPrimaryKey(carrierList);
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
