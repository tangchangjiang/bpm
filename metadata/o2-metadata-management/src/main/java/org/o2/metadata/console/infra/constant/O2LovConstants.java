package org.o2.metadata.console.infra.constant;

/**
 * 组件枚举、使用全局变量
 *
 * @author youlong.peng 2021年08月23日
 */
public interface O2LovConstants {

    interface RequestParam {
        String POST = "POST";

        String URL_PREFIX = "http://";
        String LOV_CODE = "lovCode";
        String ORGANIZATIONID = "organizationId";

    }

    interface LovTypeCode {
        String SQL = "SQL";
        String URL = "URL";
    }

    interface RegionLov {
        /**
         * 查询条件
         */
        String REGION_CODES = "regionCodes";
        /**
         * 值集编码
         */
        String REGION_LOV_CODE = "O2MD.REGION";
        String LEVEL_NUMBER = "levelNumber";
        String REGION_NAME = "regionName";
        String ADDRESS_TYPE = "addressType";
        String REGION_CODE = "regionCode";
        String NOT_IN_REGION_CODE = "notInRegionCodes";
        String PARENT_REGION_CODES = "parentRegionCodes";
        String COUNTRY_CODE = "countryCode";
        String DEFAULT_COUNTRY_CODE = "CN";
        String TENANT_ID = "tenantId";
        String LANG = "lang";
        String DEFAULT_LANG = "zh_CN";
        String DEFAULT_CODE = "-1";
        String DEFAULT_DATA = "region";

    }

    interface IamUserLov {
        String IAM_USER_LOV_CODE = "O2MD.IAM_USER";
        String TENANT_ID = "tenantId";
        String REAL_NAME = "realName";
        String idList = "idList";

    }

    /**
     * Exception ErrorCode
     */
    interface ErrorCode {
        String BASIC_DATA_LOV_PERMISSION_REFUSE = "error.pub.lov.permission.refuse";
    }

    /**
     * 货币
     */
    interface Currency {
        String CODE = "O2MD.CURRENCY";

        /**
         * 参数currencyCode
         */
        String PARAM_CURRENCY_CODE = "currencyCode";
        /**
         * 返回值currencyCode
         */
        String CURRENCY_CODE = PARAM_CURRENCY_CODE;
        /**
         * 返回值currencyName
         */
        String CURRENCY_NAME = "currencyName";
        String COUNTRY_NAME = "countryName";
        String COUNTRY_CODE = "countryCode";
        String CURRENCY_SYMBOL = "currencySymbol";
    }

    interface Uom {
        String CODE = "O2MD.UOM";

        /**
         * 参数uomCode
         */
        String PARAM_UOM_CODE = "uomCode";

        /**
         * 返回值uomCode
         */
        String UOM_CODE = PARAM_UOM_CODE;
        /**
         * 返回值uomName
         */
        String UOM_NAME = "uomName";
    }

    interface UomType {
        String CODE = "O2MD.UOM_TYPE";

        /**
         * 参数uomTypeCode
         */
        String PARAM_UOM_TYPE_CODE = "uomTypeCode";

        /**
         * 返回值uomTpeCode
         */
        String UOM_TYPE_CODE = PARAM_UOM_TYPE_CODE;
        /**
         * 返回值uomTypeName
         */
        String UOM_TYPE_NAME = "uomTypeName";
    }

    /**
     * Address Type
     */
    interface AddressType {
        // 国家
        String COUNTRY = "country";
        // 国家下所有数据
        String COUNTRY_DETAIL = "countryDetail";
        // 地区
        String REGION = "region";

        String CODE = "O2MD.REGION";
    }

    /**
     * 地区信息
     */
    interface RegionInfo {
        /**
         * addressType segment
         */
        String ADDRESS_TYPE = "addressType";
        /**
         * 地区
         */
        String LOV_CODE = "O2MD.REGION";
        /**
         * 地区
         */
        String REGION_CODE = "regionCode";
        /**
         * 参数地区code
         */
        String PARAM_REGION_CODES = "regionCodes";
        /**
         * 国家code
         */
        String COUNTRY_CODE = "countryCode";
        /**
         * 国家名称
         */
        String COUNTRY_NAME = "countryName";
        /**
         * 地区名称
         */
        String REGION_NAME = "regionName";
    }
}

