package org.o2.metadata.console.app.service.impl;

import org.apache.commons.collections.CollectionUtils;
import org.hzero.core.base.BaseConstants;
import org.hzero.mybatis.domian.Condition;
import org.hzero.mybatis.util.Sqls;
import org.o2.cache.util.CollectionCacheHelper;
import org.o2.core.O2CoreConstants;
import org.o2.core.exception.O2CommonException;
import org.o2.metadata.console.api.co.PlatformCO;
import org.o2.metadata.console.api.dto.MerchantInfoDTO;
import org.o2.metadata.console.api.dto.PlatformQueryInnerDTO;
import org.o2.metadata.console.app.service.PlatformService;
import org.o2.metadata.console.infra.constant.MetadataConstants;
import org.o2.metadata.console.infra.constant.PlatformConstants;
import org.o2.metadata.console.infra.convertor.PlatformConverter;
import org.o2.metadata.console.infra.entity.Platform;
import org.o2.metadata.console.infra.entity.PlatformInfoMapping;
import org.o2.metadata.console.infra.repository.PlatformInfoMappingRepository;
import org.o2.metadata.console.infra.repository.PlatformRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 平台定义表应用服务默认实现
 *
 * @author zhilin.ren@hand-china.com 2021-08-02 11:11:28
 */
@Service
public class PlatformServiceImpl implements PlatformService {

    private final PlatformRepository platformRepository;
    private final PlatformInfoMappingRepository platformInfoMappingRepository;

    public PlatformServiceImpl(PlatformRepository platformRepository,
                               PlatformInfoMappingRepository platformInfoMappingRepository) {
        this.platformRepository = platformRepository;
        this.platformInfoMappingRepository = platformInfoMappingRepository;
    }

    @Override
    public Platform save(Platform platform) {
        //保存平台定义表
        if (platform.getPlatformId() == null) {
            validCodeUnique(platform);
            validNameUnique(platform);
            platformRepository.insertSelective(platform);
        } else {
            Platform original = platformRepository.selectByPrimaryKey(platform);
            if (!original.getPlatformCode().equals(platform.getPlatformCode())) {
                throw new O2CommonException(null, PlatformConstants.ErrorCode.ERROR_PLATFORM_CODE_UPDATE,
                        PlatformConstants.ErrorCode.ERROR_PLATFORM_CODE_UPDATE);
            }
            if (!original.getPlatformName().equals(platform.getPlatformName())) {
                validNameUnique(platform);
            }
            platformRepository.updateOptional(platform,
                    Platform.FIELD_PLATFORM_CODE,
                    Platform.FIELD_PLATFORM_NAME,
                    Platform.FIELD_ACTIVE_FLAG,
                    Platform.FIELD_TENANT_ID,
                    Platform.FIELD_PLATFORM_TYPE_CODE
            );
        }
        return platform;
    }

    @Override
    public Map<String, PlatformCO> selectCondition(PlatformQueryInnerDTO queryInnerDTO) {
        Map<String, PlatformCO> map = new HashMap<>(16);
        Map<String, List<PlatformInfoMapping>> result = CollectionCacheHelper.getCache(MetadataConstants.MappingCacheName.METADATA_CACHE_NAME,
                String.format(PlatformConstants.CacheKeyPrefix.PLATFORM_CACHE_MAPPING_KEY_PREFIX, queryInnerDTO.getTenantId(),
                        queryInnerDTO.getInfTypeCode()),
                queryInnerDTO.getPlatformCodes(), k -> {
                    queryInnerDTO.setPlatformCodes(new ArrayList<>(k));
                    List<PlatformInfoMapping> list = platformInfoMappingRepository.selectCondition(queryInnerDTO);
                    return list.stream().collect(Collectors.groupingBy(PlatformInfoMapping::getPlatformCode));
                });
        for (Map.Entry<String, List<PlatformInfoMapping>> entry : result.entrySet()) {
            String k = entry.getKey();
            List<PlatformInfoMapping> value = entry.getValue();
            PlatformCO co = new PlatformCO();
            co.setPlatformCode(k);
            co.setPlatformName(value.get(0).getPlatformName());
            co.setPlatformInfoMappings(PlatformConverter.poToCoListObjects(value));
            map.put(k, co);
        }
        return map;
    }

    @Override
    public Platform buildPlatform(MerchantInfoDTO merchantInfo) {
        Platform platform = new Platform();
        platform.setPlatformCode(O2CoreConstants.PlatformFrom.OW);
        platform.setPlatformName(merchantInfo.getOnlineShopName());
        platform.setPlatformTypeCode(PlatformConstants.PlatformType.E_COMMERCE_PLATFORM);
        platform.setActiveFlag(BaseConstants.Flag.YES);
        platform.setTenantId(merchantInfo.getTenantId());
        return platform;
    }

    protected void validNameUnique(Platform platform) {
        // 唯一性校验
        Condition condition = Condition.builder(Platform.class).andWhere(Sqls.custom()
                .andEqualTo(Platform.FIELD_PLATFORM_NAME, platform.getPlatformName())
                .andEqualTo(Platform.FIELD_TENANT_ID, platform.getTenantId())
        ).build();
        List<Platform> platforms = platformRepository.selectByCondition(condition);
        if (CollectionUtils.isNotEmpty(platforms)) {
            throw new O2CommonException(null, PlatformConstants.ErrorCode.ERROR_PLATFORM_NAME_UNIQUE,
                    PlatformConstants.ErrorCode.ERROR_PLATFORM_NAME_UNIQUE);
        }
    }

    protected void validCodeUnique(Platform platform) {
        // 唯一性校验
        Condition condition = Condition.builder(Platform.class).andWhere(Sqls.custom()
                .andEqualTo(Platform.FIELD_PLATFORM_CODE, platform.getPlatformCode())
                .andEqualTo(Platform.FIELD_TENANT_ID, platform.getTenantId())
        ).build();
        List<Platform> platforms = platformRepository.selectByCondition(condition);
        if (CollectionUtils.isNotEmpty(platforms)) {
            throw new O2CommonException(null, PlatformConstants.ErrorCode.ERROR_PLATFORM_CODE_UNIQUE,
                    PlatformConstants.ErrorCode.ERROR_PLATFORM_CODE_UNIQUE);
        }
    }
}
