package org.o2.metadata.console.infra.lovadapter.impl;

import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;

import org.o2.metadata.console.app.bo.CurrencyBO;
import org.o2.metadata.console.app.bo.UomBO;
import org.o2.metadata.console.app.bo.UomTypeBO;
import org.o2.metadata.console.infra.constant.O2LovConstants;
import org.o2.metadata.console.infra.lovadapter.BaseLovQueryRepository;
import org.o2.metadata.console.infra.lovadapter.HzeroLovQueryRepository;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * 基础LOVService 实现,具有业务属性
 *
 * @author wei.cai@hand-china.com 2021/8/25
 */
@Service("baseLovQueryServiceImpl")
@Slf4j
public class BaseLovQueryRepositoryImpl implements BaseLovQueryRepository {

    private final HzeroLovQueryRepository hzeroLovQueryRepository;

    public BaseLovQueryRepositoryImpl(HzeroLovQueryRepository hzeroLovQueryRepository) {
        this.hzeroLovQueryRepository = hzeroLovQueryRepository;
    }


    @Override
    public CurrencyBO findCurrencyByCode(Long tenantId, String currencyCode) {
        final CurrencyBO currencyBO = new CurrencyBO();
        String currencyMeaning = currencyCode;
        final Map<String, String> queryParams = Maps.newHashMapWithExpectedSize(3);
        queryParams.put(O2LovConstants.Currency.PARAM_CURRENCY_CODE, currencyCode);
        List<Map<String, Object>> maps;
        try {
            maps = hzeroLovQueryRepository.queryLovValueMeaning(tenantId, O2LovConstants.Currency.CODE, queryParams);
        } catch (Exception e) {
            maps = Collections.emptyList();
        }

        for (Map<String, Object> lov : maps) {
            if (currencyCode.equals(lov.get(O2LovConstants.Currency.CURRENCY_CODE))) {
                currencyMeaning = (String) lov.get(O2LovConstants.Currency.CURRENCY_NAME);
                break;
            }
        }
        currencyBO.setName(currencyMeaning);
        return currencyBO;
    }

    @Override
    public Map<String, CurrencyBO> findCurrencyByCodes(Long tenantId, List<String> currencyCodes) {
        final Map<String, CurrencyBO> currencyMap = Maps.newHashMapWithExpectedSize(2);
        final Map<String, String> queryParams = Maps.newHashMapWithExpectedSize(3);
        List<Map<String, Object>> maps;
        try {
            maps = hzeroLovQueryRepository.queryLovValueMeaning(tenantId, O2LovConstants.Currency.CODE, queryParams);
        } catch (Exception e) {
            maps = Collections.emptyList();
        }

        final Map<String, Map<String, Object>> resultsMap = Maps.newHashMapWithExpectedSize(maps.size());
        for (Map<String, Object> lov : maps) {
            resultsMap.put((String)lov.get(O2LovConstants.Currency.CURRENCY_CODE), lov);
        }

        // 货币编码为空
        if (null == currencyCodes || currencyCodes.isEmpty()) {
            for (Map.Entry<String, Map<String, Object>> entry : resultsMap.entrySet()) {
                String k = entry.getKey();
                Map<String, Object> v = entry.getValue();
                final CurrencyBO currencyBO = new CurrencyBO();
                currencyBO.setName((String) v.get(O2LovConstants.Currency.CURRENCY_NAME));
                currencyMap.put(k, currencyBO);
            }
            return currencyMap;
        }
        for (String currencyCode : currencyCodes) {
            final Map<String, Object> lov = resultsMap.get(currencyCode);
            if (null != lov) {
                final CurrencyBO currencyBO = new CurrencyBO();
                currencyBO.setName((String) lov.get(O2LovConstants.Currency.CURRENCY_NAME));
                currencyMap.put(currencyCode, currencyBO);
            }
        }

        return currencyMap;
    }

    @Override
    public UomBO findUomByCode(Long tenantId, String uomCode) {
        final UomBO uomBO = new UomBO();
        String uomName = uomCode;
        final Map<String, String> queryParams = Maps.newHashMapWithExpectedSize(3);
        queryParams.put(O2LovConstants.Uom.PARAM_UOM_CODE, uomCode);
        List<Map<String, Object>> maps;
        try {
            maps = hzeroLovQueryRepository.queryLovValueMeaning(tenantId, O2LovConstants.Uom.CODE, queryParams);
        } catch (Exception e) {
            maps = Collections.emptyList();
        }

        for (Map<String, Object> lov : maps) {
            if (uomCode.equals(lov.get(O2LovConstants.Uom.UOM_CODE))) {
                uomName = (String) lov.get(O2LovConstants.Uom.UOM_NAME);
                break;
            }
        }
        uomBO.setName(uomName);
        return uomBO;
    }

    @Override
    public Map<String, UomBO> findUomByCodes(Long tenantId, List<String> uomCodes) {
        final Map<String, UomBO> uomMap = Maps.newHashMapWithExpectedSize(2);
        final Map<String, String> queryParams = Maps.newHashMapWithExpectedSize(3);
        List<Map<String, Object>> maps;
        try {
            maps = hzeroLovQueryRepository.queryLovValueMeaning(tenantId, O2LovConstants.Uom.CODE, queryParams);
        } catch (Exception e) {
            maps = Collections.emptyList();
        }

        final Map<String, Map<String, Object>> resultsMap = Maps.newHashMapWithExpectedSize(maps.size());
        for (Map<String, Object> lov : maps) {
            resultsMap.put((String)lov.get(O2LovConstants.Uom.UOM_CODE), lov);
        }
        // 单位编码为空
        if (null == uomCodes || uomCodes.isEmpty()) {
            for (Map.Entry<String, Map<String, Object>> entry : resultsMap.entrySet()) {
                String k = entry.getKey();
                Map<String, Object> v = entry.getValue();
                final UomBO uomBO = new UomBO();
                uomBO.setName((String) v.get(O2LovConstants.Uom.UOM_NAME));
                uomMap.put(k, uomBO);
            }
            return uomMap;
        }
        for (String uomCode : uomCodes) {
            final Map<String, Object> lov = resultsMap.get(uomCode);
            if (null != lov) {
                final UomBO uomBO = new UomBO();
                uomBO.setName((String) lov.get(O2LovConstants.Uom.UOM_NAME));
                uomMap.put(uomCode, uomBO);
            }
        }

        return uomMap;
    }

    @Override
    public UomTypeBO findUomTypeByCode(Long tenantId, String uomTypeCode) {
        final UomTypeBO uomTypeBO = new UomTypeBO();
        String uomTypeName = uomTypeCode;
        final Map<String, String> queryParams = Maps.newHashMapWithExpectedSize(3);
        queryParams.put(O2LovConstants.UomType.PARAM_UOM_TYPE_CODE, uomTypeCode);
        List<Map<String, Object>> maps;
        try {
            maps = hzeroLovQueryRepository.queryLovValueMeaning(tenantId, O2LovConstants.UomType.CODE, queryParams);
        } catch (Exception e) {
            maps = Collections.emptyList();
        }

        for (Map<String, Object> lov : maps) {
            if (uomTypeCode.equals(lov.get(O2LovConstants.UomType.UOM_TYPE_CODE))) {
                uomTypeName = (String) lov.get(O2LovConstants.UomType.UOM_TYPE_NAME);
                break;
            }
        }
        uomTypeBO.setName(uomTypeName);
        return uomTypeBO;
    }

    @Override
    public Map<String, UomTypeBO> findUomTypeByCodes(Long tenantId, List<String> uomTypeCodes) {
        final Map<String, UomTypeBO> uomTypeCodeMap = Maps.newHashMapWithExpectedSize(2);
        final Map<String, String> queryParams = Maps.newHashMapWithExpectedSize(3);
        List<Map<String, Object>> maps;
        try {
            maps = hzeroLovQueryRepository.queryLovValueMeaning(tenantId, O2LovConstants.UomType.CODE, queryParams);
        } catch (Exception e) {
            maps = Collections.emptyList();
        }

        final Map<String, Map<String, Object>> resultsMap = Maps.newHashMapWithExpectedSize(maps.size());
        for (Map<String, Object> lov : maps) {
            resultsMap.put((String)lov.get(O2LovConstants.UomType.UOM_TYPE_CODE), lov);
        }
        // 单位类型编码为空
        if (null == uomTypeCodes || uomTypeCodes.isEmpty()) {
            for (Map.Entry<String, Map<String, Object>> entry : resultsMap.entrySet()) {
                String k = entry.getKey();
                Map<String, Object> v = entry.getValue();
                final UomTypeBO uomTypeBO = new UomTypeBO();
                uomTypeBO.setName((String) v.get(O2LovConstants.UomType.UOM_TYPE_NAME));
                uomTypeCodeMap.put(k, uomTypeBO);
            }
            return uomTypeCodeMap;
        }

        for (String uomTypeCode : uomTypeCodes) {
            final Map<String, Object> lov = resultsMap.get(uomTypeCode);
            if (null != lov) {
                final UomTypeBO uomTypeBO = new UomTypeBO();
                uomTypeBO.setName((String) lov.get(O2LovConstants.UomType.UOM_TYPE_NAME));
                uomTypeCodeMap.put(uomTypeCode, uomTypeBO);
            }
        }

        return uomTypeCodeMap;
    }

}
