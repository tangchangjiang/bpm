package org.o2.metadata.console.infra.constant;

/**
 *
 * 系统参数常量
 *
 * @author yipeng.zhu@hand-china.com 2021-07-13
 **/
public interface SystemParameterConstants {
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
         * 值集编码
         */
        String LOV_CODE = "O2EXT.PARAM_TYPE";
    }

    /**
     * Redis Hash 系统参数
     *
     * o2ext:parameter:[tenantId]:[parameterType]
     */
    interface SystemParameter {
        String KEY = "o2md:parameter:%d:{%s}";
    }
    interface Parameter {
       String DEFAULT_WH_UPLOAD_RATIO = "DEFAULT_WH_UPLOAD_RATIO";
       String DEFAULT_WH_SAFETY_STOCK = "DEFAULT_WH_SAFETY_STOCK";
       String DEFAULT_SHOP_UPLOAD_RATIO = "DEFAULT_SHOP_UPLOAD_RATIO";
       String DEFAULT_SHOP_SAFETY_STOCK = "DEFAULT_SHOP_SAFETY_STOCK";
    }
}
