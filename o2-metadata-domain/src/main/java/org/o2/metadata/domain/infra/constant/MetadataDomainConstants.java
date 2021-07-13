package org.o2.metadata.domain.infra.constant;

/**
 * 基础数据常量
 *
 * @author tingting.wang@hand-china.com 2019-3-25
 */
public interface MetadataDomainConstants {
    /**
     * 服务点类型
     */
    interface PosType {
        /**
         * 仓库
         */
        String WAREHOUSE = "WAREHOUSE";
        /**
         * 门店
         */
        String STORE = "STORE";

        String LOV_CODE = "O2MD.POS_TYPE";
    }

}
