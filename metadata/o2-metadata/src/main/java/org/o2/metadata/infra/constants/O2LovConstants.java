package org.o2.metadata.infra.constants;

/**
 * 组件枚举、使用全局变量
 *
 * @author youlong.peng 2021年08月23日
 */
public interface O2LovConstants {

    interface RegionLov {
        /**
         * 查询条件
         */
        String REGION_CODES = "regionCodes";
        /**
         * 值集编码
         */
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
        String REGION_CODE_LIST = "regionCodeList";

    }

    /**
     * 独立值集
     */
    interface IdpLov {
        /**
         * 缓存名
         */
        String CACHE_NAME = "O2_LOV";
        /**
         * 缓存值 key 前缀
         */
        String KEY_PREFIX = "idp_";
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
        String COUNTRY_CODE = "countryCode";
        String CURRENCY_SYMBOL = "currencySymbol";
        String COUNTRY_NAME = "countryName";
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

    interface RoleLov {
        String ROLE_SQL_LOV = "O2MD.IAM_ROLE";
        String ROLE_SQL_PARAM = "roleCodes";
        String ROLE_SQL_PARAM_TENANT_ID = "tenantId";
    }
}

