package org.o2.metadata.console.infra.constant;

/**
 *
 * 地址常量
 *
 * @author yipeng.zhu@hand-china.com 2021-10-19
 **/
public interface AddressConstants {
    /**
     * 地址匹配
     */
    interface AddressMapping {
        Integer LEVEL_NUMBER_1 = 1;
        Integer LEVEL_NUMBER_2 = 2;
        Integer LEVEL_NUMBER_3 = 3;
        String REGION = "REGION";
        String CITY = "CITY";
        String DISTRICT = "DISTRICT";
    }
}
