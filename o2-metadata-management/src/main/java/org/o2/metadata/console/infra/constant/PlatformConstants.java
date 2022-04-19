package org.o2.metadata.console.infra.constant;

/**
 *
 * 平台定义常量
 *
 * @author yipeng.zhu@hand-china.com 2021-11-11
 **/
public interface PlatformConstants {
    interface ErrorCode {
        String ERROR_PLATFORM_CODE_UNIQUE ="o2md.error.platform_code.is.not.unique";
        String ERROR_PLATFORM_NAME_UNIQUE ="o2md.error.platform_name.is.not.unique";
        String ERROR_PLATFORM_CODE_UPDATE ="o2md.error.platform_code.is.forbidden.update";
    }

    interface  PlatformMappingCacheName {
         String PLATFORM_CACHE_MAPPING_NAME = "O2_PLATFORM_MAPPING";
    }
    interface CacheKeyPrefix {
        String PLATFORM_CACHE_MAPPING_KEY_PREFIX = "PLATFORM_MAPPING_%S_%S";

    }
}
