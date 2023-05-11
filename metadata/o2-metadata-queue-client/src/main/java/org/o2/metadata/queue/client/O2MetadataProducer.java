package org.o2.metadata.queue.client;

import org.hzero.core.util.ValidUtils;
import org.o2.metadata.queue.client.constant.O2MetadataQueueConstants;
import org.o2.metadata.queue.client.domain.MerchantInfoBO;
import org.o2.queue.app.service.ProducerService;
import org.o2.queue.domain.context.ProducerContext;

import javax.validation.Validator;

/**
 * 元数据队列发送client
 *
 * @author chao.yang05@hand-china.com 2023-05-10
 */
public class O2MetadataProducer {

    private final ProducerService producerService;
    private final Validator validator;

    public O2MetadataProducer(ProducerService producerService, Validator validator) {
        this.producerService = producerService;
        this.validator = validator;
    }

    /**
     * 商家元数据初始化队列发送
     *
     * @param merchantInfo 商家信息
     */
    public void pushMerchantInitQueue(MerchantInfoBO merchantInfo) {
        ValidUtils.valid(validator, merchantInfo);
        ProducerContext<MerchantInfoBO> context = new ProducerContext<>();
        context.setData(merchantInfo);
        context.setQueueCode(O2MetadataQueueConstants.QueueCode.O2MD_MERCHANT_META_INIT_EVT);
        producerService.produce(merchantInfo.getTenantId(), context);
    }
}
