package org.o2.metadata.app.service.impl;

import org.apache.commons.lang3.StringUtils;
import org.o2.core.helper.FastJsonHelper;
import org.o2.data.redis.client.RedisCacheClient;

import org.o2.metadata.api.dto.FreightDTO;
import org.o2.metadata.api.dto.OrderEntryDTO;
import org.o2.metadata.app.bo.FreightTemplateBO;
import org.o2.metadata.app.bo.ProductRelFreightBO;
import org.o2.metadata.app.service.FreightService;
import org.o2.metadata.infra.constants.FreightConstants;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;

/**
 * 运费计算服务默认实现
 *
 * @author peng.xu@hand-china.com 2019-06-18
 */
@Service
public class FreightServiceImpl implements FreightService {

    private RedisCacheClient redisCacheClient;

    public FreightServiceImpl(RedisCacheClient redisCacheClient) {
        this.redisCacheClient = redisCacheClient;
    }

    @Override
    public BigDecimal getFreightAmount(FreightDTO freight) {
        List<OrderEntryDTO> orderEntryDTOList = freight.getOrderEntryList();
        //平台商品编码
        List<String> platformSkuCodeList = new ArrayList<>();
        String skuRelFreightKey = FreightConstants.RedisKey.getSkuRelFreightKey(freight.getTenantId());
        Map<String, OrderEntryDTO> orderEntryMap = new HashMap<>();
        for (OrderEntryDTO entry : orderEntryDTOList) {
            platformSkuCodeList.add(entry.getPlatformSkuCode());
            orderEntryMap.put(entry.getPlatformSkuCode(), entry);
        }
        //商品对应的运费模版编码
        List<String> freightTemplateCodes = redisCacheClient.<String, String>opsForHash().multiGet(skuRelFreightKey, platformSkuCodeList);
        Map<String, List<OrderEntryDTO>> templateRelProductMap = new HashMap<>();
        for (int i = 0; i < platformSkuCodeList.size(); i++) {
            String platformSkuCode = platformSkuCodeList.get(i);
            String str = freightTemplateCodes.get(i);
            List<OrderEntryDTO> orderEntryList = (null == templateRelProductMap.get(platformSkuCode)) ? new ArrayList<>() : templateRelProductMap.get(platformSkuCode);
            orderEntryList.add(orderEntryMap.get(platformSkuCode));
            if (StringUtils.isEmpty(str)) {
                templateRelProductMap.put("false", orderEntryList);
                continue;
            }
            ProductRelFreightBO productRelFreight = FastJsonHelper.stringToObject(str, ProductRelFreightBO.class);
            templateRelProductMap.put(productRelFreight.getTemplateCode(),orderEntryList);
        }

        return null;
    }
}
