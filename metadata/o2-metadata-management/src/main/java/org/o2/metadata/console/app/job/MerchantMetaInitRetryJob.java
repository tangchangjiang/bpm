package org.o2.metadata.console.app.job;

import lombok.extern.slf4j.Slf4j;
import org.hzero.boot.scheduler.infra.annotation.JobHandler;
import org.hzero.boot.scheduler.infra.tool.SchedulerTool;
import org.hzero.core.base.BaseConstants;
import org.o2.core.helper.JsonHelper;
import org.o2.core.thread.ThreadJobPojo;
import org.o2.metadata.console.app.bo.MerchantInfoBO;
import org.o2.metadata.console.app.service.OnlineShopService;
import org.o2.metadata.console.infra.constant.MetadataConstants;
import org.o2.metadata.console.infra.entity.RetryErrorLog;
import org.o2.metadata.console.infra.repository.RetryErrorLogRepository;
import org.o2.scheduler.job.distributed.AbstractDistributedJobHandler;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 商家初始化重试Job
 *
 * @author chao.yang05@hand-china.com 2023-05-11
 */
@Slf4j
@JobHandler("merchantMetaInitRetryJob")
public class MerchantMetaInitRetryJob extends AbstractDistributedJobHandler<Long> {

    private final RetryErrorLogRepository retryErrorLogRepository;
    private final OnlineShopService onlineShopService;

    public MerchantMetaInitRetryJob(RetryErrorLogRepository retryErrorLogRepository,
                                    OnlineShopService onlineShopService) {
        this.retryErrorLogRepository = retryErrorLogRepository;
        this.onlineShopService = onlineShopService;
    }

    @Override
    public List<Long> rootData(Map<String, String> map, SchedulerTool tool) {
        ThreadJobPojo threadJobPojo = ThreadJobPojo.newThreadJobPojo(map);
        RetryErrorLog queryLog = new RetryErrorLog();
        queryLog.setCaseCode(MetadataConstants.QueueCode.O2MD_MERCHANT_META_INIT_EVT);
        queryLog.setProcessTotal(threadJobPojo.getProcessTotal());
        queryLog.setProcessStatus(MetadataConstants.RetryStatus.PROCESS_ERROR);
        // 设置retry参数
        Map<String, String> jobParams = Optional.ofNullable(threadJobPojo.getJobParams()).orElse(new HashMap<>(BaseConstants.Digital.FOUR));
        String retryCount = jobParams.getOrDefault(RetryErrorLog.FIELD_RETRY,
                String.valueOf(MetadataConstants.RetryStatus.DEFAULT_RETRY_COUNT));
        queryLog.setRetry(Long.parseLong(retryCount));
        return retryErrorLogRepository.listQueueErrorLogId(queryLog);
    }

    @Override
    public void handleData(Map<String, String> map, SchedulerTool tool, List<Long> contents) {
        String ids = contents.stream().map(String::valueOf).collect(Collectors.joining(BaseConstants.Symbol.COMMA));
        List<RetryErrorLog> retryErrorLogs = retryErrorLogRepository.selectByIds(ids);
        for (RetryErrorLog retryErrorLog : retryErrorLogs) {
            try {
                onlineShopService.syncMerchantInfo(JsonHelper.stringToObject(retryErrorLog.getRequestBody(), MerchantInfoBO.class));

                retryErrorLog.setProcessStatus(MetadataConstants.RetryStatus.SUCCESS);
            } catch (Exception e) {
                log.error("StoreOrderConsumerRetryJob handleData : {}", JsonHelper.objectToString(retryErrorLog), e);
                retryErrorLog.setErrorMessage(e.getMessage());
                retryErrorLog.setProcessStatus(MetadataConstants.RetryStatus.PROCESS_ERROR);
            } finally {
                retryErrorLog.setProcessTime(new Date());
                retryErrorLog.setRetry(retryErrorLog.getRetry() + 1);
            }
        }
        retryErrorLogRepository.batchUpdateByPrimaryKey(retryErrorLogs);
    }
}
