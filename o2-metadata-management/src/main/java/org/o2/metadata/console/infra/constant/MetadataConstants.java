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
