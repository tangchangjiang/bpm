package org.o2.metadata.console.app.service.impl;

import org.o2.core.helper.FastJsonHelper;
import org.o2.data.redis.client.RedisCacheClient;
import org.o2.metadata.console.app.bo.FreightBO;
import org.o2.metadata.console.app.bo.FreightDetailBO;
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
        final String freightStr = FastJsonHelper.objectToString(freight);
        final String freightInforKey = getFreightInforCacheKey(freight.getTenantId(),freight.getTemplateCode());
        this.redisCacheClient.opsForHash().put (freightInforKey, MetadataConstants.FreightCache.FREIGHT_HEAD_KEY ,freightStr);
        saveFreightDetails(freightDetailList);

        //更新默认模板
        if( freight.getDafaultFlag()==1 ) {
            saveFreightDefault(freightTemplate);
        }
    }

    /***
     * 更新默认模板
     * @param freightTemplate
     */
    public void saveFreightDefault(final FreightTemplateBO freightTemplate) {
        final FreightBO freight = freightTemplate.getFreight();
        if (freight.getDafaultFlag()==1 ) {return;}

        final List<FreightDetailBO> freightDetailList = freightTemplate.getFreightDetailList();
        final String freightStr = FastJsonHelper.objectToString(freight);
        String templateCode =  MetadataConstants.FreightCache.FREIGHT_DEFAULT_KEY;
        final String freightInforKey = getFreightInforCacheKey(freight.getTenantId(),templateCode);
        this.redisCacheClient.delete(freightInforKey);
        this.redisCacheClient.opsForHash().put (freightInforKey, MetadataConstants.FreightCache.FREIGHT_HEAD_KEY ,freightStr);
        saveFreightDetails(freightDetailList);
    }

    @Override
    public void saveFreightDetails(final List<FreightDetailBO> freightDetailList) {
        if (freightDetailList != null && freightDetailList.size() > 0) {
            final String freightCode = freightDetailList.get(0).getTemplateCode();
            final long tenantId = freightDetailList.get(0).getTenantId();
            final String freightDetailKey = getFreightInforCacheKey(tenantId,freightCode);

            final Map<String, String> freightDetailMap = convertToFreightDetailMap(freightDetailList);

            //运费模板头信息
         //  final Map<String, String> freightHeadeMap = new HashMap<>();
        //   freightHeadeMap.put(MetadataConstants.FreightCache.FREIGHT_DEFAULT_LINE_KEY, FastJsonHelper.objectToString(freight));

            executeSaveFreightInforScript(freightDetailKey, freightDetailMap);
        }
    }

    @Override
    public void deleteFreight(final FreightTemplateBO freightTemplate) {
        final FreightBO freight = freightTemplate.getFreight();
        final long tenantId = freight.getTenantId();
        final String freightCode = freight.getTemplateCode();
        final String freightKey = getFreightInforCacheKey( tenantId,freightCode);
        this.redisCacheClient.delete(freightKey);
    }

    @Override
    public void deleteFreightDetails(final List<FreightDetailBO> freightDetailList) {
        if (freightDetailList != null && freightDetailList.size() > 0) {
            final String freightCode = freightDetailList.get(0).getTemplateCode();
            final long tenantId = freightDetailList.get(0).getTenantId();
           // final String freightCodeDefault = freightDetailList.get(0).getDefaultFlag()==1? MetadataConstants.FreightCache.FREIGHT_DEFAULT_LINE_KEY :"" ;
            final String freightDetailKey = getFreightInforCacheKey(tenantId,freightCode);
            final List<String> tmpDetailIdList = getTemplateDetailRegionCodeList(freightDetailList);

            executeDeleteFreightDetailScript(freightDetailKey, tmpDetailIdList);
        }
    }

    /**
     * 获取运费模板明细redis缓存key
     *
     * @param freightCode 运费模板编码
     * @return 运费模板明细redis缓存key
     */
    private String getFreightInforCacheKey(final Long tenantId, final String freightCode) {
        return String.format(MetadataConstants.FreightCache.FREIGHT_DETAIL_KEY,tenantId, freightCode);
    }


    /**
     * 将运费模板明细列表转换成Map(key为运费模板明细的地区${region}；默认的为 DEFAULT, value为运费模板明细json字符串)
     *
     * @param freightDetailList 运费模板明细列表
     * @return Map
     */
    private Map<String, String> convertToFreightDetailMap(final List<FreightDetailBO> freightDetailList) {
        final Map<String, String> freightDetailMap = new HashMap<>(freightDetailList.size());
        for (FreightDetailBO freightDetail : freightDetailList) {
            if (freightDetail.getDefaultFlag()!=null&&freightDetail.getDefaultFlag()==1){
                freightDetailMap.put
                        (MetadataConstants.FreightCache.FREIGHT_DEFAULT_LINE_KEY, FastJsonHelper.objectToString(freightDetail));
            }else{
                freightDetailMap.put
                        (freightDetail.getRegionCode(), FastJsonHelper.objectToString(freightDetail));
            }

        }

        return freightDetailMap;
    }

    /**
     * 获取运费模板明细列表的所有地区（默认模板为DEFAULT)
     *
     * @param freightDetailList 运费模板明细列表
     * @return 运费模板明细地区列表
     */
    private List<String> getTemplateDetailRegionCodeList(final List<FreightDetailBO> freightDetailList) {
        final List<String> tmpDetailIdRegionCodeList = new ArrayList<>();
        for (FreightDetailBO freightDetail : freightDetailList) {
            tmpDetailIdRegionCodeList.add(freightDetail.getDefaultFlag()==1?MetadataConstants.FreightCache.FREIGHT_DEFAULT_LINE_KEY:freightDetail.getRegionCode());
        }
        return tmpDetailIdRegionCodeList;
    }


    /**
     * 更新运费模板redis缓存
     *
     * @param freightDetailKey 运费模板明细redis key
     * @param freightDetailMap 运费模板明细Map
     */
    private void executeSaveFreightInforScript(final String freightDetailKey,
                                               final Map<String, String> freightDetailMap) {
        final DefaultRedisScript<Boolean> defaultRedisScript = new DefaultRedisScript<>();
        defaultRedisScript.setScriptSource(MetadataConstants.FreightCache.SAVE_FREIGHT_DETAIL_CACHE_LUA);
      //  String freightHeadMapStr = ObjectUtils.isEmpty(freightHeadMap)? FastJsonHelper.objectToString(freightHeadMap) :"" ;
        //   LOG.info("freightHeadlMap json = {}", freightHeadMapStr);
        LOG.info("freightDetailMap json = {}", FastJsonHelper.objectToString(freightDetailMap));
        this.redisCacheClient.execute(defaultRedisScript, Collections.singletonList(freightDetailKey), FastJsonHelper.objectToString(freightDetailMap));
    }

    /**
     * 清除运费模板明细redis缓存
     *
     * @param freightDetailKey 运费模板明细redis key
     * @param templateRegionCodeList  运费模板明细ID列表
     */
    private void executeDeleteFreightDetailScript(final String freightDetailKey,
                                                  final List<String> templateRegionCodeList) {
        final DefaultRedisScript<Boolean> defaultRedisScript = new DefaultRedisScript<>();
        defaultRedisScript.setScriptSource(MetadataConstants.FreightCache.DELETE_FREIGHT_DETAIL_CACHE_LUA);
        LOG.info("templateRegionCodeList json = {}", FastJsonHelper.objectToString(templateRegionCodeList));
        this.redisCacheClient.execute(defaultRedisScript, Collections.singletonList(freightDetailKey), FastJsonHelper.objectToString(templateRegionCodeList));
    }
}
