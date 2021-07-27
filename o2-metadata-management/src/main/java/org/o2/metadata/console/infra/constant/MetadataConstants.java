package org.o2.metadata.console.infra.constant;

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

    interface Constants {
        String COUNTRY_ALL = "$ALL$";
        String ADDRESS_SPLIT = ".";
        String ADDRESS_SPLIT_REGEX = "\\.";
    }

    interface ErrorCode {
        String BASIC_DATA_ENTITY_CANNOT_UPDATE = "error.basic_data.entity_cannot_update";
        String BASIC_DATA_DUPLICATE_CODE = "error.basic_data.duplicate_entity_code";
        String BASIC_DATA_DUPLICATE_NAME = "error.basic_data.duplicate_entity_name";
        String BASIC_DATA_DUPLICATE_U_INDEX = "error.basic_data.duplicate_u_index";
        String BASIC_DATA_DATE_RANGE_ERROR = "error.basic_data.start_is_later_than_end";
        String BASIC_DATA_ONLINE_AND_WAREHOUSE_CODE_IS_NULL = "error.basic_data.online_and_warehouse_code_is_null";
        String BASIC_DATA_PARENT_NOT_ENABLED = "error.basic_data.parent_disable";


        String BASIC_DATA_TENANT_ID_IS_NULL =  "error.basic_data.tenantId.should.is.not.null";
        String BASIC_DATA_CATALOG_CODE_IS_NULL =  "error.basic_data.catalogCode.should.is.not.null";
        String BASIC_DATA_LOV_PERMISSION_NOT_PASS = "error.basic_data.lov.permission.notPass";
        String O2MD_ERROR_CARRIER_EXISTS = "o2md.error.carrier.exists";
        String O2MD_ERROR_CATALOG_FORBIDDEN = "o2md.error.catalog.forbidden";
    }

    /**
     * 营业类型
     */
    interface BusinessType {
        /**
         * 自营
         */
        String SELF_SALE_STORE = "SELF_SALE_STORE";
        /**
         * 加盟
         */
        String FRANCHISE_STORE = "FRANCHISE_STORE";

        String LOV_CODE = "O2MD.BUSINESS_TYPE";
    }



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

    /**
     * 大区定义
     */
    interface AreaCode {
        /**
         * 华北
         */
        String HB = "HB";
        /**
         * 华东
         */
        String HD = "HD";

        String LOV_CODE = "O2MD.AREA_CODE";
    }
    interface  DefaultShop {
        Integer DEFAULT = 1;
    }
    interface LuaCode {

        ResourceScriptSource BATCH_SAVE_WAREHOUSE_REDIS_HASH_VALUE_LUA =
                new ResourceScriptSource(new ClassPathResource("script/lua/batch_save_warehouse_redis_hash_value.lua"));

        ResourceScriptSource BATCH_SAVE_REDIS_HASH_VALUE_LUA =
                new ResourceScriptSource(new ClassPathResource("script/lua/batch_save_redis_hash_value.lua"));

        ResourceScriptSource BATCH_DELETE_SHOP_REL_WH_REDIS_HASH_VALUE_LUA =
                new ResourceScriptSource(new ClassPathResource("script/lua/batch_delete_shopRelWh_redis_hash_value.lua"));

        ResourceScriptSource BATCH_DELETE_REDIS_HASH_VALUE_LUA =
                new ResourceScriptSource(new ClassPathResource("script/lua/batch_delete_redis_hash_value.lua"));

        ResourceScriptSource BATCH_UPDATE_REDIS_HASH_VALUE_LUA =
                new ResourceScriptSource(new ClassPathResource("script/lua/batch_update_redis_hash_value.lua"));
    }

    interface  Path {
        String FILE = "file";

        String REGION ="region";

        String ZH_CN ="zh_CN";

        String EN_US ="en_US";

        String FILE_NAME = "country-region";

    }

    public interface FileSuffix {
        String JSON = ".json";
    }

    interface  Status {
        String UPDATE = "update";
        String CREATE = "create";
    }

    interface  ActiveFlag {
        Integer FORBIDDEN = 0;
        Integer ENABLE = 1;
    }
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

        /**
         * 时间格式
         * @return DateFormat
         */
        static DateFormat dateFormat() {
            return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
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
        String FREIGHT_DETAIL_KEY = "o2md:freight:%s:{%s}";


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
