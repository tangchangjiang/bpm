package org.o2.metadata.console.app.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.hzero.mybatis.domian.Condition;
import org.hzero.mybatis.util.Sqls;
import org.o2.metadata.console.app.service.PlatformDefineTenantInitService;
import org.o2.metadata.console.infra.constant.TenantInitConstants;
import org.o2.metadata.console.infra.entity.Platform;
import org.o2.metadata.console.infra.repository.PlatformRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * description
 *
 * @author zhanpeng.jiang@hand-china.com
 * @date 2021/12/17 16:12
 */
@Slf4j
@Service
public class PlatformDefineTenantInitServiceImpl implements PlatformDefineTenantInitService {

    private final PlatformRepository platformRepository;

    public PlatformDefineTenantInitServiceImpl(PlatformRepository platformRepository) {
        this.platformRepository = platformRepository;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void tenantInitialize(Long sourceTenantId, Long targetTenantId) {
        log.info("initializePlatforms start");
        // 1. 查询平台租户（所有已启用）
        final List<Platform> platforms = platformRepository.selectByCondition(Condition.builder(Platform.class)
                .andWhere(Sqls.custom()
                        .andEqualTo(Platform.FIELD_TENANT_ID, sourceTenantId)
                        .andIn(Platform.FIELD_PLATFORM_CODE, TenantInitConstants.PlatformBasis.PLATFORM_CODE)
                )
                .build());

        if (CollectionUtils.isEmpty(platforms)) {
            log.info("platforms is empty.");
            return;
        }

        // 2. 查询目标租户是否存在数据
        final List<Platform> targetPlatforms = platformRepository.selectByCondition(Condition.builder(Platform.class)
                .andWhere(Sqls.custom()
                        .andEqualTo(Platform.FIELD_TENANT_ID, targetTenantId)
                .andIn(Platform.FIELD_PLATFORM_CODE, TenantInitConstants.PlatformBasis.PLATFORM_CODE))
                .build());

        handleData(targetPlatforms,platforms,targetTenantId);
        log.info("initializePlatforms finish, tenantId[{}]", targetTenantId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void tenantInitializeBusiness(Long sourceTenantId, Long targetTenantId) {
        log.info("Business :initializePlatforms start");
        // 1. 查询平台租户（所有已启用）
        final List<Platform> platforms = platformRepository.selectByCondition(Condition.builder(Platform.class)
                .andWhere(Sqls.custom()
                        .andEqualTo(Platform.FIELD_TENANT_ID, sourceTenantId)
                        .andIn(Platform.FIELD_PLATFORM_CODE, TenantInitConstants.PlatformBusiness.PLATFORM_CODE)
                )
                .build());

        if (CollectionUtils.isEmpty(platforms)) {
            log.info("Business platforms is empty.");
            return;
        }

        // 2. 查询目标租户是否存在数据
        final List<Platform> targetPlatforms = platformRepository.selectByCondition(Condition.builder(Platform.class)
                .andWhere(Sqls.custom()
                        .andEqualTo(Platform.FIELD_TENANT_ID, targetTenantId)
                        .andIn(Platform.FIELD_PLATFORM_CODE, TenantInitConstants.PlatformBusiness.PLATFORM_CODE))
                .build());

        handleData(targetPlatforms,platforms,targetTenantId);
        log.info("Business initializePlatforms finish, tenantId[{}]", targetTenantId);
    }

    private void handleData(List<Platform> oldList ,List<Platform> initList,Long targetTenantId) {
        List<Platform> addList = new ArrayList<>(16);
        List<Platform> updateList = new ArrayList<>(16);
        for (Platform init :initList) {
            String initCode = init.getPlatformCode();
            boolean addFlag = true;
            if (CollectionUtils.isNotEmpty(oldList)) {
               addList.add(init);
               continue;
            }
            for (Platform old : oldList) {
                String oldCode = init.getPlatformCode();
                if (initCode.equals(oldCode)) {
                    addFlag = false;
                    init.setPlatformId(old.getPlatformId());
                    init.setTenantId(targetTenantId);
                    init.setObjectVersionNumber(old.getObjectVersionNumber());
                    updateList.add(init);
                    break;
                }
            }
            if (addFlag) {
                addList.add(init);
            }
        }
        addList.forEach(platform -> {
            platform.setPlatformId(null);
            platform.setTenantId(targetTenantId);
        });

        platformRepository.batchInsert(addList);
        platformRepository.batchUpdateByPrimaryKey(updateList);

    }
}
