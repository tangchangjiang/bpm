package org.o2.metadata.app.service.impl;

import org.apache.commons.collections4.CollectionUtils;
import org.hzero.mybatis.helper.SecurityTokenHelper;
import org.o2.metadata.app.service.PosRelCarrierService;
import org.o2.metadata.domain.entity.PosRelCarrier;
import org.o2.metadata.domain.repository.PosRelCarrierRepository;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 服务点关联承运商应用服务默认实现
 *
 * @author tingting.wang@hand-china.com 2019-3-25
 */
@Service
public class PosRelCarrierServiceImpl implements PosRelCarrierService {
    private final PosRelCarrierRepository posRelCarrierRepository;

    public PosRelCarrierServiceImpl(final PosRelCarrierRepository posRelCarrierRepository) {
        this.posRelCarrierRepository = posRelCarrierRepository;
    }

    @Override
    public List<PosRelCarrier> batchMerge(final List<PosRelCarrier> posRelCarriers) {
        final Map<String, Object> map = new HashMap<>(posRelCarriers.size());
        final List<PosRelCarrier> updateList = new ArrayList<>();
        final List<PosRelCarrier> insertList = new ArrayList<>();
        Integer updateIndex = null;
        for (int i = 0; i < posRelCarriers.size(); i++) {
            final PosRelCarrier posRelCarrier = posRelCarriers.get(i);
            posRelCarrier.baseValidate();
            // 数据库查重
            final int isExist = posRelCarrierRepository.isExist(posRelCarrier);
            if (isExist == 1) {
                Assert.isTrue(false, "该服务点存在相同承运商");
            } else if (isExist == 2) {
                Assert.isTrue(false, "存在优先级相同的承运商");
            }
            // list查重
            final String key = String.valueOf(posRelCarrier.getCarrierId())
                    + String.valueOf(posRelCarrier.getPosId());
            Assert.isTrue(map.get(key) == null, "该服务点存在相同承运商");
            if (posRelCarrier.getPosRelCarrierId() != null) {
                SecurityTokenHelper.validToken(posRelCarrier);
                updateList.add(posRelCarrier);
            } else {
                insertList.add(posRelCarrier);
            }
            map.put(key, i);
            if (posRelCarrier.getDefaultFlag() == 1) {
                updateIndex = i;
            }
        }
        final List<PosRelCarrier> resultList = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(updateList)) {
            resultList.addAll(posRelCarrierRepository.batchUpdateByPrimaryKey(updateList));
        }
        if (CollectionUtils.isNotEmpty(insertList)) {
            resultList.addAll(posRelCarrierRepository.batchInsertSelective(insertList));
        }
        if (updateIndex != null) {
            final PosRelCarrier posRelCarrier = posRelCarriers.get(updateIndex);
            posRelCarrierRepository.updateIsDefault(posRelCarrier.getPosRelCarrierId(), posRelCarrier.getPosId());
        }
        return resultList;
    }

}
