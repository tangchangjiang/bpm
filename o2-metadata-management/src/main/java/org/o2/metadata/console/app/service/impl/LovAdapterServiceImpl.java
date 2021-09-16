package org.o2.metadata.console.app.service.impl;

import io.choerodon.core.domain.Page;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import lombok.extern.slf4j.Slf4j;
import org.hzero.boot.platform.lov.dto.LovValueDTO;

import org.o2.metadata.console.api.co.PageCO;
import org.o2.metadata.console.app.bo.CurrencyBO;
import org.o2.metadata.console.app.bo.UomBO;
import org.o2.metadata.console.app.bo.UomTypeBO;
import org.o2.metadata.console.app.service.LovAdapterService;
import org.o2.metadata.console.infra.lovadapter.repository.BaseLovQueryRepository;
import org.o2.metadata.console.infra.lovadapter.repository.HzeroLovQueryRepository;
import org.o2.metadata.console.infra.lovadapter.repository.PublicLovQueryRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
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


    public LovAdapterServiceImpl(BaseLovQueryRepository baseLovQueryRepository,
                                 PublicLovQueryRepository publicLovQueryRepository,
                                 HzeroLovQueryRepository hzeroLovQueryRepository) {
        this.baseLovQueryRepository = baseLovQueryRepository;
        this.publicLovQueryRepository = publicLovQueryRepository;
        this.hzeroLovQueryRepository = hzeroLovQueryRepository;
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
    public  <E> PageCO<E> queryLovPage(Map<String, String> queryParam, PageRequest pageRequest, String lovCode, Long tenantId) {
        return hzeroLovQueryRepository.queryLovPage(queryParam,pageRequest,lovCode,tenantId);
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


}
