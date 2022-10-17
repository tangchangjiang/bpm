package org.o2.metadata.infra.lovadapter.repository.impl;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.hzero.core.base.BaseConstants;
import org.o2.cache.util.CollectionCacheHelper;
import org.o2.core.helper.JsonHelper;
import org.o2.metadata.api.co.RoleCO;
import org.o2.metadata.infra.constants.MetadataCacheConstants;
import org.o2.metadata.infra.constants.O2LovConstants;
import org.o2.metadata.infra.lovadapter.repository.HzeroLovQueryRepository;
import org.o2.metadata.infra.lovadapter.repository.SqlLovQueryRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Description
 *
 * @author yipeng.zhu@hand-china.com 2022/8/30
 */
@Repository
@Slf4j
public class SqlLovQueryRepositoryImpl implements SqlLovQueryRepository {
    private final HzeroLovQueryRepository hzeroLovQueryRepository;

    public SqlLovQueryRepositoryImpl(HzeroLovQueryRepository hzeroLovQueryRepository) {
        this.hzeroLovQueryRepository = hzeroLovQueryRepository;
    }
    @Override
    public Map<String, RoleCO> findRoleByCodes(Long organizationId, List<String> roleCodes) {
        final String keyPrefix = MetadataCacheConstants.CacheKey.getFetchRolePrefix(organizationId);
        return CollectionCacheHelper.getCache(MetadataCacheConstants.CacheName.ACROSS, keyPrefix, roleCodes, code -> fetchRoleInner(organizationId, code));
    }
    /**
     * 查询角色
     * @param tenantId 租户ID
     * @param roleCodes      角色
     * @return 角色信息
     */
    public Map<String,RoleCO> fetchRoleInner(Long tenantId, Collection<String> roleCodes) {
        Map<String, String> queryLovValueMap = new HashMap<>(4);
        queryLovValueMap.put(O2LovConstants.RoleLov.ROLE_SQL_PARAM, StringUtils.join(roleCodes, BaseConstants.Symbol.COMMA));
        queryLovValueMap.put(O2LovConstants.RoleLov.ROLE_SQL_PARAM_TENANT_ID,String.valueOf(tenantId));
        Map<String,RoleCO> coMap = new HashMap<>(roleCodes.size());
        try {
            List<Map<String, Object>> result =  hzeroLovQueryRepository.queryLovValueMeaning(tenantId, O2LovConstants.RoleLov.ROLE_SQL_LOV, queryLovValueMap);
            for (Map<String, Object> objectMap : result) {
                String str = JsonHelper.mapToString(objectMap);
                RoleCO co = JsonHelper.stringToObject(str,RoleCO.class);
                coMap.put(co.getRoleCode(),co);
            }
            return coMap;
        } catch (Exception e) {
            log.error("fetch role info error from metadata service. errorMsg:[{}], roleCodes:[{}]",
                    e.getMessage(), roleCodes, e);
        }
        return Collections.emptyMap();
    }
}
