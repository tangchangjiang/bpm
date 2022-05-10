package org.o2.metadata.console.infra.constant;

/**
 *
 * 服务点常量
 *
 * @author yipeng.zhu@hand-china.com 2021-11-08
 **/
public interface PosConstants {

    interface ErrorCode {
        String ERROR_POS_NAME_DUPLICATE= "o2md.error.pos_name.duplicate";
        String ERROR_POS_CODE_DUPLICATE= "o2md.error.pos_code.duplicate";
        String ERROR_POS_CODE_NOT_UPDATE = "o2md.error.pos_code.forbidden.update";
    }

    interface RedisKey {
        /**
         * 所有门店信息索引
         * redis key(set): o2md:pos:store:index:{tenantId}:all
         */
        String POS_ALL_STORE_KEY = "o2md:pos:store:index:{%d}:all";

        /**
         * 市下的所有门店信息索引
         * redis key(set): o2md:pos:store:index:{tenantId}:regionCode:cityCode:all
         */
        String POS_CITY_STORE_KEY = "o2md:pos:store:index:{%d}:%s:%s:all";

        /**
         * 区下的所有门店信息索引
         * redis key(set): o2md:pos:store:index:{tenantId}:regionCode:cityCode:districtCode
         */
        String POS_DISTRICT_STORE_KEY = "o2md:pos:store:index:{%d}:%s:%s:%s";

        /**
         * 服务点信息详情
         * redis key(hash): o2md:pos:store:detail:{tenantId}
         */
        String POS_DETAIL_KEY = "o2md:pos:store:detail:{%d}";

        /**
         * 获取所有门店信息key
         * @param tenantId 租户Id
         * @return key
         */
        static String getPosAllStoreKey(Long tenantId) {
            return String.format(POS_ALL_STORE_KEY, tenantId);
        }

        /**
         * 获取市下所有门店key
         * @param tenantId 租户Id
         * @param regionCode 省code
         * @param cityCode 市code
         * @return key
         */
        static String getPosCityStoreKey(Long tenantId, String regionCode, String cityCode) {
            return String.format(POS_CITY_STORE_KEY, tenantId, regionCode, cityCode);
        }

        /**
         * 获取区下的所有key
         * @param tenantId 租户Id
         * @param regionCode 省code
         * @param cityCode 市code
         * @param districtCode 区code
         * @return key
         */
        static String getPosDistrictStoreKey(Long tenantId, String regionCode, String cityCode, String districtCode) {
            return String.format(POS_DISTRICT_STORE_KEY, tenantId, regionCode, cityCode, districtCode);
        }

        /**
         * 获取服务点详情key
         * @param tenantId 租户Id
         * @return key
         */
        static String getPosDetailKey(Long tenantId) {
            return String.format(POS_DETAIL_KEY, tenantId);
        }
    }

    /**
     * 服务点类型
     */
    interface PosTypeCode {
        /**
         * 仓库
         */
        String WAREHOUSE = "WAREHOUSE";
        /**
         * 门店
         */
        String STORE = "STORE";
    }
}
