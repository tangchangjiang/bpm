package org.o2.metadata.console.infra.lovadapter.repository.impl;

import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.o2.cache.util.CacheHelper;
import org.o2.metadata.console.app.bo.CurrencyBO;
import org.o2.metadata.console.app.bo.UomBO;
import org.o2.metadata.console.app.bo.UomTypeBO;
import org.o2.metadata.console.infra.constant.MetadataCacheConstants;
import org.o2.metadata.console.infra.constant.O2LovConstants;
import org.o2.metadata.console.infra.lovadapter.repository.BaseLovQueryRepository;
import org.o2.metadata.console.infra.lovadapter.repository.HzeroLovQueryRepository;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.stereotype.Repository;

import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * 基础LOVService 实现,具有业务属性
 *
 * @author wei.cai@hand-china.com 2021/8/25
 */
@Repository("baseLovQueryServiceImpl")
@Slf4j
@EnableAspectJAutoProxy( proxyTargetClass = true , exposeProxy = true )
public class BaseLovQueryRepositoryImpl implements BaseLovQueryRepository {

    private final HzeroLovQueryRepository hzeroLovQueryRepository;

    public BaseLovQueryRepositoryImpl(HzeroLovQueryRepository hzeroLovQueryRepository) {
        this.hzeroLovQueryRepository = hzeroLovQueryRepository;
    }

    @Override
    public Map<String, CurrencyBO> findCurrencyByCodes(Long tenantId, List<String> currencyCodes) {
        final Map<String, CurrencyBO> currencyMap = Maps.newHashMapWithExpectedSize(2);
        List<Map<String, Object>> maps;
        try {
            maps = queryLovValueMeaning(tenantId, O2LovConstants.Currency.CODE);
        } catch (Exception e) {
            log.error("findCurrencyByCodes occurs an exception, so it will return the empty map. errorMsg:{}", e.getMessage(), e);
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
                currencyBO.setCode(k);
                currencyBO.setName((String) v.get(O2LovConstants.Currency.CURRENCY_NAME));
                currencyBO.setCountryCode((String)v.get(O2LovConstants.Currency.COUNTRY_CODE));
                currencyBO.setCountryName((String)v.get(O2LovConstants.Currency.COUNTRY_NAME));
                currencyBO.setCurrencySymbol((String)v.get(O2LovConstants.Currency.CURRENCY_SYMBOL));
                currencyMap.put(k, currencyBO);
            }
            return currencyMap;
        }
        for (String currencyCode : currencyCodes) {
            final Map<String, Object> lov = resultsMap.get(currencyCode);
            if (null != lov) {
                final CurrencyBO currencyBO = new CurrencyBO();
                currencyBO.setCode(currencyCode);
                currencyBO.setName((String) lov.get(O2LovConstants.Currency.CURRENCY_NAME));
                currencyBO.setCountryCode((String)lov.get(O2LovConstants.Currency.COUNTRY_CODE));
                currencyBO.setCountryName((String)lov.get(O2LovConstants.Currency.COUNTRY_NAME));
                currencyBO.setCurrencySymbol((String)lov.get(O2LovConstants.Currency.CURRENCY_SYMBOL));
                currencyMap.put(currencyCode, currencyBO);
            }
        }

        return currencyMap;
    }



    @Override
    public Map<String, UomBO> findUomByCodes(Long tenantId, List<String> uomCodes) {
        final Map<String, UomBO> uomMap = Maps.newHashMapWithExpectedSize(2);
        List<Map<String, Object>> maps;
        try {
            maps = queryLovValueMeaning(tenantId, O2LovConstants.Uom.CODE);
        } catch (Exception e) {
            log.error("findUomByCodes occurs an exception, so it will return the empty map. errorMsg:{}", e.getMessage(), e);
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
    public Map<String, UomTypeBO> findUomTypeByCodes(Long tenantId, List<String> uomTypeCodes) {
        final Map<String, UomTypeBO> uomTypeCodeMap = Maps.newHashMapWithExpectedSize(2);
        List<Map<String, Object>> maps;
        try {
            maps = queryLovValueMeaning(tenantId, O2LovConstants.UomType.CODE);
        } catch (Exception e) {
            log.error("findUomTypeByCodes occurs an exception, so it will return the empty map. errorMsg:{}", e.getMessage(), e);
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

    /**
     * 缓存基本单位值集
     * @param lovCode 值集编码
     * @return list
     */
    public List<Map<String, Object>> queryLovValueMeaning(Long tenantId, String lovCode) {
        final Map<String, String> queryParams = Maps.newHashMapWithExpectedSize(3);

        return CacheHelper.getCache(
                MetadataCacheConstants.CacheName.O2_LOV,
                MetadataCacheConstants.KeyPrefix.getBaseLovPrefix(lovCode),
                tenantId, lovCode,
                (tenantParam, lovCodeParam) -> hzeroLovQueryRepository.queryLovValueMeaning(tenantParam,lovCodeParam, queryParams),
                false
        );
    }
}
