package org.o2.metadata.console.app.service;

/**
 * @author tangcj
 * @version V1.0
 * @date 2022/6/8 10:50
 */
@Deprecated
public interface SourcingCacheUpdateService {

    /**
     * 更新元数据配置需要同步到寻源
     * @param tenantId
     * @param className
     */
    void refreshSourcingCache(Long tenantId, String className);


    /**
     * 更新临近省信息
     * @param tenantId
     * @param className
     */
    void refreshSourcingNearRegion(Long tenantId, String className);
}
