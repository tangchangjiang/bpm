package org.o2.metadata.console.app.service.impl;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.hzero.boot.platform.lov.dto.LovValueDTO;
import org.o2.metadata.console.api.co.PageCO;
import org.o2.metadata.console.api.dto.LovQueryInnerDTO;
import org.o2.metadata.console.api.dto.RegionQueryLovInnerDTO;
import org.o2.metadata.console.app.bo.CurrencyBO;
import org.o2.metadata.console.app.bo.UomBO;
import org.o2.metadata.console.app.bo.UomTypeBO;
import org.o2.metadata.console.app.service.LovAdapterService;
import org.o2.metadata.console.app.service.lang.MultiLangService;
import org.o2.metadata.console.infra.entity.Region;
import org.o2.metadata.console.infra.lovadapter.repository.BaseLovQueryRepository;
import org.o2.metadata.console.infra.lovadapter.repository.HzeroLovQueryRepository;
import org.o2.metadata.console.infra.lovadapter.repository.IdpLovQueryRepository;
import org.o2.metadata.console.infra.lovadapter.repository.LovGeneralQueryRepository;
import org.o2.metadata.console.infra.lovadapter.repository.PublicLovQueryRepository;
import org.o2.metadata.console.infra.lovadapter.repository.RegionLovQueryRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import io.choerodon.mybatis.pagehelper.domain.PageRequest;

/**
 * 值集查询
 *
 * @author yipeng.zhu@hand-china.com 2021-08-30
 **/
@Service
@Slf4j
public class LovAdapterServiceImpl implements LovAdapterService {
    private final BaseLovQueryRepository baseLovQueryRepository;
    private final PublicLovQueryRepository publicLovQueryRepository;
    private final HzeroLovQueryRepository hzeroLovQueryRepository;
    private final RegionLovQueryRepository regionLovQueryRepository;
    private final IdpLovQueryRepository idpLovQueryRepository;
    private final LovGeneralQueryRepository lovGeneralQueryRepository;
    private final MultiLangService multiLangService;

    public LovAdapterServiceImpl(BaseLovQueryRepository baseLovQueryRepository,
                                 PublicLovQueryRepository publicLovQueryRepository,
                                 HzeroLovQueryRepository hzeroLovQueryRepository,
                                 RegionLovQueryRepository regionLovQueryRepository,
                                 IdpLovQueryRepository idpLovQueryRepository,
                                 LovGeneralQueryRepository lovGeneralQueryRepository,
                                 MultiLangService multiLangService) {
        this.baseLovQueryRepository = baseLovQueryRepository;
        this.publicLovQueryRepository = publicLovQueryRepository;
        this.hzeroLovQueryRepository = hzeroLovQueryRepository;
        this.regionLovQueryRepository = regionLovQueryRepository;
        this.idpLovQueryRepository = idpLovQueryRepository;
        this.lovGeneralQueryRepository = lovGeneralQueryRepository;
        this.multiLangService = multiLangService;
    }

    @Override
    public Map<String, CurrencyBO> findCurrencyByCodes(Long tenantId, List<String> currencyCodes) {
        return baseLovQueryRepository.findCurrencyByCodes(tenantId, currencyCodes);
    }

    @Override
    public Map<String, UomBO> findUomByCodes(Long tenantId, List<String> uomCodes) {
        return baseLovQueryRepository.findUomByCodes(tenantId, uomCodes);
    }

    @Override
    public Map<String, UomTypeBO> findUomTypeByCodes(Long tenantId, List<String> uomTypeCodes) {
        return baseLovQueryRepository.findUomTypeByCodes(tenantId, uomTypeCodes);
    }

    @Override
    public List<Region> queryRegion(Long tenantId, RegionQueryLovInnerDTO innerDTO) {
        return regionLovQueryRepository.queryRegion(tenantId, innerDTO);
    }

    @Override
    public PageCO<Region> queryRegionPage(Long tenantId, Integer page, Integer size, RegionQueryLovInnerDTO innerDTO) {
        return regionLovQueryRepository.queryRegionPage(tenantId, page, size, innerDTO);
    }

    @Override
    public ResponseEntity<Map<String, List<LovValueDTO>>> batchQueryLovInfo(Map<String, String> queryMap, Long tenantId) {
        return publicLovQueryRepository.batchQueryLovInfo(queryMap, tenantId);
    }

    @Override
    public String queryLovPage(Map<String, String> queryParam, PageRequest pageRequest, String lovCode, Long tenantId) {
        return hzeroLovQueryRepository.queryLovPage(queryParam, pageRequest, lovCode, tenantId);
    }

    @Override
    public List<LovValueDTO> queryLovValue(Long tenantId, String lovCode) {
        return idpLovQueryRepository.queryLovValue(tenantId, lovCode);
    }

    @Override
    public String queryLovValueMeaning(Long tenantId, String lovCode, String lovValue) {
        return idpLovQueryRepository.queryLovValueMeaning(tenantId, lovCode, lovValue);
    }

    @Override
    public List<Map<String, Object>> queryLovValueMeaning(Long tenantId, String lovCode, Map<String, String> queryLovValueMap) {
        return lovGeneralQueryRepository.queryLovValueMeaning(tenantId, lovCode, queryLovValueMap);
    }

    @Override
    public List<Map<String, Object>> queryLovValueMeaning(Long tenantId, String lovCode, Integer page, Integer size,
                                                          Map<String, String> queryLovValueMap) {
        return lovGeneralQueryRepository.queryLovValueMeaning(tenantId, lovCode, page, size, queryLovValueMap);
    }

    @Override
    public List<Map<String, Object>> queryLovValueMeaning(Long tenantId, String lovCode, Integer page, Integer size, Map<String, String> queryLovValueMap, boolean useCache) {
        return lovGeneralQueryRepository.queryLovValueMeaning(tenantId, lovCode, page, size, queryLovValueMap, useCache);
    }

    @Override
    public Map<String, List<LovValueDTO>> batchQueryLovValueByLang(Long tenantId, LovQueryInnerDTO lovQueryInnerDTO) {
        String lovCode = lovQueryInnerDTO.getLovCode();
        if (StringUtils.isBlank(lovCode)) {
            return Collections.emptyMap();
        }

        return multiLangService.batchQueryByLang(tenantId, lovQueryInnerDTO.getLanguageList(),
                lang -> idpLovQueryRepository.queryLovValue(tenantId, lang, lovCode));
    }
}
