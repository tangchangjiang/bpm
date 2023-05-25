package org.o2.metadata.console.infra.constant;

import org.springframework.core.io.ClassPathResource;
import org.springframework.scripting.support.ResourceScriptSource;

/**
 * 系统参数常量
 *
 * @author yipeng.zhu@hand-china.com 2021-07-13
 **/
public interface SystemParameterConstants {

    interface ErrorCode {

        String BASIC_DATA_MAP_KEY_IS_NULL = "error.basic_data.system_param_value.key.not_null";
        String ERROR_SYSTEM_PARAM_CODE_UNIQUE = "o2md.error.system_param_code.not.unique";
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
         * KEY VALUE
         */
        String MAP = "MAP";

        /**
         * 值集编码
         */
        String LOV_CODE = "O2MD.PARAM_TYPE";
    }

    /**
     * Redis Hash 系统参数
     * <p>
     * o2ext:parameter:[tenantId]:[parameterType]
     */
    interface Redis {
        String KEY = "o2md:parameter:{%d}:%s";
        String MAP_KEY = "o2md:parameter:{%d}:map:%s";

    }

    interface Parameter {
        String DEFAULT_WH_UPLOAD_RATIO = "DEFAULT_WH_UPLOAD_RATIO";
        String DEFAULT_WH_SAFETY_STOCK = "DEFAULT_WH_SAFETY_STOCK";
        String DEFAULT_SHOP_UPLOAD_RATIO = "DEFAULT_SHOP_UPLOAD_RATIO";
        String DEFAULT_SHOP_SAFETY_STOCK = "DEFAULT_SHOP_SAFETY_STOCK";
        /**
         * 默认站点
         */
        String DEFAULT_SITE_CODE = "DEFAULT_SITE_CODE";

        /**
         * 默认站点编码（门店类型）
         */
        String DEFAULT_SITE_CODE_STORE = "DEFAULT_SITE_CODE_STORE";
    }

    interface FileConfig {
        String FILE_SUFFIX_JSON = ".json";

        String FILE_JSON_TYPE = "application/json";
        /**
         * 上传路径
         */
        String FILE_PREFIX = "FILE_PREFIX";
    }

    ResourceScriptSource INIT_DATA_REDIS_HASH_VALUE_LUA =
            new ResourceScriptSource(new ClassPathResource("script/lua/systemParameter/init_data_redis_hash_value.lua"));
}
