package org.o2.metadata.core.infra.constants;

import com.google.common.base.Joiner;
import org.springframework.core.io.ClassPathResource;
import org.springframework.scripting.support.ResourceScriptSource;

/**
 * 元数据常量
 *
 * @author mark.bao@hand-china.com 2019-04-16
 */
public interface MetadataConstants {

    interface PosCache {
        String POS_INFO_KEY = "o2md:pos:%s";
        String PICK_UP_LIMIT_QUANTITY = "pick_up_limit_quantity";
        String EXPRESS_LIMIT_QUANTITY = "express_limit_quantity";
        String PICK_UP_LIMIT_VALUE = "pick_up_limit_value";
        String EXPRESS_LIMIT_VALUE = "express_limit_value";
        String EXPRESS_LIMIT_COLLECTION = "o2md:pos:express:limit";
        String PICK_UP_LIMIT_COLLECTION = "o2md:pos:pick_up:limit";

        /**
         * 格式化的字符串
         *
         * @param posCode 服务点编码
         * @return the return
         * @throws RuntimeException exception description
         */
        static String posCacheKey(final String posCode) {
            return String.format(POS_INFO_KEY, posCode);
        }
    }

    interface FreightCache {
        /**
         * 运费模板redis key(string): o2md:ft:{freightCode}
         */
        String FREIGHT_KEY = "o2md:ft:%s";

        /**
         * 运费模板明细redis key(hash): o2md:ft:{freightCode}:detail
         */
        String FREIGHT_DETAIL_KEY = "o2md:ft:%s:detail";

        /**
         * 运费价格redis key(string): o2md:ft:{freightCode}:car:{carrierCode}:reg:{regionCode}
         */
        String FREIGHT_PRICE_KEY = "o2md:ft:%s:car:%s:reg:%s";

        ResourceScriptSource SAVE_FREIGHT_DETAIL_CACHE_LUA =
                new ResourceScriptSource(new ClassPathResource("script/lua/freight/save_freight_detail_cache.lua"));

        ResourceScriptSource DELETE_FREIGHT_DETAIL_CACHE_LUA =
                new ResourceScriptSource(new ClassPathResource("script/lua/freight/delete_freight_detail_cache.lua"));
    }

    /**
     * 服务点类型
     */
    interface PosType {
        /**
         * 仓库
         */
        String WAREHOUSE = "WAREHOUSE";
        /**
         * 门店
         */
        String STORE = "STORE";

        String LOV_CODE = "O2MD.POS_TYPE";
    }

    /**
     * 服务点状态
     */
    interface PosStatus {
        /**
         * 正常
         */
        String NORMAL = "NORMAL";
        /**
         * 暂停
         */
        String HOLD = "HOLD";
        /**
         * 关闭
         */
        String CLOSE = "CLOSE";

        String LOV_CODE = "O2MD.POS_STATUS";
    }



    interface PosCacheCode {
        String CACHE_SERVICE_NAME_POS = "pos";
    }
    interface SysParameterCache {
        String SYS_PARAMETER_KEY = "o2md:sys_parameter:%s";
        String CACHE_SERVICE_NAME = "o2md";
        String CACHE_MODULE_NAME_SYSPARAMETER = "sys_parameter";

        /**
         * 系统参数缓存 KEY
         *
         * @param sysParameterCode 系统参数编码
         * @param tenantId         租户Id
         * @return 系统参数缓存 KEY
         */
        static String sysParameterKey(String sysParameterCode, Long tenantId) {
            String tenantStr = null == tenantId ? null : tenantId.toString();
            return Joiner.on(":").skipNulls().join(CACHE_SERVICE_NAME, CACHE_MODULE_NAME_SYSPARAMETER,tenantStr,sysParameterCode);
        }
    }
}
