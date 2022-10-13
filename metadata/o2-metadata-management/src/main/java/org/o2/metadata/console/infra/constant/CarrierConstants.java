package org.o2.metadata.console.infra.constant;

import org.springframework.core.io.ClassPathResource;
import org.springframework.scripting.support.ResourceScriptSource;

/**
 *
 * 承运商
 *
 * @author yipeng.zhu@hand-china.com 2021-08-05
 **/
public interface CarrierConstants {

    /**
     * 承运商类型
     */
    interface CarrierType {
        /**
         * 物流
         */
        String LOGISTICS = "LOGISTICS";
        /**
         * 快递
         */
        String EXPRESS = "EXPRESS";

        String LOV_CODE = "O2MD.CARRIER_TYPE";
    }


    interface CarrierDeliveryRegionType {
        /**
         * 全国
         */
        String NATIONWIDE = "NATIONWIDE";
        /**
         * 自定义
         */
        String CUSTOM_REGION = "CUSTOM_REGION";
    }

    interface ErrorCode {
        String O2MD_ERROR_CARRIER_EXISTS = "o2md.error.carrier.exists";
        String O2MD_ERROR_PLATFORM_NOT_EXISTS = "o2md.error.platform.not.exist";
        String O2MD_ERROR_PLATFORM_CODE_DUPLICATE = "o2md.error.platform.code.duplicate";
        String ERROR_CARRIER_NAME_DUPLICATE = "o2md.error.carrier_name.duplicate";
        String ERROR_CARRIER_CODE_DUPLICATE = "o2md.error.carrier_code.duplicate";
        String ERROR_CARRIER_CODE_NOT_UPDATE = "o2md.error.carrier_code.forbidden.update";

        String ERROR_EXISTS_REGION_DATA = "o2md.error.carrier.exists_region_data";

        String ERROR_EXISTS_CITY_DATA = "o2md.error.carrier.exists_city_data";

        String ERROR_EXISTS_DISTRICT_DATA = "o2md.error.carrier.exists_district_data";

        String ERROR_EXISTS_PRIORITY_DATA = "o2md.error.carrier.exists_priority_data";
    }

    interface Redis {
        /**
         * redis key(hash): o2md:carrier:{tenantId}
         */
        String CARRIER_KEY = "o2md:carrier:{%s}:all";
        /**
         * GetFreightDefaultKey
         *
         * @param tenantId 租户ID
         * @return
         */
        static String getCarrierKey(Long tenantId) {
            return String.format(CARRIER_KEY, tenantId);
        }


        ResourceScriptSource CARRIER_CACHE_LUA =
                new ResourceScriptSource(new ClassPathResource("script/lua/carrier/batch_update_redis_hash_value.lua"));
    }
}
