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

    interface ErrorCode {
        String O2MD_ERROR_CARRIER_EXISTS = "o2md.error.carrier.exists";
    }

    interface Redis {
        /**
         * redis key(hash): o2md:carrier:{tenantId}
         */
        String CARRIER_KEY = "o2md:carrier:%s:";
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
