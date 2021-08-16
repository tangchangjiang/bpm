package org.o2.metadata.console.infra.constant;

import org.springframework.core.io.ClassPathResource;
import org.springframework.scripting.support.ResourceScriptSource;

/**
 *
 *  运费常量
 *
 * @author yipeng.zhu@hand-china.com 2021-07-14
 **/
public interface FreightConstants {

    interface ErrorCode {
        String BASIC_DATA_FREIGHT_ID_IS_NULL = "error.basic_data.freight_template.id.not_null";
        String BASIC_DATA_FREIGHT_CODE_IS_NULL = "error.basic_data.freight_template.code.not_null";
        String BASIC_DATA_FREIGHT_NAME_IS_NULL = "error.basic_data.freight_template.name.not_null";
        String BASIC_DATA_FREIGHT_NOT_EXISTS = "error.basic_data.freight_template.name.not_null";
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
    interface Redis {

        /**
         * 默认运费模板KEY
         */
        String FREIGHT_DEFAULT_KEY = "DEFAULT";

        /**
         * 运费模板头KEY
         */
        String FREIGHT_HEAD_KEY = "HEAD";

        /**
         * 运费模板明细hash key: o2md:freight:{tenantId}:{freightCode} <br />
         * key: HEAD/region <br />
         * value: freight detail json <br />
         */
        String FREIGHT_DETAIL_KEY = "o2md:freight:%s:{%s}";

        /**
         * GetFreightDefaultKey
         *
         * @param tenantId
         * @param freightCode
         * @return
         */
        static String getFreightDetailKey(Long tenantId, String freightCode) {
            return String.format(FREIGHT_DETAIL_KEY, tenantId, freightCode);
        }
        ResourceScriptSource SAVE_FREIGHT_DETAIL_CACHE_LUA =
                new ResourceScriptSource(new ClassPathResource("script/lua/freight/save_freight_detail_cache.lua"));

        ResourceScriptSource DELETE_FREIGHT_DETAIL_CACHE_LUA =
                new ResourceScriptSource(new ClassPathResource("script/lua/freight/delete_freight_detail_cache.lua"));

        ResourceScriptSource BATCH_UPDATE_FREIGHT_LUA =
                new ResourceScriptSource(new ClassPathResource("script/lua/freight/batch_update_freight.lua"));
    }

    /**
     * 运费模版类型
     */
    interface FreightType {
        /**
         * 计价方式
         */
        String LOV_VALUATION_TYPE = "O2MD.VALUATION_TYPE" ;
        /**
         * 计价单位
         */
        String LOV_VALUATION_UOM = "O2MD.VALUATION_UOM" ;
        /**
         * 运送方式
         */
        String LOV_TRANSPORT_TYPE = "O2MD.TRANSPORT_TYPE" ;
        /**
         *是否标识
         */
        String LOV_HPFM_FLAG = "HPFM.FLAG" ;


        /**
         * 计价方式
         */
        String LOV_VALUATION_TYPE_NEW = "O2MD.UOM_TYPE" ;

        /**
         * 计价单位
         */
        String LOV_VALUATION_UOM_NEW = "O2MD.UOM" ;

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

        String PIECE = "PIECE";

        String LOV_CODE = "HPFM.UOM_TYPE";
    }

    /**
     * 模版
     */
    interface Template {
        String TEMPLATE_HEAD = "HEAD";
        String TEMPLATE_DETAIL = "DETAIL";
    }
}
