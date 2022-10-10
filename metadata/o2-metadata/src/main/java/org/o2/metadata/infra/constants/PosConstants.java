package org.o2.metadata.infra.constants;

import org.springframework.core.io.ClassPathResource;
import org.springframework.scripting.support.ResourceScriptSource;

/**
 * 服务点常量
 *
 * @author chao.yang05@hand-china.com 2022/4/14
 */
public interface PosConstants {

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

    ResourceScriptSource SEARCH_POS_LIST_LUA =
            new ResourceScriptSource(new ClassPathResource("script/lua/pos/search_pos_list.lua"));
}