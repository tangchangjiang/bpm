package org.o2.metadata.console.infra.constant;

/**
 *
 * 服务点常量
 *
 * @author yipeng.zhu@hand-china.com 2021-08-03
 **/
public enum PosConstants {
    //默认
    DEFUTLT;

    public enum RegionLov {
        /**
         * 查询条件
         */
        QUERY_PARAM("regionCodes", ""),
        /**
         * 值集编码
         */
        LOV_CODE("O2MD.REGION", "");

        private String code;

        private String value;

        public String getCode() {
            return code;
        }

        public String getValue() {
            return value;
        }

        RegionLov(String code, String value) {
            this.code = code;
            this.value = value;
        }
    }
}
