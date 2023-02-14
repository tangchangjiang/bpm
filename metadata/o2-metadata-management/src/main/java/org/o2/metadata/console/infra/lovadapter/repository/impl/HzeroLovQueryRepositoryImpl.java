package org.o2.metadata.console.infra.lovadapter.repository.impl;

import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.hzero.boot.platform.lov.adapter.LovAdapter;
import org.hzero.boot.platform.lov.dto.LovDTO;
import org.hzero.boot.platform.lov.dto.LovValueDTO;

import org.hzero.common.HZeroService;
import org.hzero.core.base.BaseConstants;
import org.hzero.core.redis.RedisHelper;
import org.hzero.core.util.ResponseUtils;
import org.o2.metadata.console.infra.constant.O2LovConstants;
import org.o2.metadata.console.infra.lovadapter.repository.HzeroLovQueryRepository;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;
import java.util.Objects;

@Repository
@Slf4j
public class HzeroLovQueryRepositoryImpl implements HzeroLovQueryRepository {

    private LovAdapter lovAdapter;
    private final RestTemplate restTemplate;
    private final RedisHelper redisHelper;

    public HzeroLovQueryRepositoryImpl(LovAdapter lovAdapter,
                                       RestTemplate restTemplate,
                                       RedisHelper redisHelper) {
        this.lovAdapter = lovAdapter;
        this.restTemplate = restTemplate;
        this.redisHelper = redisHelper;
    }

    @Override
    public List<LovValueDTO> queryLovValue(Long tenantId,
                                           String lovCode) {
        return lovAdapter.queryLovValue(lovCode, tenantId);
    }

    @Override
    public List<LovValueDTO> queryLovValue(Long tenantId, String lovCode, String lang) {
        return lovAdapter.queryLovValue(lovCode, tenantId, lang);
    }

    @Override
    public String queryLovValueMeaning(Long tenantId,
                                       String lovCode,
                                       String lovValue) {
        return lovAdapter.queryLovMeaning(lovCode, tenantId, lovValue);
    }

    @Override
    public List<Map<String, Object>> queryLovValueMeaning(Long tenantId,
                                                          String lovCode,
                                                          Map<String, String> queryLovValueMap) {
        return queryLovValueMeaning(tenantId, lovCode, null, null, queryLovValueMap);
    }

    @Override
    public List<Map<String, Object>> queryLovValueMeaning(Long tenantId, String lovCode, Integer page, Integer size,
                                                          Map<String, String> queryLovValueMap) {
        return lovAdapter.queryLovData(lovCode, tenantId, null, page, size, queryLovValueMap);
    }

    @Override
    public String queryLovPage(Map<String, String> queryParam, PageRequest pageRequest, String lovCode, Long tenantId) {
        if (!queryParam.containsKey(O2LovConstants.RequestParam.LOV_CODE)) {
            queryParam.put("lovCode", lovCode);
        }
        if (!queryParam.containsKey(O2LovConstants.RequestParam.ORGANIZATIONID)) {
            queryParam.put("organizationId", String.valueOf(tenantId));
        }
        LovDTO lovDTO = lovAdapter.queryLovInfo(lovCode, tenantId);
        processPageInfo(queryParam, pageRequest.getPage(), pageRequest.getSize(), Objects.equals(lovDTO.getMustPageFlag(), BaseConstants.Flag.YES));

        String url = getLovUrl(lovDTO, tenantId);
        if (null == url) {
            return "";
        }

        String json;
        if (O2LovConstants.RequestParam.POST.equals(lovDTO.getRequestMethod())) {
            json = ResponseUtils.getResponse(this.restTemplate.postForEntity(this.preProcessUrlParam(url, queryParam), queryParam, String.class),
                    String.class);
        } else {
            json = ResponseUtils.getResponse(this.restTemplate.getForEntity(this.preProcessUrlParam(url, queryParam), String.class, queryParam),
                    String.class);
        }

        return json;
    }

    @Override
    public String getQueryParamStr(Long tenantId, Map<String, String> queryParam) {
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
     * 构造请求地址
     *
     * @param url    请求url
     * @param params 参数
     * @return str
     */
    private String preProcessUrlParam(String url, Map<String, String> params) {
        if (org.apache.commons.collections.MapUtils.isEmpty(params)) {
            return url;
        }
        StringBuilder stringBuilder = new StringBuilder(url);
        boolean firstKey = !url.contains("?");
        for (String key : params.keySet()) {
            if (firstKey) {
                stringBuilder.append('?').append(key).append("={").append(key).append('}');
                firstKey = false;
                continue;
            }
            stringBuilder.append('&').append(key).append("={").append(key).append('}');
        }
        return stringBuilder.toString();
    }

    /**
     * 获取服务名
     *
     * @param serverCode 路由
     * @return str
     */
    private String getServerName(String serverCode) {
        this.redisHelper.setCurrentDatabase(HZeroService.Admin.REDIS_DB);
        String serverName = this.redisHelper.hshGet("hadm:routes", serverCode);
        this.redisHelper.clearCurrentDatabase();
        return serverName;
    }

    /**
     * 构造分页入参
     *
     * @param params     参数
     * @param page       页码
     * @param size       大小
     * @param isMustPage 是否分页
     */
    private void processPageInfo(Map<String, String> params, Integer page, Integer size, boolean isMustPage) {
        if (page == null) {
            params.put("page", "0");
        } else {
            params.put("page", String.valueOf(page));
        }

        if (size == null) {
            if (isMustPage) {
                params.put("size", "100");
            } else {
                params.put("size", "100000000");
            }
        } else {
            params.put("size", String.valueOf(Integer.min(size, Integer.parseInt("100000000"))));
        }

    }

    /**
     * @param lov      值集信息
     * @param tenantId 租户ID
     * @return 获取请求路径
     * @date 2021-09-14
     */
    private String getLovUrl(LovDTO lov, Long tenantId) {
        String serviceName = this.getServerName(lov.getRouteName());
        String lovTypeCode = lov.getLovTypeCode();
        if (O2LovConstants.LovTypeCode.SQL.equals(lovTypeCode)) {
            if (tenantId == null || Objects.equals(tenantId, BaseConstants.DEFAULT_TENANT_ID)) {
                return O2LovConstants.RequestParam.URL_PREFIX + serviceName + "/v1/lovs/sql/data";
            }
            return O2LovConstants.RequestParam.URL_PREFIX + serviceName + "/v1/{organizationId}/lovs/sql/data";

        }
        if (O2LovConstants.LovTypeCode.URL.equals(lovTypeCode)) {
            return O2LovConstants.RequestParam.URL_PREFIX + serviceName + lov.getCustomUrl();
        }
        return null;
    }

}
