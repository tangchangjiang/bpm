package org.o2.feignclient.metadata.infra.constants;

/**
 *
 * 元数据常量
 *
 * @author yipeng.zhu@hand-china.com 2021-07-13
 **/
public class MetadataConstants {
    /**
     * 系统参数
     */
    interface SystemParameter {
        /**
         * 仓库上传比例
         */
        String DEFAULT_WH_UPLOAD_RATIO = "DEFAULT_WH_UPLOAD_RATIO";
        /**
         *  仓库安全库存
         */
        String DEFAULT_WH_SAFETY_STOCK = "DEFAULT_WH_SAFETY_STOCK";
        /**
         * 网店上传比例
         */
        String DEFAULT_SHOP_UPLOAD_RATIO = "DEFAULT_SHOP_UPLOAD_RATIO";
        /**
         * 网店安全库存
         */
        String DEFAULT_SHOP_SAFETY_STOCK = "DEFAULT_SHOP_SAFETY_STOCK";
    }

    /**
     * 仓库
     */
    interface Warehouse {
        String FIELD_POS_CODE = "posCode";
        String FIELD_WAREHOUSE_STATUS_CODE = "warehouseStatusCode";
        String FIELD_WAREHOUSE_TYPE_CODE = "warehouseTypeCode";
        String FIELD_PICKED_UP_FLAG = "pickedUpFlag";
        String FIELD_EXPRESSED_FLAG = "expressedFlag";
        String FIELD_SCORE = "score";
        String FIELD_ACTIVED_DATE_FROM = "activedDateFrom";
        String FIELD_ACTIVED_DATE_TO = "activedDateTo";
        String FIELD_INV_ORGANIZATION_CODE = "invOrganizationCode";
        String FIELD_EXPRESS_LIMIT_QUANTITY = "expressLimitQuantity";
        String FIELD_EXPRESS_LIMIT_VALUE = "expressLimitValue";
        String FIELD_PICK_UP_LIMIT_QUANTITY = "pickUpLimitQuantity";
        String FIELD_PICK_UP_LIMIT_VALUE = "pickUpLimitValue";
    }
}
