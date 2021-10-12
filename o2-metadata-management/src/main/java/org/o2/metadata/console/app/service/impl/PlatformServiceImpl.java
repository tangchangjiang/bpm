package org.o2.metadata.console.app.service.impl;

import io.choerodon.core.exception.CommonException;
import org.apache.commons.collections.CollectionUtils;
import org.hzero.core.message.MessageAccessor;
import org.hzero.mybatis.domian.Condition;
import org.hzero.mybatis.util.Sqls;
import org.o2.metadata.console.api.co.PlatformCO;
import org.o2.metadata.console.api.dto.PlatformQueryInnerDTO;
import org.o2.metadata.console.infra.constant.MetadataConstants;
import org.o2.metadata.console.infra.convertor.PlatformConverter;
import org.o2.metadata.console.infra.entity.Platform;
import org.o2.metadata.console.infra.entity.PlatformInfoMapping;
import org.o2.metadata.console.infra.repository.PlatformInfoMappingRepository;
import org.o2.metadata.console.infra.repository.PlatformRepository;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.o2.metadata.console.app.service.PlatformService;
import java.util.List;
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
        // 唯一性校验
        Condition condition = Condition.builder(Platform.class).andWhere(Sqls.custom().andEqualTo(Platform.FIELD_PLATFORM_CODE,platform.getPlatformCode())).build();
        List<Platform> platforms = platformRepository.selectByCondition(condition);
        //保存平台定义表
        if (platform.getPlatformId() == null) {
            if (CollectionUtils.isNotEmpty(platforms)) {
                throw new CommonException(MetadataConstants.ErrorCode.O2MD_ERROR_CHECK_FAILED,MessageAccessor.getMessage(MetadataConstants.ErrorCode.O2MD_ERROR_CHECK_FAILED).desc());
            }
            platformRepository.insertSelective(platform);
        } else {
            if (CollectionUtils.isNotEmpty(platforms)) {
                Platform temp = platforms.get(0);
                if (!temp.getPlatformId().equals(platform.getPlatformId())) {
                    throw new CommonException(MetadataConstants.ErrorCode.O2MD_ERROR_CHECK_FAILED, MessageAccessor.getMessage(MetadataConstants.ErrorCode.O2MD_ERROR_CHECK_FAILED).desc());
                }

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
        Map<String,PlatformCO> map = new HashMap<>(16);
        List<PlatformInfoMapping> list = platformInfoMappingRepository.selectCondition(queryInnerDTO);
        if (list.isEmpty()) {
            return map;
        }

        Map<String,List<PlatformInfoMapping>> groupMap = list.stream().collect(Collectors.groupingBy(PlatformInfoMapping::getPlatformCode));
        for (Map.Entry<String, List<PlatformInfoMapping>> entry : groupMap.entrySet()) {
            String k = entry.getKey();
            List<PlatformInfoMapping> value = entry.getValue();
            PlatformCO co = new PlatformCO();
            co.setPlatformCode(k);
            co.setPlatformName(value.get(0).getPlatformName());
            co.setPlatformInfoMappings(PlatformConverter.poToCoListObjects(value));
            map.put(k,co);
        }
        return map;
    }
}
