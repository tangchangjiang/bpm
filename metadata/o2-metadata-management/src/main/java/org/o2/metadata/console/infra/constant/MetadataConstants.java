package org.o2.metadata.console.infra.constant;

import com.google.common.collect.ImmutableList;
import org.springframework.core.io.ClassPathResource;
import org.springframework.scripting.support.ResourceScriptSource;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;

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
        String MODE_NAME = "METADATA";
    }

    interface ErrorCode {
        String BASIC_DATA_ENTITY_CANNOT_UPDATE = "error.basic_data.entity_cannot_update";
        String BASIC_DATA_DUPLICATE_CODE = "error.basic_data.duplicate_entity_code";
        String BASIC_DATA_DUPLICATE_NAME = "error.basic_data.duplicate_entity_name";
        String BASIC_DATA_DUPLICATE_U_INDEX = "error.basic_data.duplicate_u_index";
        String BASIC_DATA_DATE_RANGE_ERROR = "error.basic_data.start_is_later_than_end";
        String BASIC_DATA_ONLINE_AND_WAREHOUSE_CODE_IS_NULL = "error.basic_data.online_and_warehouse_code_is_null";
        String BASIC_DATA_PARENT_NOT_ENABLED = "error.basic_data.parent_disable";

        String BASIC_DATA_TENANT_ID_IS_NULL = "error.basic_data.tenantId.should.is.not.null";
        String BASIC_DATA_CATALOG_CODE_IS_NULL = "error.basic_data.catalogCode.should.is.not.null";
        String BASIC_DATA_PLATFORM_CODE_IS_NULL = "error.basic_data.platformCode.should.is.not.null";
        String BASIC_DATA_LOV_PERMISSION_NOT_PASS = "error.basic_data.lov.permission.notPass";
        String O2MD_ERROR_CHECK_FAILED = "o2md.error.check.failed";
        String O2MD_ERROR_CHECK_ERROR = "o2md.error.check.error";
        String STATIC_FILE_UPLOAD_FAIL = "o2md.error.static_file_upload_fail";

        String O2MD_RESOURCE_OWNER_IS_NULL = "o2md.resource_owner.should.is.not.null";

        /**
         * 静态资源配置查询失败
         */
        String QUERY_STATIC_RESOURCE_CONFIG_FAIL = "o2md.query_static_resource_config_fail";

        /**
         * 静态资源保存失败
         */
        String SAVE_STATIC_RESOURCE_FAIL = "o2md.save_static_resource_fail";

        /**
         * 初始化策略加载错误
         */
        String BUSINESS_STRATEGY_INIT_WRONG = "o2md.error.strategy.init.wrong";

        /**
         * 没有符合的策略处理实现类
         */
        String BUSINESS_SERVICE_STRATEGY_FAILED = "o2md.error.service_handle_failed";
    }

    interface MappingCacheName {
        String METADATA_CACHE_NAME = "O2_METADATA";
    }

    /**
     * 公共值集
     */
    interface PublicLov {
        String PUB_LOV_CODE = "O2MD.PUBLIC_LOV";
        String JSON_TYPE = "application/json";
        String STATIC_RESOURCE_LOV_CODE = "O2MD.RESOURCE_LEVEL";
        String DIFFERENT_LANG_FLAG = "O2MD.FLAG";
        String SOURCE_MODULE_CODE = "O2MD.DOMAIN_MODULE";
    }

    /**
     * 地区静态文件同步
     */
    interface O2SiteRegionFile {
        String JSON_TYPE = "application/json";
        String ZH_CN = "zh_CN";
        String EN_US = "en_US";
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
     * 元数据缓存job参数
     */
    interface CacheJob {
        String DEFAULT_ACTION = "SKIP";

        String REFRESH = "REFRESH";

        String CACHE_WAREHOUSE = "Warehouse";

        String CACHE_ONLINE_SHOP_REL_WAREHOUSE = "OnlineShopRelWarehouse";

        String CACHE_SYS_PARAMETER = "SysParameter";
        String TENANT_ID = "tenantId";
        String DEFAULT_TENANT_ID = "0";
        String CARRIER = "Carrier";
        String FREIGHT = "Freight";
        String ONLINE_SHOP = "OnlineShop";
        String POS = "Pos";
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

    interface DefaultShop {
        Integer DEFAULT = 1;
    }

    interface LuaCode {

        ResourceScriptSource BATCH_SAVE_WAREHOUSE_REDIS_HASH_VALUE_LUA =
                new ResourceScriptSource(new ClassPathResource("script/lua/batch_save_warehouse_redis_hash_value.lua"));

        ResourceScriptSource BATCH_SAVE_REDIS_HASH_VALUE_LUA =
                new ResourceScriptSource(new ClassPathResource("script/lua/batch_save_redis_hash_value.lua"));

        ResourceScriptSource BATCH_DELETE_SHOP_REL_WH_REDIS_HASH_VALUE_LUA =
                new ResourceScriptSource(new ClassPathResource("script/lua/onlineShop/batch_delete_shopRelWh_redis.lua"));
    }

    interface Path {
        String FILE = "file";

        String REGION = "region";

        String ZH_CN = "zh_CN";

        String EN_US = "en_US";

        String FILE_NAME = "country-region";

        String LOV = "lov";

        String LOV_FILE_NAME = "o2-public-lov";

    }

    interface FileSuffix {
        String JSON = ".json";
    }

    interface Status {
        String UPDATE = "update";
        String CREATE = "create";
    }

    interface ActiveFlag {
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
         *
         * @return DateFormat
         */
        static DateFormat dateFormat() {
            return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        }
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
    interface CodeRuleBuilder {
        String RULE_CODE = "O2MD.WAREHOUSE";
        String LEVEL_CODE = "GLOBAL";
        String LEVEL_VALUE = "GLOBAL";
    }

    /**
     * 静态资源文件编码
     */
    interface StaticResourceCode {
        /**
         * 元数据 - 地区数据文件
         */
        String O2MD_REGION = "O2MD_REGION";
        /**
         * 元数据 - external地区数据文件
         */
        String O2MD_REGION_EXTERNAL = "O2MD_REGION_EXTERNAL";

        /**
         * 元数据 - 供C端使用的Lov值集数据文件
         */
        String O2MD_PUB_LOV = "O2MD_PUB_LOV";

        String O2MD_IDP_LOV = "O2MD_IDP_LOV";

        /**
         * 元数据 - 地区数据文件描述
         */
        String O2MD_REGION_DESCRIPTION = "省市区数据文件";

        /**
         * 元数据 - 值集数据文件
         */
        String LOV_DESCRIPTION = "值集数据文件";

        /**
         * 元数据 - 商城端公共的Lov值集
         */
        String O2MD_PUBLIC_LOV = "O2MD_PUB_LOV";

        static String buildMetadataRegionCode() {
            return String.format("%s", O2MD_REGION);
        }

    }

    interface StaticResourceConstants {
        Integer ENABLE_FLAG = 1;

        String LEVEL_PUBLIC = "PUBLIC";

        String LEVEL_SITE = "SITE";

        Integer CONFIG_DIFFERENT_LANG_FLAG = 1;
    }

    interface RefreshJobConstants {
        String TENANT_ID = "tenantId";

        String COUNTRY_CODE = "countryCode";

        String LOV_CODE = "lovCode";

        String IDP_LOV_OWNER = "idpLovOwner";

        String REGION_OWNER = "regionOwner";

        String BUSINESS_TYPE_CODE = "businessTypeCode";
    }

    interface Lang {
        String ZH_CN = "zh_CN";
        String EN_US = "en_US";
        ImmutableList<String> ALL_LANGUAGE = ImmutableList.of(ZH_CN, EN_US);
    }

}
