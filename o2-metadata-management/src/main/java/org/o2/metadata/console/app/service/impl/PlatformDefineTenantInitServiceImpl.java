package org.o2.metadata.console.app.service.impl;

import jodd.util.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.hzero.mybatis.domian.Condition;
import org.hzero.mybatis.util.Sqls;
import org.o2.initialize.domain.context.TenantInitContext;
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
    public void tenantInitialize(TenantInitContext context) {
        log.info("initializePlatforms start");
        String platform  =context.getParamMap().get(TenantInitConstants.InitBaseParam.BASE_PLATFORM);
        if (StringUtil.isBlank(platform)) {
            log.info("base_platform is null");
            return;
        }
        // 1. 查询平台租户（所有已启用）
        final List<Platform> platforms = platformRepository.selectByCondition(Condition.builder(Platform.class)
                .andWhere(Sqls.custom()
                        .andEqualTo(Platform.FIELD_TENANT_ID, context.getSourceTenantId())
                        .andIn(Platform.FIELD_PLATFORM_CODE, Arrays.asList(context.getParamMap().get(TenantInitConstants.InitBaseParam.BASE_PLATFORM).split(",")))
                )
                .build());

        if (CollectionUtils.isEmpty(platforms)) {
            log.info("platforms is empty.");
            return;
        }

        // 2. 查询目标租户是否存在数据
        final List<Platform> targetPlatforms = platformRepository.selectByCondition(Condition.builder(Platform.class)
                .andWhere(Sqls.custom()
                        .andEqualTo(Platform.FIELD_TENANT_ID, context.getTargetTenantId())
                .andIn(Platform.FIELD_PLATFORM_CODE, Arrays.asList(context.getParamMap().get(TenantInitConstants.InitBaseParam.BASE_PLATFORM).split(","))))
                .build());

        handleData(targetPlatforms,platforms,context.getTargetTenantId());
        log.info("initializePlatforms finish, tenantId[{}]", context.getTargetTenantId());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void tenantInitializeBusiness(TenantInitContext context) {
        log.info("Business :initializePlatforms start");

        String platform  =context.getParamMap().get(TenantInitConstants.InitBusinessParam.BUSINESS_PLATFORM);
        if (StringUtil.isBlank(platform)) {
            log.info("business_platform is null");
            return;
        }
        // 1. 查询平台租户（所有已启用）
        final List<Platform> platforms = platformRepository.selectByCondition(Condition.builder(Platform.class)
                .andWhere(Sqls.custom()
                        .andEqualTo(Platform.FIELD_TENANT_ID, context.getSourceTenantId())
                        .andIn(Platform.FIELD_PLATFORM_CODE, Arrays.asList(context.getParamMap().get(TenantInitConstants.InitBusinessParam.BUSINESS_PLATFORM).split(",")))
                )
                .build());

        if (CollectionUtils.isEmpty(platforms)) {
            log.info("Business platforms is empty.");
            return;
        }

        // 2. 查询目标租户是否存在数据
        final List<Platform> targetPlatforms = platformRepository.selectByCondition(Condition.builder(Platform.class)
                .andWhere(Sqls.custom()
                        .andEqualTo(Platform.FIELD_TENANT_ID, context.getTargetTenantId())
                        .andIn(Platform.FIELD_PLATFORM_CODE, Arrays.asList(context.getParamMap().get(TenantInitConstants.InitBusinessParam.BUSINESS_PLATFORM).split(","))))
                .build());

        handleData(targetPlatforms,platforms,context.getTargetTenantId());
        log.info("Business initializePlatforms finish, tenantId[{}]", context.getTargetTenantId());
    }

    /**
     *  更新&插入 租户数据
     * @param oldList 目标租户 已存在的
     * @param initList 目标租户 重新初始化
     * @param targetTenantId  目标租户
     */
    private void handleData(List<Platform> oldList ,List<Platform> initList,Long targetTenantId) {
        List<Platform> addList = new ArrayList<>(16);
        List<Platform> updateList = new ArrayList<>(16);
        for (Platform init :initList) {
            String initCode = init.getPlatformCode();
            boolean addFlag = true;
            if (CollectionUtils.isEmpty(oldList)) {
               addList.add(init);
               continue;
            }
            for (Platform old : oldList) {
                String oldCode = old.getPlatformCode();
                if (initCode.equals(oldCode)) {
                    init.setPlatformId(old.getPlatformId());
                    init.setTenantId(old.getTenantId());
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
        addList.forEach(platform -> {
            platform.setPlatformId(null);
            platform.setTenantId(targetTenantId);
        });

        platformRepository.batchInsert(addList);
        platformRepository.batchUpdateByPrimaryKey(updateList);

    }
}
