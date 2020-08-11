package org.o2.metadata.console.app.service.impl;

import org.apache.commons.lang3.StringUtils;
import org.o2.core.helper.FastJsonHelper;
import org.o2.data.redis.client.RedisCacheClient;
import org.o2.metadata.console.app.bo.FreightBO;
import org.o2.metadata.console.app.bo.FreightDetailBO;
import org.o2.metadata.console.app.bo.FreightPriceBO;
import org.o2.metadata.console.app.bo.FreightTemplateBO;
import org.o2.metadata.console.app.service.FreightCacheService;
import org.o2.metadata.core.infra.constants.MetadataConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * 运费模板缓存服务默认实现
 *
 * @author peng.xu@hand-china.com 2019-06-18
 */
@Component("freightCacheService")
public class FreightCacheServiceImpl implements FreightCacheService {
    private final static Logger LOG = LoggerFactory.getLogger(FreightCacheServiceImpl.class);

    private final static String DEFAULT_REGION = "null";

    private RedisCacheClient redisCacheClient;

    public FreightCacheServiceImpl(final RedisCacheClient redisCacheClient) {
        this.redisCacheClient = redisCacheClient;
    }

    @Override
    public void saveFreight(final FreightTemplateBO freightTemplate) {
        final FreightBO freight = freightTemplate.getFreight();
        final List<FreightDetailBO> freightDetailList = freightTemplate.getFreightDetailList();

        final String freightCode = freight.getTemplateCode();
        final String freightStr = FastJsonHelper.objectToString(freight);
        final String freightKey = getFreightCacheKey(freightCode);
        this.redisCacheClient.opsForValue().set(freightKey, freightStr);

        saveFreightDetails(freightDetailList);
    }

    @Override
    public void saveFreightDetails(final List<FreightDetailBO> freightDetailList) {
        if (freightDetailList != null && freightDetailList.size() > 0) {
            final String freightCode = freightDetailList.get(0).getTemplateCode();
            final String freightDetailKey = getFreightDetailCacheKey(freightCode);

            final Map<String, String> freightDetailMap = convertToFreightDetailMap(freightDetailList);
            final Map<String, String> freightPriceMap = convertToFreightPriceMap(freightDetailList);

            executeSaveFreightDetailScript(freightDetailKey, freightDetailMap, freightPriceMap);
        }
    }

    @Override
    public void deleteFreight(final FreightTemplateBO freightTemplate) {
        final FreightBO freight = freightTemplate.getFreight();
        final List<FreightDetailBO> freightDetailList = freightTemplate.getFreightDetailList();

        deleteFreightDetails(freightDetailList);

        final String freightCode = freight.getTemplateCode();
        final String freightKey = getFreightCacheKey(freightCode);

        this.redisCacheClient.delete(freightKey);
    }

    @Override
    public void deleteFreightDetails(final List<FreightDetailBO> freightDetailList) {
        if (freightDetailList != null && freightDetailList.size() > 0) {
            final String freightCode = freightDetailList.get(0).getTemplateCode();
            final String freightDetailKey = getFreightDetailCacheKey(freightCode);

            final List<String> tmpDetailIdList = getTemplateDetailId(freightDetailList);

            executeDeleteFreightDetailScript(freightDetailKey, tmpDetailIdList);
        }
    }

    @Override
    public FreightPriceBO getFreightPrice(final String templateCode,  final String regionCode) {
        // 初始值为默认承运商
        final String defaultKey = getFreightPriceKey(templateCode, DEFAULT_REGION);
        String key = defaultKey;

        // 承运商为空、地区不为空时，承运商=默认运费行对应的承运商，地区=传入的地区编码
        if (StringUtils.isNotEmpty(regionCode)) {
            FreightDetailBO defaultFreightDetail = getDefaultFreightDetail(templateCode);
            if (defaultFreightDetail != null) {
                LOG.info("templateCode={}, carrierCode={}, regionCode={}", templateCode,  regionCode);
                key = getFreightPriceKey(templateCode, regionCode);
            }
        } else {
            key = getFreightPriceKey(templateCode, regionCode);
        }

        String jsonStr = this.redisCacheClient.opsForValue().get(key);
        if (StringUtils.isEmpty(jsonStr)) {
            jsonStr = this.redisCacheClient.opsForValue().get(defaultKey);
        }

        if (jsonStr != null) {
            return FastJsonHelper.stringToObject(jsonStr, FreightPriceBO.class);
        }

        return null;
    }

    @Override
    public FreightDetailBO getFreightDetail(final String templateCode, final Long detailId) {
        final String key = getFreightDetailCacheKey(templateCode);
        final String jsonObj = this.redisCacheClient.<String, String>opsForHash().get(key, detailId);
        if (StringUtils.isBlank(jsonObj)) {
            return FastJsonHelper.stringToObject(jsonObj, FreightDetailBO.class);
        }

        return null;
    }

    @Override
    public FreightBO getFreight(final String templateCode) {
        final String key = getFreightCacheKey(templateCode);
        final String jsonStr = this.redisCacheClient.opsForValue().get(key);

        if (jsonStr != null) {
            return FastJsonHelper.stringToObject(jsonStr, FreightBO.class);
        }

        return null;
    }

    @Override
    public FreightDetailBO getDefaultFreightDetail(final String templateCode) {
        final String freightDetailKey = getFreightDetailCacheKey(templateCode);
        final Map<String, String> hashMap = this.redisCacheClient.<String, String>opsForHash().entries(freightDetailKey);

        for (String var : hashMap.values()) {
            if (StringUtils.isBlank(var)) {
                continue;
            }
            final FreightDetailBO freightDetail = FastJsonHelper.stringToObject(var, FreightDetailBO.class);

            if (freightDetail.getDefaultFlag() != null && freightDetail.getDefaultFlag() == 1) {
                return freightDetail;
            }
        }

        return null;
    }

    /**
     * 获取运费模板redis缓存key
     *
     * @param freightCode 运费模板编码
     * @return 运费模板redis缓存key
     */
    private String getFreightCacheKey(final String freightCode) {
        return String.format(MetadataConstants.FreightCache.FREIGHT_KEY, freightCode);
    }

    /**
     * 获取运费模板明细redis缓存key
     *
     * @param freightCode 运费模板编码
     * @return 运费模板明细redis缓存key
     */
    private String getFreightDetailCacheKey(final String freightCode) {
        return String.format(MetadataConstants.FreightCache.FREIGHT_DETAIL_KEY, freightCode);
    }

    /**
     * 获取运费模板价格行redis缓存key
     *
     * @param freightCode 运费模板编码
     * @param regionCode  地区编码
     * @return 运费模板价格行redis缓存key
     */
    private String getFreightPriceKey(final String freightCode, final String regionCode) {
        return String.format(MetadataConstants.FreightCache.FREIGHT_PRICE_KEY, freightCode, regionCode);
    }

    /**
     * 将运费模板明细列表转换成Map(key为运费模板明细ID, value为运费模板明细json字符串)
     *
     * @param freightDetailList 运费模板明细列表
     * @return Map
     */
    private Map<String, String> convertToFreightDetailMap(final List<FreightDetailBO> freightDetailList) {
        final Map<String, String> freightDetailMap = new HashMap<>(freightDetailList.size());
        for (FreightDetailBO freightDetail : freightDetailList) {
            freightDetailMap.put
                    (freightDetail.getTemplateDetailId().toString(), FastJsonHelper.objectToString(freightDetail));
        }

        return freightDetailMap;
    }

    /**
     * 获取运费模板明细列表的所有ID
     *
     * @param freightDetailList 运费模板明细列表
     * @return 运费模板明细ID列表
     */
    private List<String> getTemplateDetailId(final List<FreightDetailBO> freightDetailList) {
        final List<String> tmpDetailIdList = new ArrayList<>();
        for (FreightDetailBO freightDetail : freightDetailList) {
            tmpDetailIdList.add(freightDetail.getTemplateDetailId().toString());
        }

        return tmpDetailIdList;
    }

    /**
     * 将运费模板明细列表转换成Map(key为运费价格行redis key, value为运费价格行json字符串)
     *
     * @param freightDetailList 运费模板明细列表
     * @return 结果
     */
    private Map<String, String> convertToFreightPriceMap(final List<FreightDetailBO> freightDetailList) {
        final Map<String, String> freightPriceMap = new HashMap<>(16);
        for (FreightDetailBO freightDetail : freightDetailList) {
            final FreightPriceBO price = new FreightPriceBO();
            price.setFirstPieceWeight(freightDetail.getFirstPieceWeight());
            price.setFirstPrice(freightDetail.getFirstPrice());
            price.setNextPieceWeight(freightDetail.getNextPieceWeight());
            price.setNextPrice(freightDetail.getNextPrice());

            final String freightPriceKey = getFreightPriceKey
                    (freightDetail.getTemplateCode(),freightDetail.getRegionCode());
            final String freightPriceStr = FastJsonHelper.objectToString(price);
            freightPriceMap.put(freightPriceKey, freightPriceStr);

            if (freightDetail.getDefaultFlag() != null && freightDetail.getDefaultFlag() == 1) {
                final String defaultPriceKey = getFreightPriceKey(freightDetail.getTemplateCode(), DEFAULT_REGION);
                freightPriceMap.put(defaultPriceKey, freightPriceStr);
            }
        }

        return freightPriceMap;
    }

    /**
     * 更新运费模板明细redis缓存
     *
     * @param freightDetailKey 运费模板明细redis key
     * @param freightDetailMap 运费模板明细Map
     * @param freightPriceMap  运费价格行Map
     */
    private void executeSaveFreightDetailScript(final String freightDetailKey,
                                                final Map<String, String> freightDetailMap,
                                                final Map<String, String> freightPriceMap) {
        final DefaultRedisScript<Boolean> defaultRedisScript = new DefaultRedisScript<>();
        defaultRedisScript.setScriptSource(MetadataConstants.FreightCache.SAVE_FREIGHT_DETAIL_CACHE_LUA);
        LOG.info("freightDetailMap json = {}", FastJsonHelper.objectToString(freightDetailMap));
        LOG.info("freightPriceMap json = {}", FastJsonHelper.objectToString(freightPriceMap));
        this.redisCacheClient.execute(defaultRedisScript, Collections.singletonList(freightDetailKey), FastJsonHelper.objectToString(freightDetailMap), FastJsonHelper.objectToString(freightPriceMap));
    }

    /**
     * 清除运费模板明细redis缓存
     *
     * @param freightDetailKey 运费模板明细redis key
     * @param tmpDetailIdList  运费模板明细ID列表
     */
    private void executeDeleteFreightDetailScript(final String freightDetailKey,
                                                  final List<String> tmpDetailIdList) {
        final DefaultRedisScript<Boolean> defaultRedisScript = new DefaultRedisScript<>();
        defaultRedisScript.setScriptSource(MetadataConstants.FreightCache.DELETE_FREIGHT_DETAIL_CACHE_LUA);
        LOG.info("tmpDetailIdList json = {}", FastJsonHelper.objectToString(tmpDetailIdList));
        this.redisCacheClient.execute(defaultRedisScript, Collections.singletonList(freightDetailKey), FastJsonHelper.objectToString(tmpDetailIdList));
    }
}
