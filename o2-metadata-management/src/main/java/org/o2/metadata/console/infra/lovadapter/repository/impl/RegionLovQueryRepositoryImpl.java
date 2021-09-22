package org.o2.metadata.console.infra.lovadapter.repository.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.o2.core.helper.JsonHelper;
import org.o2.metadata.console.api.co.PageCO;
import org.o2.metadata.console.api.co.RegionCO;
import org.o2.metadata.console.infra.constant.O2LovConstants;
import org.o2.metadata.console.infra.lovadapter.repository.HzeroLovQueryRepository;
import org.o2.metadata.console.infra.lovadapter.repository.RegionLovQueryRepository;
import org.springframework.aop.framework.AopContext;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 *
 * 地址值集
 *
 * @author yipeng.zhu@hand-china.com 2021-09-22
 **/
@Repository
@Slf4j
@EnableAspectJAutoProxy( proxyTargetClass = true , exposeProxy = true )
public class RegionLovQueryRepositoryImpl implements RegionLovQueryRepository {
    private HzeroLovQueryRepository hzeroLovQueryRepository;

    private ObjectMapper objectMapper;

    public RegionLovQueryRepositoryImpl(HzeroLovQueryRepository hzeroLovQueryRepository, ObjectMapper objectMapper) {
        this.hzeroLovQueryRepository = hzeroLovQueryRepository;
        this.objectMapper = objectMapper;
    }

    @Override
    public List<RegionCO> queryRegion(Long tenantId, Map<String, String> queryParam) {
        String cacheKey = getCacheKey(tenantId,queryParam);
        RegionLovQueryRepositoryImpl currentProxy = (RegionLovQueryRepositoryImpl) AopContext.currentProxy();
        return currentProxy.queryRegion(tenantId, queryParam, cacheKey);
    }

    @Override
    public PageCO<RegionCO> queryRegionPage(Long tenantId, Integer page, Integer size, Map<String, String> queryParam) {
        String cacheKey = getCacheKey(tenantId,queryParam);
        RegionLovQueryRepositoryImpl currentProxy = (RegionLovQueryRepositoryImpl) AopContext.currentProxy();
        return currentProxy.queryRegionPage(tenantId,page,size,queryParam,cacheKey);
    }
    
    /**
     * 获取缓存key
     * @param tenantId 租户ID
     * @param queryParam 查询参数
     * @return  str
     */
    private String getCacheKey(Long tenantId,Map<String, String> queryParam) {
        StringBuilder cacheKey = new StringBuilder(tenantId.toString()).append("_");
        if (!queryParam.isEmpty()) {
            for (Map.Entry<String, String> entry : queryParam.entrySet()) {
                String k = entry.getKey();
                String v = entry.getValue();
                if (StringUtils.isNotEmpty(v)) {
                    cacheKey.append(k).append("_").append(v);
                }
            }
        }
        return cacheKey.toString();
    }

    /**
     * 查询地区值集
     * @param tenantId 租户ID
     * @param queryParam 查询条件
     * @param cacheKey 缓存key
     * @return list
     */
    @Cacheable(value = "O2_LOV", key = "'region'+'_'+#cacheKey")
    public List<RegionCO> queryRegion(Long tenantId, Map<String, String> queryParam, String cacheKey) {
        List<Map<String, Object>> maps = hzeroLovQueryRepository.queryLovValueMeaning(tenantId, O2LovConstants.AddressType.CODE, queryParam);
        List<RegionCO> list = null;
        try {
            list = this.objectMapper.readValue(JsonHelper.objectToString(maps), new TypeReference<List<RegionCO>>() {
            });
        } catch (Exception e) {
            log.error("region translation data error.");
        }
        return list;
    }

    /**
     * 分页查询地区值集
     * @param tenantId 租户ID
     * @param queryParam 查询条件
     * @param cacheKey 缓存key
     * @param page  页码
     * @param size 大小
     * @return page
     */
    @Cacheable(value = "O2_LOV", key = "'regionPage'+'_'+#cacheKey")
    public PageCO<RegionCO> queryRegionPage(Long tenantId, Integer page, Integer size, Map<String, String> queryParam, String cacheKey) {
        PageCO<RegionCO> result = new PageCO<>();
        PageRequest pageRequest  = new PageRequest();
        pageRequest.setPage(page);
        pageRequest.setSize(size);
        queryParam.put("organizationId", String.valueOf(tenantId));
        String json = hzeroLovQueryRepository.queryLovPage(queryParam,pageRequest,O2LovConstants.AddressType.CODE,tenantId);
        if (StringUtils.isNotEmpty(json)) {
            try {
                result = this.objectMapper.readValue(json, new TypeReference<PageCO<RegionCO>>() {});
            } catch (Exception e) {
                log.error("region translation data error.");
            }
        }
        return result;

    }
}
