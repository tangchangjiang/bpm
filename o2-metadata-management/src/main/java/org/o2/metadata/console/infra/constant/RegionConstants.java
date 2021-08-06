package org.o2.metadata.console.infra.constant;

/**
 *
 * 服务点常量
 *
 * @author yipeng.zhu@hand-china.com 2021-08-03
 **/
public enum RegionConstants {
    //默认
    DEFUTLT;

    public enum RegionLov {
        /**
         * 查询条件
         */
        REGION_CODES("regionCodes", ""),
        /**
         * 值集编码
         */
        REGION_LOV_CODE("O2MD.REGION", ""),
        COUNTRY_LOV_CODE("O2MD.COUNTRY", ""),
        LEVEL_NUMBER("levelNumber", ""),
        REGION_NAME("regionName",""),
        REGION_CODE("regionCode",""),
        REGION_IDS("regionIds",""),
        PARENT_REGION_ID("parentRegionId",""),
        PARENT_REGION_CODE("parentRegionCode",""),
        NOT_IN_REGION_CODE("notInRegionCodes",""),
        PARENT_REGION_IDS("parentRegionIds",""),
        ENABLED_FLAG("enabledFlag",""),
        COUNTRY_CODE("countryCode",""),
        TENANT_ID("tenantId",""),
        LANG("lang","");


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
