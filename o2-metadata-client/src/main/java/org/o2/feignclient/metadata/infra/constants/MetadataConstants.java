package org.o2.feignclient.metadata.infra.constants;

/**
 * 元数据常量
 *
 * @author mark.bao@hand-china.com 2019-04-16
 */
public interface MetadataConstants {
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
