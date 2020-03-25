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

    interface OnlineShopRelWarehouse {
        String KEY_ONLINE_SHOP_REL_WAREHOUSE = "o2md:shopRelwh:%d:%s:%s";
        String FIELD_POS_CODE = "posCode";
        String FIELD_WAREHOUSE_CODE = "warehouseCode";
        String FIELD_BUSINESS_ACTIVE_FLAG = "businessActiveFlag";
    }


    interface WarehouseCache {
        String WAREHOUSE_INFO_KEY = "o2md:warehouse:%d:%s";
        String EXPRESS_LIMIT_COLLECTION = "o2md:warehouse:express:%d:limit";
        String PICK_UP_LIMIT_COLLECTION = "o2md:warehouse:pick_up:%d:limit";
        String POS_CODE = "posCode";
        String WAREHOUSE_STATUS_CODE= "warehouseStatusCode";
        String WAREHOUSE_TYPE_CODE = "warehouseTypeCode";
        String PICKUP_FLAG = "pickedUpFlag";
        String EXPRESSED_FLAG = "expressedFlag";
        String SCORE = "score";
        String ACTIVE_DATE_FROM = "activedDateFrom";
        String ACTIVE_DATE_TO = "activedDateTo";
        String INV_ORGANIZATION_CODE = "invOrganizationCode";
        String EXPRESS_LIMIT_QUANTITY= "express_limit_quantity";
        String EXPRESS_LIMIT_VALUE = "express_limit_value";
        String PICK_UP_LIMIT_QUANTITY = "pick_up_limit_quantity";
        String PICK_UP_LIMIT_VALUE = "pick_up_limit_value";

        /**
         * 格式化的字符串
         *
         * @param tenantId      租户ID
         * @param warehouseCode 仓库编码
         * @return the return
         * @throws RuntimeException exception description
         */
        static String warehouseCacheKey(final long tenantId, final String warehouseCode) {
            return String.format(WAREHOUSE_INFO_KEY, tenantId, warehouseCode);
        }

        /**
         * 格式化的字符串
         * @param limit           limit
         * @param tenantId      租户ID
         * @return
         */
        static String warehouseLimitCacheKey(final String limit,final long tenantId) {
            return String.format(limit, tenantId);
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

    /**
     * 仓库类型
     */
    interface WarehouseType {
        String LOV_CODE = "O2MD.WAREHOUSE_TYPE";
    }

    /**
     * 仓库状态
     */
    interface WarehouseStatus {

        String LOV_CODE = "O2MD.WAREHOUSE_STATUS";
    }


    /**
     * 来源系统
     */
    interface SourceSystemCode {
        /**
         * O2
         */
        String O2 = "O2";
        String LOV_CODE = "O2MD.SOURCE_SYSTEM";
    }

    /**
     * 类型：PC/H5/WeChat/APP/TMALL/JD，值集O2VIP.CHANNEL_TYPE
     */
    interface ChannelTypeCode {

        /**
         * 电脑端
         */
        String PC = "PC";
        /**
         * 移动端H5
         */
        String H5 = "H5";
        /**
         * 微信
         */
        String WECHAT = "WeChat";
        /**
         * APP
         */
        String APP = "APP";
        /**
         * 天猫
         */
        String TMALL = "TMALL";
        /**
         * 京东
         */
        String JD = "JD";

        String LOV_CODE = "O2MD.CHANNEL_TYPE";
    }


    interface PosCacheCode {
        String CACHE_SERVICE_NAME_POS = "pos";
    }

    interface SysParameterCache {
        String SYS_PARAMETER_KEY = "o2md:sys_parameter:%s";
        String CACHE_SERVICE_NAME = "o2md";
        String CACHE_MODULE_NAME_SYS_PARAMETER = "sys_parameter";

        /**
         * 系统参数缓存 KEY
         *
         * @param sysParameterCode 系统参数编码
         * @param tenantId         租户Id
         * @return 系统参数缓存 KEY
         */
        static String sysParameterKey(String sysParameterCode, Long tenantId) {
            String tenantStr = null == tenantId ? null : tenantId.toString();
            return Joiner.on(":").skipNulls().join(CACHE_SERVICE_NAME, CACHE_MODULE_NAME_SYS_PARAMETER, tenantStr, sysParameterCode);
        }
    }

    /**
     * 编码规则
     */
    interface  CodeRuleBuilder {
        String RULE_CODE = "O2MD.WAREHOUSE";
        String LEVEL_CODE = "GLOBAL";
        String LEVEL_VALUE = "GLOBAL";
    }
}
