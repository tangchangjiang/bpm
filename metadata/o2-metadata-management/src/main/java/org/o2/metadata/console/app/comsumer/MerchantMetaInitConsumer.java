package org.o2.metadata.console.app.comsumer;

import lombok.extern.slf4j.Slf4j;
import org.o2.core.helper.JsonHelper;
import org.o2.metadata.console.app.bo.MerchantInfoBO;
import org.o2.metadata.console.app.service.OnlineShopService;
import org.o2.metadata.console.infra.constant.MetadataConstants;
import org.o2.metadata.console.infra.entity.RetryErrorLog;
import org.o2.metadata.console.infra.repository.RetryErrorLogRepository;
import org.o2.queue.app.service.Consumer;
import org.o2.queue.app.service.impl.AbstractWrapperConsumer;
import org.o2.queue.domain.context.ConsumerContext;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Objects;

/**
 * 商家元数据信息初始化消费
 *
 * @author chao.yang05@hand-china.com 2023-05-10
 */
@Slf4j
@Component("merchantMetaInitConsumer")
public class MerchantMetaInitConsumer extends AbstractWrapperConsumer<MerchantInfoBO> implements Consumer<MerchantInfoBO> {

    private final OnlineShopService onlineShopService;
    private final RetryErrorLogRepository retryErrorLogRepository;

    public MerchantMetaInitConsumer(OnlineShopService onlineShopService,
                                    RetryErrorLogRepository retryErrorLogRepository) {
        this.onlineShopService = onlineShopService;
        this.retryErrorLogRepository = retryErrorLogRepository;
    }

    @Override
    public void consume(Long tenantId, ConsumerContext<MerchantInfoBO> context) {
        MerchantInfoBO merchantInfo = context.getData();
        if (Objects.isNull(merchantInfo)) {
            log.info("MerchantMetaInitConsumer data null");
            return;
        }
        log.info("init merchant meta info, tenantId: {}, data: {}", tenantId, context.getData());
        onlineShopService.syncMerchantInfo(merchantInfo);
    }

    @Override
    public void errorHandler(Long tenantId, ConsumerContext<MerchantInfoBO> context, Exception e) {
        MerchantInfoBO merchantInfo = context.getData();
        log.error("init merchant meta info failed, tenantId: {}, data: {}", tenantId, JsonHelper.objectToString(merchantInfo));
        RetryErrorLog retryErrorLog = RetryErrorLog.builder().errorTime(new Date())
                .processStatus(MetadataConstants.RetryStatus.PROCESS_ERROR)
                .caseCode(MetadataConstants.QueueCode.O2MD_MERCHANT_META_INIT_EVT)
                .errorTime(new Date())
                .requestBody(JsonHelper.objectToString(merchantInfo))
                .errorMessage(e.getMessage())
                .retry(0L)
                .processTime(new Date())
                .tenantId(merchantInfo.getTenantId()).build();
        retryErrorLogRepository.insertSelective(retryErrorLog);
    }
}
