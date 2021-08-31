package org.o2.metadata.console.app.service.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.choerodon.core.domain.Page;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.MapUtils;
import org.hzero.boot.platform.lov.adapter.LovAdapter;
import org.hzero.boot.platform.lov.dto.LovDTO;
import org.hzero.boot.platform.lov.dto.LovValueDTO;
import org.hzero.common.HZeroService;
import org.hzero.core.base.BaseConstants;
import org.hzero.core.redis.RedisHelper;
import org.hzero.core.util.ResponseUtils;
import org.o2.lov.app.service.BaseLovQueryService;
import org.o2.lov.app.service.PublicLovQueryService;
import org.o2.lov.domain.bo.CurrencyBO;
import org.o2.lov.domain.bo.UomBO;
import org.o2.lov.domain.bo.UomTypeBO;
import org.o2.metadata.console.app.service.LovAdapterService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.*;

/**
 *
 * 值集查询
 *
 * @author yipeng.zhu@hand-china.com 2021-08-30
 **/
@Service
@Slf4j
public class LovAdapterServiceImpl implements LovAdapterService {
    private BaseLovQueryService baseLovQueryService;
    private PublicLovQueryService publicLovQueryService;
    private LovAdapter lovAdapter;
    private final RestTemplate restTemplate;
    private final RedisHelper redisHelper;
    private final ObjectMapper objectMapper;

    public LovAdapterServiceImpl(BaseLovQueryService baseLovQueryService,
                                 PublicLovQueryService publicLovQueryService, LovAdapter lovAdapter, RestTemplate restTemplate, RedisHelper redisHelper, ObjectMapper objectMapper) {
        this.baseLovQueryService = baseLovQueryService;
        this.publicLovQueryService = publicLovQueryService;
        this.lovAdapter = lovAdapter;
        this.restTemplate = restTemplate;
        this.redisHelper = redisHelper;
        this.objectMapper = objectMapper;
    }


    @Override
    public Map<String, CurrencyBO> findCurrencyByCodes(Long tenantId, List<String> currencyCodes) {
        return baseLovQueryService.findCurrencyByCodes(tenantId,currencyCodes);
    }

    @Override
    public Map<String, UomBO> findUomByCodes(Long tenantId, List<String> uomCodes) {
        return baseLovQueryService.findUomByCodes(tenantId,uomCodes);
    }

    @Override
    public Map<String, UomTypeBO> findUomTypeByCodes(Long tenantId, List<String> uomTypeCodes) {
        return baseLovQueryService.findUomTypeByCodes(tenantId,uomTypeCodes);
    }

    @Override
    public ResponseEntity<Map<String, List<LovValueDTO>>> batchQueryLovInfo(Map<String, String> queryMap, Long tenantId) {
        return publicLovQueryService.batchQueryLovInfo(queryMap,tenantId);
    }


    @Override
    public  <E> Page<E> pageList(Map<String, String> queryParam, PageRequest pageRequest, String lovCode) {
        Page<E> result = new Page<>();
        LovDTO lovDTO = lovAdapter.queryLovInfo(lovCode, 2L);

        processPageInfo(queryParam,pageRequest.getPage(),pageRequest.getSize(), Objects.equals(lovDTO.getMustPageFlag(), BaseConstants.Flag.YES));

        String url = "http" + "://" + getServerName(lovDTO.getRouteName()) + lovDTO.getCustomUrl();

        String json;
        if ("POST".equals(lovDTO.getRequestMethod())) {
            json = ResponseUtils.getResponse(this.restTemplate.postForEntity(this.preProcessUrlParam(url, queryParam), queryParam, String.class, new Object[0]), String.class);
        } else {
            json = ResponseUtils.getResponse(this.restTemplate.getForEntity(this.preProcessUrlParam(url, queryParam), String.class, queryParam), String.class);
        }
        try {
            result =  this.objectMapper.readValue(json, new TypeReference<Page<E>>() {
            });
        } catch (IOException var9) {
            log.error("get translation data error.");
        }
        return result;
    }


    private String preProcessUrlParam(String url, Map<String, String> params) {
        if (MapUtils.isEmpty(params)) {
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



    private String getServerName(String serverCode) {
        this.redisHelper.setCurrentDatabase(HZeroService.Admin.REDIS_DB);
        String serverName = this.redisHelper.hshGet("hadm:routes", serverCode);
        this.redisHelper.clearCurrentDatabase();
        return serverName;
    }

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
}
