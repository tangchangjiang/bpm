package org.o2.metadata.app.service.impl;

import org.apache.commons.lang3.StringUtils;
import org.o2.core.helper.FastJsonHelper;
import org.o2.data.redis.client.RedisCacheClient;
import org.o2.metadata.app.bo.FreightBO;
import org.o2.metadata.app.bo.FreightDetailBO;
import org.o2.metadata.app.bo.FreightPriceBO;
import org.o2.metadata.app.bo.FreightTemplateBO;
import org.o2.metadata.app.service.FreightCacheService;
import org.o2.metadata.infra.constants.MetadataConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.script.DefaultRedisScript;

import java.util.*;

/**
 * 运费模板缓存服务默认实现
 *
 * @author peng.xu@hand-china.com 2019-06-18
 */

public class FreightCacheServiceImpl implements FreightCacheService {
    private final static Logger LOG = LoggerFactory.getLogger(FreightCacheServiceImpl.class);

    private final static String DEFAULT_CARRIER = "default";
    private final static String DEFAULT_REGION = "null";

    private RedisCacheClient redisCacheClient;

    public FreightCacheServiceImpl(final RedisCacheClient redisCacheClient) {
        this.redisCacheClient = redisCacheClient;
    }

    @Override
    public void saveFreight(final FreightTemplateBO freightTemplateBO) {
        final FreightBO freightBO = freightTemplateBO.getFreightBO();
        final List<FreightDetailBO> freightDetailBOList = freightTemplateBO.getFreightDetailBOList();

        final String freightCode = freightBO.getTemplateCode();
        final String freightStr = FastJsonHelper.objectToString(freightBO);
        final String freightKey = getFreightCacheKey(freightCode);
        this.redisCacheClient.opsForValue().set(freightKey, freightStr);

        saveFreightDetails(freightDetailBOList);
    }

    @Override
    public void saveFreightDetails(final List<FreightDetailBO> freightDetailBOList) {
        if (freightDetailBOList != null && freightDetailBOList.size() > 0) {
            final String freightCode = freightDetailBOList.get(0).getTemplateCode();
            final String freightDetailKey = getFreightDetailCacheKey(freightCode);

            final Map<String, String> freightDetailMap = convertToFreightDetailMap(freightDetailBOList);
            final Map<String, String> freightPriceMap = convertToFreightPriceMap(freightDetailBOList);

            executeSaveFreightDetailScript(freightDetailKey, freightDetailMap, freightPriceMap);
        }
    }

    @Override
    public void deleteFreight(final FreightTemplateBO freightTemplateBO) {
        final FreightBO freightBO = freightTemplateBO.getFreightBO();
        final List<FreightDetailBO> freightDetailBOList = freightTemplateBO.getFreightDetailBOList();

        deleteFreightDetails(freightDetailBOList);

        final String freightCode = freightBO.getTemplateCode();
        final String freightKey = getFreightCacheKey(freightCode);

        this.redisCacheClient.delete(freightKey);
    }

    @Override
    public void deleteFreightDetails(final List<FreightDetailBO> freightDetailBOList) {
        if (freightDetailBOList != null && freightDetailBOList.size() > 0) {
            final String freightCode = freightDetailBOList.get(0).getTemplateCode();
            final String freightDetailKey = getFreightDetailCacheKey(freightCode);

            final List<String> tmpDetailIdList = getTemplateDetailId(freightDetailBOList);

            executeDeleteFreightDetailScript(freightDetailKey, tmpDetailIdList);
        }
    }

    @Override
    public FreightPriceBO getFreightPrice(final String templateCode, final String carrierCode, final String regionCode) {
        // 初始值为默认承运商
        final String defaultKey = getFreightPriceKey(templateCode, DEFAULT_CARRIER, DEFAULT_REGION);
        String key = defaultKey;

        // 承运商为空、地区不为空时，承运商=默认运费行对应的承运商，地区=传入的地区编码
        if (StringUtils.isEmpty(carrierCode) && StringUtils.isNotEmpty(regionCode)) {
            FreightDetailBO defaultFreightDetail = getDefaultFreightDetail(templateCode);
            if (defaultFreightDetail != null) {
                LOG.info("templateCode={}, carrierCode={}, regionCode={}", templateCode, defaultFreightDetail.getCarrierCode(), regionCode);
                key = getFreightPriceKey(templateCode, defaultFreightDetail.getCarrierCode(), regionCode);
            }
        } else {
            key = getFreightPriceKey(templateCode, carrierCode, regionCode);
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
            final FreightDetailBO freightDetailBO = FastJsonHelper.stringToObject(var, FreightDetailBO.class);

            if (freightDetailBO.getIsDefault() != null && freightDetailBO.getIsDefault() == 1) {
                return freightDetailBO;
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
     * @param carrierCode 承运商编码
     * @param regionCode  地区编码
     * @return 运费模板价格行redis缓存key
     */
    private String getFreightPriceKey(final String freightCode, final String carrierCode, final String regionCode) {
        return String.format(MetadataConstants.FreightCache.FREIGHT_PRICE_KEY, freightCode, carrierCode, regionCode);
    }

    /**
     * 将运费模板明细列表转换成Map(key为运费模板明细ID, value为运费模板明细json字符串)
     *
     * @param freightDetailBOList 运费模板明细列表
     * @return Map
     */
    private Map<String, String> convertToFreightDetailMap(final List<FreightDetailBO> freightDetailBOList) {
        final Map<String, String> freightDetailMap = new HashMap<>(freightDetailBOList.size());
        for (FreightDetailBO freightDetailBO : freightDetailBOList) {
            freightDetailMap.put
                    (freightDetailBO.getTemplateDetailId().toString(), FastJsonHelper.objectToString(freightDetailBO));
        }

        return freightDetailMap;
    }

    /**
     * 获取运费模板明细列表的所有ID
     *
     * @param freightDetailBOList 运费模板明细列表
     * @return 运费模板明细ID列表
     */
    private List<String> getTemplateDetailId(final List<FreightDetailBO> freightDetailBOList) {
        final List<String> tmpDetailIdList = new ArrayList<>();
        for (FreightDetailBO freightDetailBO : freightDetailBOList) {
            tmpDetailIdList.add(freightDetailBO.getTemplateDetailId().toString());
        }

        return tmpDetailIdList;
    }

    /**
     * 将运费模板明细列表转换成Map(key为运费价格行redis key, value为运费价格行json字符串)
     *
     * @param freightDetailBOList 运费模板明细列表
     * @return 结果
     */
    private Map<String, String> convertToFreightPriceMap(final List<FreightDetailBO> freightDetailBOList) {
        final Map<String, String> freightPriceMap = new HashMap<>(16);
        for (FreightDetailBO freightDetailBO : freightDetailBOList) {
            final FreightPriceBO priceBO = new FreightPriceBO();
            priceBO.setFirstPieceWeight(freightDetailBO.getFirstPieceWeight());
            priceBO.setFirstPrice(freightDetailBO.getFirstPrice());
            priceBO.setNextPieceWeight(freightDetailBO.getNextPieceWeight());
            priceBO.setNextPrice(freightDetailBO.getNextPrice());

            final String freightPriceKey = getFreightPriceKey
                    (freightDetailBO.getTemplateCode(), freightDetailBO.getCarrierCode(), freightDetailBO.getRegionCode());
            final String freightPriceStr = FastJsonHelper.objectToString(priceBO);
            freightPriceMap.put(freightPriceKey, freightPriceStr);

            if (freightDetailBO.getIsDefault() != null && freightDetailBO.getIsDefault() == 1) {
                final String defaultPriceKey = getFreightPriceKey(freightDetailBO.getTemplateCode(), DEFAULT_CARRIER, DEFAULT_REGION);
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
