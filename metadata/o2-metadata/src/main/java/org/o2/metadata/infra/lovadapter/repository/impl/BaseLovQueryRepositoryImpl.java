package org.o2.metadata.infra.lovadapter.repository.impl;

import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.o2.cache.util.CacheHelper;
import org.o2.metadata.api.co.CurrencyCO;
import org.o2.metadata.app.bo.UomBO;
import org.o2.metadata.infra.constants.MetadataCacheConstants;
import org.o2.metadata.infra.constants.O2LovConstants;
import org.o2.metadata.infra.lovadapter.repository.BaseLovQueryRepository;
import org.o2.metadata.infra.lovadapter.repository.HzeroLovQueryRepository;
import org.springframework.aop.framework.AopContext;
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
@EnableAspectJAutoProxy(proxyTargetClass = true, exposeProxy = true)
public class BaseLovQueryRepositoryImpl implements BaseLovQueryRepository {

    private final HzeroLovQueryRepository hzeroLovQueryRepository;

    public BaseLovQueryRepositoryImpl(HzeroLovQueryRepository hzeroLovQueryRepository) {
        this.hzeroLovQueryRepository = hzeroLovQueryRepository;
    }

    @Override
    public Map<String, CurrencyCO> findCurrencyByCodes(Long tenantId, List<String> currencyCodes) {
        final Map<String, CurrencyCO> currencyMap = Maps.newHashMapWithExpectedSize(2);
        List<Map<String, Object>> maps;
        try {
            BaseLovQueryRepositoryImpl currentProxy = (BaseLovQueryRepositoryImpl) AopContext.currentProxy();
            maps = currentProxy.queryLovValueMeaning(tenantId, O2LovConstants.Currency.CODE);
        } catch (Exception e) {
            maps = Collections.emptyList();
        }

        final Map<String, Map<String, Object>> resultsMap = Maps.newHashMapWithExpectedSize(maps.size());
        for (Map<String, Object> lov : maps) {
            resultsMap.put((String) lov.get(O2LovConstants.Currency.CURRENCY_CODE), lov);
        }

        // 货币编码为空
        if (null == currencyCodes || currencyCodes.isEmpty()) {
            for (Map.Entry<String, Map<String, Object>> entry : resultsMap.entrySet()) {
                String k = entry.getKey();
                Map<String, Object> v = entry.getValue();
                final CurrencyCO co = new CurrencyCO();
                co.setCode(k);
                co.setName((String) v.get(O2LovConstants.Currency.CURRENCY_NAME));
                co.setCountryCode((String) v.get(O2LovConstants.Currency.COUNTRY_CODE));
                co.setCountryName((String) v.get(O2LovConstants.Currency.COUNTRY_NAME));
                co.setCurrencySymbol((String) v.get(O2LovConstants.Currency.CURRENCY_SYMBOL));
                currencyMap.put(k, co);
            }
            return currencyMap;
        }
        for (String currencyCode : currencyCodes) {
            final Map<String, Object> lov = resultsMap.get(currencyCode);
            if (null != lov) {
                final CurrencyCO currencyCo = new CurrencyCO();
                currencyCo.setCode(currencyCode);
                currencyCo.setName((String) lov.get(O2LovConstants.Currency.CURRENCY_NAME));
                currencyCo.setCountryCode((String) lov.get(O2LovConstants.Currency.COUNTRY_CODE));
                currencyCo.setCountryName((String) lov.get(O2LovConstants.Currency.COUNTRY_NAME));
                currencyCo.setCurrencySymbol((String) lov.get(O2LovConstants.Currency.CURRENCY_SYMBOL));
                currencyMap.put(currencyCode, currencyCo);
            }
        }

        return currencyMap;
    }

    @Override
    public Map<String, UomBO> findUomByCodes(Long tenantId, List<String> uomCodes) {
        final Map<String, UomBO> uomMap = Maps.newHashMapWithExpectedSize(2);
        List<Map<String, Object>> maps;
        try {
            BaseLovQueryRepositoryImpl currentProxy = (BaseLovQueryRepositoryImpl) AopContext.currentProxy();
            maps = currentProxy.queryLovValueMeaning(tenantId, O2LovConstants.Uom.CODE);
        } catch (Exception e) {
            maps = Collections.emptyList();
        }

        final Map<String, Map<String, Object>> resultsMap = Maps.newHashMapWithExpectedSize(maps.size());
        for (Map<String, Object> lov : maps) {
            resultsMap.put((String) lov.get(O2LovConstants.Uom.UOM_CODE), lov);
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

    /**
     * 缓存基本单位值集
     * @param lovCode 值集编码
     * @return list
     */
    public List<Map<String, Object>> queryLovValueMeaning(Long tenantId,
                                                          String lovCode) {
        final Map<String, String> queryParams = Maps.newHashMapWithExpectedSize(3);

        return CacheHelper.getCache(
                MetadataCacheConstants.CacheName.O2_LOV,
                MetadataCacheConstants.CacheKey.getBaseLovPrefix(lovCode),
                tenantId, lovCode,
                (tenantParam, lovCodeParam) -> hzeroLovQueryRepository.queryLovValueMeaning(tenantId, lovCode, queryParams),
                false
        );
    }
}
