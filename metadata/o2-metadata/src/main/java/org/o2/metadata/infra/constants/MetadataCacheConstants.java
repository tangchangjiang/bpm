package org.o2.metadata.infra.constants;

/**
 * Description
 *
 * @author yipeng.zhu@hand-china.com 2022/8/30
 */
public interface MetadataCacheConstants {
    /**
     * 缓存名称
     */
    interface CacheName {
        String ACROSS = "O2MD_ACROSS";

        String O2MD_METADATA = "O2MD_METADATA";

        String O2_LOV = "O2_LOV";
    }

    /**
     * CacheKey
     */
    interface CacheKey {
        String FETCH_USER_ROLE_BY_CODE = "fetchRoleInner_%s";

        /**
         * 获取角色
         * @param tenantId tenantId
         * @return keyPrefix
         */
        static String getFetchRolePrefix(Long tenantId) {
            return String.format(FETCH_USER_ROLE_BY_CODE, tenantId);
        }

        String FREIGHT_PREFIX = "freight_%s";

        /**
         * 获取查询运费缓存的keyPrefix
         *
         * @param freight 查询参数
         * @return keyPrefix
         */
        static String getFreightPrefix(String freight) {
            return String.format(FREIGHT_PREFIX, freight);
        }

        String CARRIER_PREFIX = "carrier_%s";

        /**
         * 获取承运商查询缓存的keyPrefix
         *
         * @param tenantId 租户Id
         * @return keyPrefix
         */
        static String getCarrierPrefix(Long tenantId) {
            return String.format(CARRIER_PREFIX, tenantId);
        }

        String ONLINE_SHOP_PREFIX = "onlineShop_%s_%s";

        /**
         * 获取网店查询缓存的keyPrefix
         *
         * @param tenantId       租户Id
         * @param onlineShopType 网店类型
         * @return keyPrefix
         */
        static String getOnlineShopPrefix(Long tenantId, String onlineShopType) {
            return String.format(ONLINE_SHOP_PREFIX, tenantId, onlineShopType);
        }

        String SYS_PARAM_BY_CODE_PREFIX = "systemParameter_%s_%s";

        /**
         * 获取查询系统参数缓存的keyPrefix
         *
         * @param tenantId  租户Id
         * @param paramCode 参数code
         * @return 系统参数
         */
        static String getSysParamByCodePrefix(Long tenantId, String paramCode) {
            return String.format(SYS_PARAM_BY_CODE_PREFIX, tenantId, paramCode);
        }

        String REGION_PREFIX = "region_%s_%s";

        /**
         * 获取查询地区值集缓存的keyPrefix
         *
         * @param countryCode 城市编码
         * @param lang        语言
         * @return keyPrefix
         */
        static String getRegionPrefix(String countryCode, String lang) {
            return String.format(REGION_PREFIX, countryCode, lang);
        }

        String BASE_LOV_PREFIX = "baseLov_%s";

        /**
         * 获取查询基本单位值集缓存的keyPrefix
         *
         * @param lovCode 值集编码
         * @return keyPrefix
         */
        static String getBaseLovPrefix(String lovCode) {
            return String.format(BASE_LOV_PREFIX, lovCode);
        }
    }
}
