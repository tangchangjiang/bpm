package org.o2.metadata.console.infra.constant;

import org.o2.data.redis.helper.ScriptHelper;
import org.springframework.data.redis.core.script.RedisScript;

/**
 * 系统参数常量
 *
 * @author yipeng.zhu@hand-china.com 2021-07-13
 **/
public interface SystemParameterConstants {

    interface ErrorCode {

        String BASIC_DATA_MAP_KEY_IS_NULL = "error.basic_data.system_param_value.key.not_null";
        String ERROR_SYSTEM_PARAM_CODE_UNIQUE = "o2md.error.system_param_code.not.unique";

        /**
         * 系统参数不存在
         */
        String ERROR_SYSTEM_PARAM_NOT_EXIST = "o2md.error.system_param_not_exist";
        /**
         * 只可以复制预定义的系统参数
         */
        String ERROR_SYSTEM_PARAM_ONLY_COPY_PREDEFINE = "o2md.error.system_param_only_copy_predefine";

        /**
         * 系统参数已存在，请勿重复复制
         */
        String ERROR_SYSTEM_PARAM_ALREADY_EXISTS = "o2md.error.system_param_already_exists";

        /**
         * 没有选择系统参数
         */
        String ERROR_SYSTEM_PARAM_NO_SELECT = "o2md.error.system_param_no_select";

        /**
         * 没有选择租户
         */
        String ERROR_TENANT_NO_SELECT = "o2md.error.tenant_no_select";
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

    RedisScript<Boolean> INIT_DATA_REDIS_HASH_VALUE_LUA =
            ScriptHelper.of("script/lua/systemParameter/init_data_redis_hash_value.lua", Boolean.class);
}
