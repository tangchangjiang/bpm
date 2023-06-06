package org.o2.metadata.console.infra.constant;

/**
 * 元数据缓存常量
 *
 * @author chao.yang05@hand-china.com 2022/11/14
 */
public interface MetadataCacheConstants {

    /**
     * 缓存名称
     */
    interface CacheName {
        String O2_LOV = "O2_LOV";
    }

    interface KeyPrefix {
        String IDP_PREFIX = "idp_%s";

        /**
         * 获取查询独立值集缓存的keyPrefix
         *
         * @param tenantId 租户Id
         * @return keyPrefix
         */
        static String getIdpPrefix(Long tenantId) {
            return String.format(IDP_PREFIX, tenantId);
        }

        String GENERAL_PREFIX = "general_%s";

        /**
         * 获取查询通用值集缓存的keyPrefix
         *
         * @param cacheKey cacheKey
         * @return keyPrefix
         */
        static String getGeneralPrefix(String cacheKey) {
            return String.format(GENERAL_PREFIX, cacheKey);
        }

        String HZERO_REGION_PREFIX = "hzero_region_%s_%s";

        /**
         * 获取查询地区值集缓存的keyPrefix
         *
         * @param countryCode 城市编码
         * @param lang        语言
         * @return keyPrefix
         */
        static String getHzeroRegionPrefix(String countryCode, String lang) {
            return String.format(HZERO_REGION_PREFIX, countryCode, lang);
        }

        String BASE_LOV_PREFIX = "baseLov_%s_%s";

        /**
         * 获取查询基本单位值集缓存的keyPrefix
         *
         * @param lovCode 值集编码
         * @return keyPrefix
         */
        static String getBaseLovPrefix(String lovCode, String lang) {
            return String.format(BASE_LOV_PREFIX, lovCode, lang);
        }
    }
}
