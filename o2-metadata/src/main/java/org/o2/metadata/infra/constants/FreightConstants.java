package org.o2.metadata.infra.constants;

/**
 *
 *  运费常量
 *
 * @author yipeng.zhu@hand-china.com 2021-07-14
 **/
public interface FreightConstants {
    interface RedisKey {
        /**
         * platformSku关联运费模板hash key: o2om:freight:skurel:${tenantId} <br />
         * key: platformSkuCode <br />
         * value: freightCode <br />
         */
        String SKU_REL_FREIGHT = "o2om:freight:skurel:{%s}";
        /**
         * GetSkuRelFreight
         *
         * @param tenantId
         * @return
         */
        static String getSkuRelFreightKey(Long tenantId) {
            return String.format(SKU_REL_FREIGHT, tenantId);
        }
    }
}
