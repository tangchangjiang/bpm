package org.o2.metadata.app.service.impl;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.hzero.core.base.BaseConstants;
import org.o2.core.helper.FastJsonHelper;
import org.o2.data.redis.client.RedisCacheClient;

import org.o2.metadata.api.dto.AddressDTO;
import org.o2.metadata.api.dto.FreightDTO;
import org.o2.metadata.api.dto.OrderEntryDTO;
import org.o2.metadata.app.bo.FreightTemplateBO;
import org.o2.metadata.app.bo.FreightTemplateDetailBO;
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

        List<String> platformSkuCodeList = new ArrayList<>();
        //商品关联运费模版redis key
        String skuRelFreightKey = FreightConstants.RedisKey.getSkuRelFreightKey(freight.getTenantId());
        Map<String, OrderEntryDTO> orderEntryMap = new HashMap<>();
        for (OrderEntryDTO entry : orderEntryDTOList) {
            //平台商品编码
            platformSkuCodeList.add(entry.getPlatformSkuCode());
            orderEntryMap.put(entry.getPlatformSkuCode(), entry);
        }
        //商品关联运费模版
        List<String> freightRelTemplateStr = redisCacheClient.<String, String>opsForHash().multiGet(skuRelFreightKey, platformSkuCodeList);

        /**
         *  拼接运费编码与订单行关联集合map，运费编码与商品运费集合map
         *  templateRelProductMap:运费模版关联订单行,一对多 key:运费模版编码 value:订单行集合
         *  productRelFreightMap: key : 运费模版编码 value: 商品运费关联关系类
         *
         */
        Map<String, List<OrderEntryDTO>> templateRelProductMap = new HashMap<>();
        Map<String, ProductRelFreightBO> productRelFreightMap = new HashMap<>();
        for (int i = 0; i < platformSkuCodeList.size(); i++) {
            String platformSkuCode = platformSkuCodeList.get(i);
            String str = freightRelTemplateStr.get(i);
            //商品无关联运费模版
            if (StringUtils.isEmpty(str)) {
                continue;
            }
            ProductRelFreightBO productRelFreight = FastJsonHelper.stringToObject(str, ProductRelFreightBO.class);
            //运费模版
            String freightTemplateCode = productRelFreight.getTemplateCode();
            List<OrderEntryDTO> orderEntryList = (null == templateRelProductMap.get(freightTemplateCode)) ? new ArrayList<>() : templateRelProductMap.get(freightTemplateCode);
            orderEntryList.add(orderEntryMap.get(platformSkuCode));
            templateRelProductMap.put(freightTemplateCode, orderEntryList);
            productRelFreightMap.put(freightTemplateCode, productRelFreight);
        }
        return calculateFreight(freight.getAddress(), productRelFreightMap, templateRelProductMap, freight.getTenantId());

    }

    /**
     * 计算运费
     * @param address 地址信息
     * @param productRelFreightMap 商品运费集合
     * @param templateRelProductMap 运费模版对应的订单行
     * @param tenantId 租户ID
     * @return 运费
     */
    private BigDecimal calculateFreight(AddressDTO address,
                                        Map<String, ProductRelFreightBO> productRelFreightMap,
                                        Map<String, List<OrderEntryDTO>> templateRelProductMap,
                                        Long tenantId) {
        BigDecimal deliveryAmount = BigDecimal.ZERO;
        //市区编码
        String cityCode = address.getCityCode();
        for (Map.Entry<String, List<OrderEntryDTO>> entry : templateRelProductMap.entrySet()) {
            String freightDetailKey = FreightConstants.RedisKey.getFreightDetailKey(tenantId, entry.getKey());
            //模版详情 依次取 地区模版，运费模版头，运费模版默认行信息
            List<String> freightTemplateStr = redisCacheClient.<String, String>opsForHash().multiGet(freightDetailKey, Arrays.asList(cityCode, FreightConstants.RedisKey.FREIGHT_HEAD_KEY, FreightConstants.RedisKey.FREIGHT_DEFAULT_KEY));
            // 是否有运费模版详情
            boolean isEmpty = CollectionUtils.isEmpty(freightTemplateStr) || StringUtils.isEmpty(freightTemplateStr.get(0));
            if (isEmpty) {
                continue;
            }
            //运费模版头
            FreightTemplateBO freightTemplateHead = FastJsonHelper.stringToObject(freightTemplateStr.get(0), FreightTemplateBO.class);
            //运费模版详情信息
            String freightTemplateDetailStr = StringUtils.isEmpty(freightTemplateStr.get(0)) ? freightTemplateStr.get(2) : freightTemplateStr.get(0);
            //不包邮但无模版详情信息
            if (BaseConstants.Flag.NO.equals(freightTemplateHead.getDeliveryFreeFlag()) && StringUtils.isEmpty(freightTemplateDetailStr)) {
                continue;
            }
            FreightTemplateDetailBO freightTemplateDetail = FastJsonHelper.stringToObject(freightTemplateDetailStr, FreightTemplateDetailBO.class);
            String valuationType = freightTemplateHead.getValuationType();
            BigDecimal unitTotal = unitTotal(productRelFreightMap, entry.getKey(), entry.getValue(), valuationType);
            if (unitTotal.compareTo(freightTemplateDetail.getFirstPieceWeight()) <= 0) {
                deliveryAmount = deliveryAmount.add(freightTemplateDetail.getFirstPrice());
            } else {
                BigDecimal remainWeight = unitTotal.subtract(freightTemplateDetail.getFirstPieceWeight());
                BigDecimal[] result = remainWeight.divideAndRemainder(freightTemplateDetail.getNextPieceWeight());
                BigDecimal times = result[1].setScale(3, BigDecimal.ROUND_HALF_UP).compareTo(BigDecimal.ZERO) == 0 ? result[0] : result[0].add(new BigDecimal(1));
                deliveryAmount = deliveryAmount.add(freightTemplateDetail.getFirstPrice().add(freightTemplateDetail.getNextPrice().multiply(times)));
            }
        }
        return deliveryAmount;
    }
    /**
     * 计价单位求和
     * @param productRelFreightMap 商品运费集合
     * @param templateCode 运费模版编码
     * @param orderEntryList 订单行集合
     * @param valuationType 计价方式
     * @return 总单位
     */
    private BigDecimal unitTotal(Map<String, ProductRelFreightBO> productRelFreightMap,
                                 String templateCode,
                                 List<OrderEntryDTO> orderEntryList,
                                 String valuationType) {
        //单位求和
        BigDecimal calcUnit = BigDecimal.ZERO;
        ProductRelFreightBO productRelFreight = productRelFreightMap.get(templateCode);
        for (OrderEntryDTO orderEntry : orderEntryList) {
            BigDecimal qty = orderEntry.getQuantity() == null ? BigDecimal.ZERO : BigDecimal.valueOf(orderEntry.getQuantity());
            switch (valuationType) {
                case FreightConstants.ValuationType.WEIGHT:
                    BigDecimal weightValue = productRelFreight.getWeight() == null ? BigDecimal.ZERO : productRelFreight.getWeight();
                    calcUnit = calcUnit.add(weightValue.multiply(qty));
                    break;
                case FreightConstants.ValuationType.VOLUME:
                    BigDecimal volumeValue = productRelFreight.getVolume() == null ? BigDecimal.ZERO : productRelFreight.getVolume();
                    calcUnit = calcUnit.add(volumeValue.multiply(qty));
                    break;
                case FreightConstants.ValuationType.PIECE:
                    calcUnit = calcUnit.add(qty);
                    break;
                default:
                    break;
            }
        }
        return calcUnit;
    }
}
