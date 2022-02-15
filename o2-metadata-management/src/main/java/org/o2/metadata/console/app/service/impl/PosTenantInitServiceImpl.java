package org.o2.metadata.console.app.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.o2.metadata.console.app.service.PosService;
import org.o2.metadata.console.app.service.PosTenantInitService;
import org.o2.metadata.console.infra.constant.TenantInitConstants;
import org.o2.metadata.console.infra.entity.Pos;
import org.o2.metadata.console.infra.repository.PosRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
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

    public PosTenantInitServiceImpl(PosService posService, PosRepository posRepository) {
        this.posService = posService;
        this.posRepository = posRepository;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void tenantInitializeBusiness(long sourceTenantId, Long targetTenantId) {
        // 1. 查询源租户数据
        log.info("Business: initializePos start, tenantId[{}]", targetTenantId);
        Pos query = new Pos();
        query.setTenantId(sourceTenantId);
        query.setPosCodes(TenantInitConstants.InitPosBusiness.POS_CODE);
        List<Pos> sourcePos = posService.selectByCondition(query);
        if (CollectionUtils.isEmpty(sourcePos)) {
            log.warn("Business data not exists in sourceTenantId[{}]", sourceTenantId);
            return;
        }
        // 2. 查询目标租户数据
        query.setTenantId(targetTenantId);
        List<Pos> targetPos = posService.selectByCondition(query);
        // 3. 操作数据
        handleData(targetPos,sourcePos,targetTenantId);
        log.info("Business: initializePos finish");
    }

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
        addList.forEach(pos -> {
            pos.setPosId(null);
            pos.setTenantId(targetTenantId);
        });
        // 4. 更新目标库
        posRepository.batchUpdateByPrimaryKey(updateList);
        posRepository.batchInsert(addList);

    }
}
