package org.o2.metadata.console.app.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.o2.metadata.console.app.service.CarrierTenantInitService;
import org.o2.metadata.console.infra.constant.TenantInitConstants;
import org.o2.metadata.console.infra.entity.Carrier;
import org.o2.metadata.console.infra.redis.CarrierRedis;
import org.o2.metadata.console.infra.repository.CarrierRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * 承运商数据初始化
 *
 * @author yipeng.zhu@hand-china.com 2022-2-11
 */
@Service
@Slf4j
public class CarrierTenantInitServiceImpl implements CarrierTenantInitService {
    private final CarrierRepository carrierRepository;
    private final CarrierRedis carrierRedis;

    public CarrierTenantInitServiceImpl(CarrierRepository carrierRepository, CarrierRedis carrierRedis) {
        this.carrierRepository = carrierRepository;
        this.carrierRedis = carrierRedis;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void tenantInitializeBusiness(long sourceTenantId, Long targetTenantId) {
        //1. 查询源租户
        log.info("Business: carrier start");
        Carrier query = new Carrier();
        query.setTenantId(sourceTenantId);
        query.setCarrierCodes(TenantInitConstants.CarrierBusiness.CARRIERS);
        List<Carrier> sourceCarriers = carrierRepository.listCarrier(query);
        if (CollectionUtils.isEmpty(sourceCarriers)) {
            log.info("Business: carrier is empty.");
        }
        // 2.查询目标租户
        query.setTenantId(targetTenantId);
        List<Carrier> targetCarriers = carrierRepository.listCarrier(query);
        handleData(targetCarriers,sourceCarriers,targetTenantId);
        log.info("Business: carrier finish");
    }

    /**
     *  更新已存在的& 插入需要初始化的
     * @param oldCarriers 已存在的
     * @param initCarriers 需要初始化数据
     * @param targetTenantId  目标租户
     */
    private void handleData( List<Carrier> oldCarriers,  List<Carrier> initCarriers, Long targetTenantId) {
        // 3. 分类更新&插入目标租户数据
        List<Carrier> addList = new ArrayList<>(4);
        List<Carrier> updateList = new ArrayList<>(4);
        for (Carrier init : initCarriers) {
            String initCode = init.getCarrierCode();
            boolean addFlag = true;
            if (CollectionUtils.isEmpty(oldCarriers)) {
                addList.add(init);
                continue;
            }
            for (Carrier old : oldCarriers) {
                String oldCode = old.getCarrierCode();
                if (initCode.equals(oldCode)) {
                    init.setTenantId(targetTenantId);
                    init.setCarrierId(old.getCarrierId());
                    init.setObjectVersionNumber(old.getObjectVersionNumber());
                    updateList.add(init);
                    addFlag = false;
                    break;
                }
            }
            if (addFlag) {
                addList.add(init);
            }
        }
        for (Carrier carrier : addList) {
            carrier.setCarrierId(null);
            carrier.setTenantId(targetTenantId);
        }
        // 4  更新目标租户数据
        carrierRepository.batchInsert(addList);
        carrierRepository.batchUpdateByPrimaryKey(updateList);
        // 5. 更新缓存
        carrierRedis.batchUpdateRedis(targetTenantId);
    }
}
