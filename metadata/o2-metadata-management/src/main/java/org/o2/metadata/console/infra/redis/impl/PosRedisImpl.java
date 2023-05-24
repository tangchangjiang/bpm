package org.o2.metadata.console.infra.redis.impl;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.o2.core.helper.JsonHelper;
import org.o2.data.redis.client.RedisCacheClient;
import org.o2.metadata.console.app.bo.PosMultiRedisBO;
import org.o2.metadata.console.infra.constant.MetadataConstants;
import org.o2.metadata.console.infra.constant.PosConstants;
import org.o2.metadata.console.infra.entity.Pos;
import org.o2.metadata.console.infra.entity.PosInfo;
import org.o2.metadata.console.infra.redis.PosRedis;
import org.o2.metadata.console.infra.repository.PosRepository;
import org.o2.multi.language.management.infra.util.O2RedisMultiLanguageManagementHelper;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.SessionCallback;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 服务店
 *
 * @author chao.yang05@hand-china.com 2022/4/13
 */
@Component
@Slf4j
public class PosRedisImpl implements PosRedis {

    private final PosRepository posRepository;
    private final RedisCacheClient redisCacheClient;
    private final O2RedisMultiLanguageManagementHelper o2RedisMultiLanguageManagementHelper;


    public PosRedisImpl(final PosRepository posRepository,
                        final RedisCacheClient redisCacheClient, O2RedisMultiLanguageManagementHelper o2RedisMultiLanguageManagementHelper) {
        this.posRepository = posRepository;
        this.redisCacheClient = redisCacheClient;
        this.o2RedisMultiLanguageManagementHelper = o2RedisMultiLanguageManagementHelper;
    }

    @Override
    public void syncPosToRedis(List<String> posCodes, Long tenantId) {
        List<PosInfo> posInfos = posRepository.listPosInfoByCode(null, posCodes, MetadataConstants.PosType.STORE, tenantId);
        List<PosInfo> pickUpInfoList = posInfos.stream()
                .filter(pos -> pos.getLongitude() != null && pos.getLatitude() != null)
                .collect(Collectors.toList());
        if (CollectionUtils.isEmpty(pickUpInfoList)) {
            return;
        }
        // 同步门店信息至Redis
        redisCacheClient.executePipelined(new SessionCallback<Object>() {
            @SuppressWarnings({"unchecked"})
            @Override
            public Object execute(RedisOperations operations) throws DataAccessException {
                String posAllStoreKey = PosConstants.RedisKey.getPosAllStoreKey(tenantId);
                String posDetailKey = PosConstants.RedisKey.getPosDetailKey(tenantId);
                Map<String, String> storeMap = new HashMap<>();
                Set<String> posCodeSet = pickUpInfoList.stream().map(PosInfo::getPosCode).collect(Collectors.toSet());
                operations.opsForSet().add(posAllStoreKey, posCodeSet.toArray());
                for (PosInfo pos : pickUpInfoList) {
                    storeMap.put(pos.getPosCode(), JsonHelper.objectToString(pos));
                    if (StringUtils.isNotBlank(pos.getRegionCode()) && StringUtils.isNotBlank(pos.getCityCode())) {
                        String cityStoreKey = PosConstants.RedisKey.getPosCityStoreKey(tenantId, pos.getRegionCode(), pos.getCityCode());
                        operations.opsForSet().add(cityStoreKey, pos.getPosCode());
                    }
                    if (StringUtils.isNotBlank(pos.getDistrictCode())) {
                        String districtStoreKey = PosConstants.RedisKey.getPosDistrictStoreKey(tenantId, pos.getRegionCode(), pos.getCityCode(),
                                pos.getDistrictCode());
                        operations.opsForSet().add(districtStoreKey, pos.getPosCode());
                    }
                }
                operations.opsForHash().putAll(posDetailKey, storeMap);
                return null;
            }
        });
        this.insertPosMultiRedis(tenantId, posInfos);

    }

    @Override
    public void updatePosDetail(List<Long> posIds, List<String> posCodes, Long tenantId) {
        List<PosInfo> posInfos = posRepository.listPosInfoByCode(posIds, null, MetadataConstants.PosType.STORE, tenantId);
        List<PosInfo> pickUpInfoList = posInfos.stream()
                .filter(pos -> pos.getLongitude() != null && pos.getLatitude() != null)
                .collect(Collectors.toList());
        if (CollectionUtils.isEmpty(pickUpInfoList)) {
            return;
        }
        Map<String, String> posMap = new HashMap<>();
        for (PosInfo posInfo : posInfos) {
            posMap.put(posInfo.getPosCode(), JsonHelper.objectToString(posInfo));
        }
        String posDetailKey = PosConstants.RedisKey.getPosDetailKey(tenantId);
        redisCacheClient.opsForHash().putAll(posDetailKey, posMap);
    }

    @Override
    public void insertPosMultiRedis(Long tenantId,List<PosInfo> list) {
        Pos select = new Pos();
        select.setTenantId(tenantId);
        for (PosInfo pos : list) {
            PosMultiRedisBO multiRedis = new PosMultiRedisBO();
            multiRedis.setPosCode(pos.getPosCode());
            multiRedis.setPosName(pos.getPosName());
            multiRedis.setPosId(pos.getPosId());
            o2RedisMultiLanguageManagementHelper.saveMultiLanguage(multiRedis,PosConstants.RedisKey.getPosDetailMultiKey(tenantId,pos.getPosCode()));
        }
    }

}
