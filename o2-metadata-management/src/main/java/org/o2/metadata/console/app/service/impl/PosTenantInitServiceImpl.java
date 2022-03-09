package org.o2.metadata.console.app.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.o2.initialize.domain.context.TenantInitContext;
import org.o2.metadata.console.app.service.PosService;
import org.o2.metadata.console.app.service.PosTenantInitService;
import org.o2.metadata.console.infra.constant.TenantInitConstants;
import org.o2.metadata.console.infra.entity.Pos;
import org.o2.metadata.console.infra.entity.PosAddress;
import org.o2.metadata.console.infra.repository.PosAddressRepository;
import org.o2.metadata.console.infra.repository.PosRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 服务点数据初始化
 *
 * @author tingting.wang@hand-china.com 2022-02-10
 */
@Service
@Slf4j
public class PosTenantInitServiceImpl implements PosTenantInitService {
    private final PosService posService;
    private final PosRepository posRepository;
    private final PosAddressRepository addressRepository;

    public PosTenantInitServiceImpl(PosService posService, PosRepository posRepository,
                                    PosAddressRepository addressRepository) {
        this.posService = posService;
        this.posRepository = posRepository;
        this.addressRepository = addressRepository;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void tenantInitializeBusiness(TenantInitContext context) {
        // 1. 查询源租户数据
        log.info("Business: initializePos start, tenantId[{}]", context.getTargetTenantId());
        Pos query = new Pos();
        query.setTenantId(context.getSourceTenantId());
        query.setPosCodes(Arrays.asList(context.getParamMap().get(TenantInitConstants.InitBusinessParam.BUSINESS_POS).split(",")));
        List<Pos> sourcePos = posService.selectByCondition(query);
        if (CollectionUtils.isEmpty(sourcePos)) {
            log.warn("Business data not exists in sourceTenantId[{}]", context.getSourceTenantId());
            return;
        }
        // 2. 查询目标租户数据
        query.setTenantId(context.getTargetTenantId());
        List<Pos> targetPos = posService.selectByCondition(query);
        // 3. 操作数据
        handleData(targetPos,sourcePos,context.getTargetTenantId());
        log.info("Business: initializePos finish");
    }

    /**
     * 处理网网店：更新已存在的，插入未存在的目标数据
     * @param oldPos  已存在的数据
     * @param initPos 初始化的数据
     * @param targetTenantId 目标租户ID
     */
    private void handleData(List<Pos> oldPos, List<Pos> initPos, Long targetTenantId) {
        // 3.1 更新目标数据
        List<Pos> updateList = new ArrayList<>(4);
        // 3.2 插入 目标数据
        List<Pos> addList = new ArrayList<>(4);
        for (Pos init : initPos) {
            String posCode = init.getPosCode();
            boolean addFlag = true;
            if (oldPos.isEmpty()) {
                addList.add(init);
                continue;
            }
            for (Pos old : oldPos) {
                String oldCode = old.getPosCode();
                if (posCode.equals(oldCode)) {
                    init.setTenantId(targetTenantId);
                    init.setObjectVersionNumber(old.getObjectVersionNumber());
                    init.setPosId(old.getPosId());
                    updateList.add(init);
                    addFlag = false;
                    break;
                }
            }
            if (addFlag) {
                addList.add(init);
            }
        }

        // 4. 更新目标库
        posRepository.batchUpdateByPrimaryKey(updateList);
        for (Pos pos : addList) {
            pos.setPosId(null);
            pos.setTenantId(targetTenantId);
            // 关联服务地址
            Long sourceAddressId = pos.getAddressId();
            PosAddress query = new PosAddress();
            query.setPosAddressId(sourceAddressId);
            PosAddress address = addressRepository.selectByPrimaryKey(query);
            if (null == address) {
                address = new PosAddress();
            }
            address.setTenantId(targetTenantId);
            address.setPosAddressId(null);
            addressRepository.insert(address);
            pos.setAddressId(address.getPosAddressId());
            posRepository.insert(pos);

        }
    }
}
