package org.o2.metadata.app.service.impl;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.hzero.core.base.BaseConstants;
import org.o2.core.helper.FastJsonHelper;
import org.o2.data.redis.client.RedisCacheClient;

import org.o2.metadata.api.dto.FreightDTO;
import org.o2.metadata.api.dto.OrderEntryDTO;
import org.o2.metadata.app.bo.FreightTemplateBO;
import org.o2.metadata.app.bo.FreightTemplateDetailBO;
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
        Map<String, List<OrderEntryDTO>> templateRelProductMap = new HashMap<>();
        // 以运费模版为key 分组
        for (OrderEntryDTO entry : orderEntryDTOList) {
            //运费模版编码
            String freightTemplateCode = entry.getFreightTemplateCode();
            if (StringUtils.isEmpty(freightTemplateCode)) {
                continue;
            }
            List<OrderEntryDTO> orderEntryList = (null == templateRelProductMap.get(freightTemplateCode)) ? new ArrayList<>() : templateRelProductMap.get(freightTemplateCode);
            orderEntryList.add(entry);
            templateRelProductMap.put(freightTemplateCode, orderEntryList);
        }
        String cityCode = freight.getAddress().getCityCode();
        //运费
        BigDecimal deliveryAmount = BigDecimal.ZERO;
        for (Map.Entry<String, List<OrderEntryDTO>> entry : templateRelProductMap.entrySet()) {
            String key = entry.getKey();
            List<OrderEntryDTO> value = entry.getValue();
            String freightDetailKey = FreightConstants.RedisKey.getFreightDetailKey(freight.getTenantId(), key);
            //模版详情 依次取 地区模版0，运费模版头1，运费模版默认行信息2
            List<String> freightTemplates = redisCacheClient.<String, String>opsForHash().multiGet(freightDetailKey, Arrays.asList(cityCode, FreightConstants.RedisKey.FREIGHT_HEAD_KEY, FreightConstants.RedisKey.FREIGHT_DEFAULT_KEY));
            //获取运费模版头行
            Map<String, String> map = this.getTemplateInfo(freightTemplates);
            //计算相同模版的商品运费金额
            BigDecimal amount = this.calculateFreight(map, value);
            deliveryAmount = deliveryAmount.add(amount);
        }
        return deliveryAmount;

    }

    /**
     * 获取运费模版头和详情
     * @param freightTemplates 运费模版集合
     * @return map
     */
    private Map<String, String> getTemplateInfo(List<String> freightTemplates) {
        Map<String, String> map = new HashMap<>();
        Boolean isEmptyTemplate = CollectionUtils.isEmpty(freightTemplates) || StringUtils.isEmpty(freightTemplates.get(1));
        if (Boolean.TRUE.equals(isEmptyTemplate)) {
            return map;
        }
        //运费模版头
        FreightTemplateBO freightTemplateHead = FastJsonHelper.stringToObject(freightTemplates.get(1), FreightTemplateBO.class);
        //运费模版详情信息
        String freightTemplateDetailStr = StringUtils.isEmpty(freightTemplates.get(0)) ? freightTemplates.get(2) : freightTemplates.get(0);
        //不包邮但无模版信息
        isEmptyTemplate = BaseConstants.Flag.NO.equals(freightTemplateHead.getDeliveryFreeFlag()) && StringUtils.isEmpty(freightTemplateDetailStr);
        if (Boolean.TRUE.equals(isEmptyTemplate)) {
            return map;
        }
        map.put(FreightConstants.Template.TEMPLATE_HEAD, freightTemplates.get(1));
        map.put(FreightConstants.Template.TEMPLATE_DETAIL, freightTemplateDetailStr);
        return map;
    }

    /**
     * 计算相同模版的商品运费金额
     * @param map 模版信息  模版头和模版详情
     * @param orderEntryList 订单行详情
     * @return 运费
     */
    private BigDecimal calculateFreight(Map<String, String> map, List<OrderEntryDTO> orderEntryList) {
        BigDecimal deliveryAmount = BigDecimal.ZERO;
        if (map.isEmpty()) {
            return deliveryAmount;
        }
        FreightTemplateDetailBO freightTemplateDetail = FastJsonHelper.stringToObject(map.get(FreightConstants.Template.TEMPLATE_HEAD), FreightTemplateDetailBO.class);
        FreightTemplateBO freightTemplateHead = FastJsonHelper.stringToObject(map.get(FreightConstants.Template.TEMPLATE_DETAIL), FreightTemplateBO.class);
        String valuationType = freightTemplateHead.getValuationType();
        BigDecimal unitTotal = this.unitTotal(orderEntryList, valuationType);
        if (unitTotal.compareTo(freightTemplateDetail.getFirstPieceWeight()) <= 0) {
            deliveryAmount = deliveryAmount.add(freightTemplateDetail.getFirstPrice());
        } else {
            BigDecimal remainWeight = unitTotal.subtract(freightTemplateDetail.getFirstPieceWeight());
            BigDecimal[] result = remainWeight.divideAndRemainder(freightTemplateDetail.getNextPieceWeight());
            BigDecimal times = result[1].setScale(3, BigDecimal.ROUND_HALF_UP).compareTo(BigDecimal.ZERO) == 0 ? result[0] : result[0].add(new BigDecimal(1));
            deliveryAmount = deliveryAmount.add(freightTemplateDetail.getFirstPrice().add(freightTemplateDetail.getNextPrice().multiply(times)));
        }
        return deliveryAmount;
    }

    /**
     * 计价单位求和
     * @param orderEntryList 订单行集合
     * @param valuationType 计价方式
     * @return 总单位
     */
    private BigDecimal unitTotal(List<OrderEntryDTO> orderEntryList,
                                 String valuationType) {
        //单位求和
        BigDecimal calcUnit = BigDecimal.ZERO;
        for (OrderEntryDTO orderEntry : orderEntryList) {
            BigDecimal qty = orderEntry.getQuantity() == null ? BigDecimal.ZERO : BigDecimal.valueOf(orderEntry.getQuantity());
            switch (valuationType) {
                case FreightConstants.ValuationType.WEIGHT:
                    BigDecimal weightValue = orderEntry.getWeight() == null ? BigDecimal.ZERO : orderEntry.getWeight();
                    calcUnit = calcUnit.add(weightValue.multiply(qty));
                    break;
                case FreightConstants.ValuationType.VOLUME:
                    BigDecimal volumeValue = orderEntry.getVolume() == null ? BigDecimal.ZERO : orderEntry.getVolume();
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
