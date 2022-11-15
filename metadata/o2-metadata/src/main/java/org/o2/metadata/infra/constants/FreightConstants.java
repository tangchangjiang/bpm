package org.o2.metadata.infra.constants;

/**
 *
 *  运费常量
 *
 * @author yipeng.zhu@hand-china.com 2021-07-14
 **/
public interface FreightConstants {

    /**
     * Redis key
     */
    interface RedisKey {

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
        String FREIGHT_DETAIL_KEY = "o2md:freight:{%s}:%s";

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
