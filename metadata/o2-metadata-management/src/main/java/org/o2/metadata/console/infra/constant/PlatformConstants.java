package org.o2.metadata.console.infra.constant;

/**
 * 平台定义常量
 *
 * @author yipeng.zhu@hand-china.com 2021-11-11
 **/
public interface PlatformConstants {
    interface ErrorCode {
        String ERROR_PLATFORM_CODE_UNIQUE = "o2md.error.platform_code.is.not.unique";
        String ERROR_PLATFORM_NAME_UNIQUE = "o2md.error.platform_name.is.not.unique";
        String ERROR_PLATFORM_CODE_UPDATE = "o2md.error.platform_code.is.forbidden.update";
    }

    interface CacheKeyPrefix {
        String PLATFORM_CACHE_MAPPING_KEY_PREFIX = "PLATFORM_MAPPING_%S_%S";

    }

    /**
     * 平台类型
     */
    interface PlatformType {
        /**
         * 电商平台
         */
        String E_COMMERCE_PLATFORM = "e_commerce_platform";
    }
}
