package org.o2.metadata.core.infra.constants;

import org.springframework.core.io.ClassPathResource;
import org.springframework.scripting.support.ResourceScriptSource;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.HashSet;

/**
 * 元数据常量
 *
 * @author mark.bao@hand-china.com 2019-04-16
 */
public interface MetadataConstants {

    /**
     * 统一时间格式
     */
    interface MdDateFormat {
        /**
         * redis时间格式
         *
         * @return DateFormat
         */
        DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        static DateFormat dateFormat() {
            return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        }
    }
    /**
     * 提示信息
     */
    interface Message {

        /**
         * 系统参数编码不可重复，成功保存N条数据
         */
        String SYSTEM_PARAMETER_SUCCESS_NUM = "system_parameter.success_num";
        /**
         * 没有找到任何系统参数数据
         */
        String SYSTEM_PARAMETER_NOT_FOUND = "system_parameter.not_found";

    }



    /**
     * Redis OnlineShopRelWarehouse
     * o2md:shopRelwh:[tenantId]:[shopCode]
     * key:value [warehouseCode:businessActiveFlag]
     * 对应数据库表：o2md_online_shop_rel_warehouse
     */
    interface OnlineShopRelWarehouse {
        String KEY_ONLINE_SHOP_REL_WAREHOUSE = "o2md:shopRelwh:%d:{%s}";
        String FIELD_WAREHOUSE_CODE = "warehouseCode";
        Collection<String> HASH_KEYS = new HashSet<String>() {{
            add(FIELD_WAREHOUSE_CODE);
        }};
    }

    /**
     * Redis Warehouse
     * o2md:warehouse:[tenantId]:[warehouseCode]
     * 对应数据库表：o2md_warehouse
     */
    interface WarehouseCache {
        String WAREHOUSE_INFO_KEY = "o2md:warehouse:%d:{%s}";
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

        /***
         * 运费模板默认运费行KEY
         */
        String FREIGHT_DEFAULT_LINE_KEY = "DEFAULT";
        /***
         * 默认运费模板KEY
         */
        String FREIGHT_DEFAULT_KEY = "DEFAULT";
        /***
         *运费模板头KEY
         */
        String FREIGHT_HEAD_KEY = "HEAD";


        /**
         * 运费模板明细redis key(hash): o2md:freight::{tenantId}:{freightCode} ; 注:默认的运费模板{freightCode} 为DEFAULT
         *
         * key:
         *     HEAD ~ 运费模板头信息
         *     {region} - 地区信息对应模板
         *     DEFAULT  - 默认运费模板行
         */
        String FREIGHT_DETAIL_KEY = "o2om:freight:%s:{%s}";


        ResourceScriptSource SAVE_FREIGHT_DETAIL_CACHE_LUA =
                new ResourceScriptSource(new ClassPathResource("script/lua/freight/save_freight_detail_cache.lua"));

        ResourceScriptSource DELETE_FREIGHT_DETAIL_CACHE_LUA =
                new ResourceScriptSource(new ClassPathResource("script/lua/freight/delete_freight_detail_cache.lua"));
    }

    /**
     * Redis Hash 系统参数
     *
     * o2ext:parameter:[tenantId]:[parameterType]
     */
    interface SystemParameter {
        String KEY = "o2md:parameter:%d:{%s}";
    }



    /**
     * 系统参数类型
     */
    interface ParamType {
        /**
         * key-value
         */
        String KV = "KV";
        /**
         * 重复
         */
        String LIST = "LIST";
        /**
         * 不重复
         */
        String SET = "SET";
        /**
         * 值集编码
         */
        String LOV_CODE = "O2EXT.PARAM_TYPE";
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

        String LOV_CODE = "O2MD.TERMINAL_TYPE";
    }


    interface PosCacheCode {
        String CACHE_SERVICE_NAME_POS = "pos";
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