package org.o2.metadata.infra.constants;

/**
 *
 * 系统参数常量
 *
 * @author yipeng.zhu@hand-china.com 2021-07-13
 **/
public interface SystemParameterConstants {

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
}