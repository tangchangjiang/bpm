package org.o2.metadata.console.job;

import org.hzero.boot.scheduler.infra.annotation.JobHandler;
import org.hzero.boot.scheduler.infra.enums.ReturnT;
import org.hzero.boot.scheduler.infra.handler.IJobHandler;
import org.hzero.boot.scheduler.infra.tool.SchedulerTool;
import org.o2.metadata.console.app.service.CacheJobService;
import org.o2.metadata.console.infra.constant.MetadataConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

import lombok.extern.slf4j.Slf4j;

/**
 * 缓存数据刷新
 * * 仓库 Warehouse
 * * 网店关联仓库 OnlineShopRelWarehouse
 * * 系统参数 SysParameter
 *
 * @author yuying.shi@hand-china.com 2020/3/24
 * @description 1、copy redis 的 expressLimitValue、pickUpLimitValue值，如果值不存在value = 0
 */
@Slf4j
@JobHandler(value = "mdRedisCacheRefreshJob")
public class MdRedisCacheRefreshJob implements IJobHandler {

    private static final Logger LOG = LoggerFactory.getLogger(MdRedisCacheRefreshJob.class);
    private CacheJobService cacheJobService;

    public MdRedisCacheRefreshJob(CacheJobService cacheJobService) {
        this.cacheJobService = cacheJobService;
    }

    @Override
    public ReturnT execute(Map<String, String> map, SchedulerTool tool) {
        if (map != null && !map.isEmpty()) {
            final Long tenantId = Long.valueOf(map.getOrDefault(MetadataConstants.CacheJob.TENANT_ID,MetadataConstants.CacheJob.DEFAULT_TENANT_ID));
            // 判断缓存操作
            final String warehouse = map.getOrDefault(MetadataConstants.CacheJob.CACHE_WAREHOUSE, MetadataConstants.CacheJob.DEFAULT_ACTION);
            final String onlineShopRelWarehouse = map.getOrDefault(MetadataConstants.CacheJob.CACHE_ONLINE_SHOP_REL_WAREHOUSE, MetadataConstants.CacheJob.DEFAULT_ACTION);
            final String sysParameter = map.getOrDefault(MetadataConstants.CacheJob.CACHE_SYS_PARAMETER, MetadataConstants.CacheJob.DEFAULT_ACTION);
            final String carrier = map.getOrDefault(MetadataConstants.CacheJob.CARRIER, MetadataConstants.CacheJob.DEFAULT_ACTION);
            final String freight = map.getOrDefault(MetadataConstants.CacheJob.FREIGHT,MetadataConstants.CacheJob.DEFAULT_ACTION);

            LOG.info("start synchronize metadata basic data to redis...");
            if (MetadataConstants.CacheJob.REFRESH.equals(warehouse)) {
                // 全量同步 仓库 数据到Redis，判断失效时间
                LOG.info("synchronize warehouse to redis cache complete.");
                cacheJobService.refreshWarehouse(tenantId);
            }

            if (MetadataConstants.CacheJob.REFRESH.equals(onlineShopRelWarehouse)) {
                // 全量同步 网店关联仓库 到Redis，判断生效标记 active_flag 和失效时间
                LOG.info("synchronize onlineShopRelWarehouse to redis cache complete.");
                cacheJobService.refreshOnlineShopRelWarehouse(tenantId);
            }

            if (MetadataConstants.CacheJob.REFRESH.equals(sysParameter)) {
                // 全量同步 系统参数 到Redis 判断 active_flag
                LOG.info("synchronize sysParameter to redis cache complete.");
                cacheJobService.refreshSysParameter(tenantId);
            }

            // 全量同步 承运商
            if (MetadataConstants.CacheJob.REFRESH.equals(carrier)) {
                cacheJobService.refreshCarrier(tenantId);
            }
            if (MetadataConstants.CacheJob.REFRESH.equals(freight)) {
                cacheJobService.refreshFreight(tenantId);
            }
        }
        return ReturnT.SUCCESS;
    }

}
