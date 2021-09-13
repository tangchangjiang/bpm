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

import org.o2.metadata.console.app.bo.CurrencyBO;
import org.o2.metadata.console.app.bo.UomBO;
import org.o2.metadata.console.app.bo.UomTypeBO;
import org.o2.metadata.console.app.service.LovAdapterService;
import org.o2.metadata.console.infra.constant.O2LovConstants;
import org.o2.metadata.console.infra.lovadapter.BaseLovQueryRepository;
import org.o2.metadata.console.infra.lovadapter.HzeroLovQueryRepository;
import org.o2.metadata.console.infra.lovadapter.PublicLovQueryRepository;
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
    private BaseLovQueryRepository baseLovQueryRepository;
    private PublicLovQueryRepository publicLovQueryRepository;
    private HzeroLovQueryRepository hzeroLovQueryRepository;
    private LovAdapter lovAdapter;
    private final RestTemplate restTemplate;
    private final RedisHelper redisHelper;
    private final ObjectMapper objectMapper;

    public LovAdapterServiceImpl(BaseLovQueryRepository baseLovQueryRepository,
                                 PublicLovQueryRepository publicLovQueryRepository,
                                 HzeroLovQueryRepository hzeroLovQueryRepository,
                                 LovAdapter lovAdapter,
                                 RestTemplate restTemplate,
                                 RedisHelper redisHelper,
                                 ObjectMapper objectMapper) {
        this.baseLovQueryRepository = baseLovQueryRepository;
        this.publicLovQueryRepository = publicLovQueryRepository;
        this.hzeroLovQueryRepository = hzeroLovQueryRepository;
        this.lovAdapter = lovAdapter;
        this.restTemplate = restTemplate;
        this.redisHelper = redisHelper;
        this.objectMapper = objectMapper;
    }


    @Override
    public Map<String, CurrencyBO> findCurrencyByCodes(Long tenantId, List<String> currencyCodes) {
        return baseLovQueryRepository.findCurrencyByCodes(tenantId,currencyCodes);
    }

    @Override
    public Map<String, UomBO> findUomByCodes(Long tenantId, List<String> uomCodes) {
        return baseLovQueryRepository.findUomByCodes(tenantId,uomCodes);
    }

    @Override
    public Map<String, UomTypeBO> findUomTypeByCodes(Long tenantId, List<String> uomTypeCodes) {
        return baseLovQueryRepository.findUomTypeByCodes(tenantId,uomTypeCodes);
    }

    @Override
    public ResponseEntity<Map<String, List<LovValueDTO>>> batchQueryLovInfo(Map<String, String> queryMap, Long tenantId) {
        return publicLovQueryRepository.batchQueryLovInfo(queryMap,tenantId);
    }


    @Override
    public  <E> Page<E> queryUrlLovPage(Map<String, String> queryParam, PageRequest pageRequest, String lovCode) {
        Page<E> result = new Page<>();
        LovDTO lovDTO = lovAdapter.queryLovInfo(lovCode, 2L);

        processPageInfo(queryParam,pageRequest.getPage(),pageRequest.getSize(), Objects.equals(lovDTO.getMustPageFlag(), BaseConstants.Flag.YES));

        String url = O2LovConstants.RequestParam.URL_PREFIX + getServerName(lovDTO.getRouteName()) + lovDTO.getCustomUrl();

        String json;
        if (O2LovConstants.RequestParam.POST.equals(lovDTO.getRequestMethod())) {
            json = ResponseUtils.getResponse(this.restTemplate.postForEntity(this.preProcessUrlParam(url, queryParam), queryParam, String.class), String.class);
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

    @Override
    public List<LovValueDTO> queryLovValue(Long tenantId, String lovCode) {
        return hzeroLovQueryRepository.queryLovValue(tenantId,lovCode);
    }

    @Override
    public String queryLovValueMeaning(Long tenantId, String lovCode, String lovValue) {
        return hzeroLovQueryRepository.queryLovValueMeaning(tenantId,lovCode,lovCode);
    }

    @Override
    public List<Map<String, Object>> queryLovValueMeaning(Long tenantId, String lovCode, Map<String, String> queryLovValueMap) {
        return hzeroLovQueryRepository.queryLovValueMeaning(tenantId,lovCode,queryLovValueMap);
    }

    @Override
    public List<Map<String, Object>> queryLovValueMeaning(Long tenantId, String lovCode, Integer page, Integer size, Map<String, String> queryLovValueMap) {
        return null;
    }

    /**
     *  构造请求地址
     * @param url 请求url
     * @param params   参数
     * @return  str
     */
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


    /**
     *  获取服务名
     * @param serverCode 路由
     * @return  str
     */
    private String getServerName(String serverCode) {
        this.redisHelper.setCurrentDatabase(HZeroService.Admin.REDIS_DB);
        String serverName = this.redisHelper.hshGet("hadm:routes", serverCode);
        this.redisHelper.clearCurrentDatabase();
        return serverName;
    }
    /**
     * 构造分页入参
     * @param params 参数
     * @param page 页码
     * @param size 大小
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
}
