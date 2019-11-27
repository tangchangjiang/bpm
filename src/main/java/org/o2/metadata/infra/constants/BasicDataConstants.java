package org.o2.metadata.infra.constants;

/**
 * 基础数据常量
 *
 * @author tingting.wang@hand-china.com 2019-3-25
 */
public interface BasicDataConstants {

    interface Constants {
        String COUNTRY_ALL = "$ALL$";
        String ADDRESS_SPLIT = ".";
        String ADDRESS_SPLIT_REGEX = "\\.";
    }

    interface ErrorCode {
        String BASIC_DATA_ENTITY_CANNOT_UPDATE = "error.basic_data.entity_cannot_update";
        String BASIC_DATA_DUPLICATE_CODE = "error.basic_data.duplicate_entity_code";
        String BASIC_DATA_DUPLICATE_NAME = "error.basic_data.duplicate_entity_name";
        String BASIC_DATA_DATE_RANGE_ERROR = "error.basic_data.start_is_later_than_end";
        String BASIC_DATA_ONLINE_AND_POS_CODE_IS_NULL = "error.basic_data.online_and_pos_code_is_null";
        String BASIC_DATA_PARENT_NOT_ENABLED = "error.basic_data.parent_disable";

        String BASIC_DATA_FREIGHT_ID_IS_NULL = "error.basic_data.freight_template.id.not_null";
        String BASIC_DATA_FREIGHT_CODE_IS_NULL = "error.basic_data.freight_template.code.not_null";
        String BASIC_DATA_FREIGHT_NAME_IS_NULL = "error.basic_data.freight_template.name.not_null";
        String BASIC_DATA_FREIGHT_DUPLICATE_CODE = "error.basic_data.freight_template.duplicate_code";
        String BASIC_DATA_FREIGHT_UNIQUE_DEFAULT = "error.basic_data.freight_template.unique_default";
        String BASIC_DATA_FREIGHT_CAN_NOT_REMOVE = "error.basic_data.freight_template.can_not_remove";
        String BASIC_DATA_FREIGHT_DETAIL_ID_IS_NULL = "error.basic_data.freight_template_detail.id.not_null";
        String BASIC_DATA_FREIGHT_DETAIL_CARRIER_IS_NULL = "error.basic_data.freight_template_detail.carrier_id.not_null";
        String BASIC_DATA_FREIGHT_DETAIL_REGION_IS_NULL = "error.basic_data.freight_template_detail.region_id.not_null";
        String BASIC_DATA_FREIGHT_DETAIL_FIRST_PIECE_WEIGHT_IS_NULL = "error.basic_data.freight_template_detail.first_piece_weight.not_null";
        String BASIC_DATA_FREIGHT_DETAIL_FIRST_PRICE_IS_NULL = "error.basic_data.freight_template_detail.first_price.not_null";
        String BASIC_DATA_FREIGHT_DETAIL_NEXT_PIECE_WEIGHT_IS_NULL = "error.basic_data.freight_template_detail.next_piece_weight.not_null";
        String BASIC_DATA_FREIGHT_DETAIL_NEXT_PRICE_IS_NULL = "error.basic_data.freight_template_detail.next_price.not_null";
        String BASIC_DATA_FREIGHT_DETAIL_TEMPLATE_ID_IS_NULL = "error.basic_data.freight_template_detail.template_id.not_null";
        String BASIC_DATA_FREIGHT_DETAIL_DUNPLICATE = "error.basic_data.freight_template_detail.duplicate";
    }

    /**
     * 营业类型
     */
    interface BusinessType {
        /**
         * 自营
         */
        String SELF_SALE_STORE = "SELF_SALE_STORE";
        /**
         * 加盟
         */
        String FRANCHISE_STORE = "FRANCHISE_STORE";

        String LOV_CODE = "O2MD.BUSINESS_TYPE";
    }

    /**
     * 承运商类型
     */
    interface CarrierType {
        /**
         * 物流
         */
        String LOGISTICS = "LOGISTICS";
        /**
         * 快递
         */
        String EXPRESS = "EXPRESS";

        String LOV_CODE = "O2MD.CARRIER_TYPE";
    }

    /**
     * 大区定义
     */
    interface AreaCode {
        /**
         * 华北
         */
        String HB = "HB";
        /**
         * 华东
         */
        String HD = "HD";

        String LOV_CODE = "O2MD.AREA_CODE";
    }

    /**
     * 计价方式
     */
    interface ValuationType {
        /**
         * 件
         */
        String NUM = "NUM";
        /**
         * 重量
         */
        String WEIGHT = "WEIGHT";
        /**
         * 体积
         */
        String VOLUME = "VOLUME";

        //        String LOV_CODE = "O2MD.VALUATION_TYPE";
        String LOV_CODE = "HPFM.UOM_TYPE";
    }

    /**
     * 单位
     */
    interface ValuationUom {
        /**
         * 件
         */
        String PIECE = "PIECE";
        /**
         * 千克
         */
        String KILOGRAM = "KG";
        /**
         * 立方米
         */
        String CUBIC_METER = "M3";

        //        String LOV_CODE = "O2MD.VALUATION_UOM";
        String LOV_CODE = "HPFM.UOM";
    }

}
