package org.o2.feignclient.metadata.infra.constants;

/**
 * 元数据 B端常量
 *
 * @author zhanpeng.jiang@hand-china.com
 * @date 2021/07/30 16:20
 */
public interface MetadataManagementConstants {

    /**
     * 静态资源文件编码
     */
    interface StaticResourceCode {
        /**
         * 元数据 - 地区数据文件
         */
        String O2MD_REGION = "O2MD_REGION";
        /**
         * 商品 - 类别文件
         */
        String O2PCM_CATEGORY = "O2PCM_CATEGORY";
        /**
         * CMS - CMS页面装修
         */
        String O2CMS_DECORATION = "O2CMS_DECORATION";
    }

    /**
     * 静态资源文件来源系统编码
     */
    interface StaticResourceSourceModuleCode {
        String METADATA = "METADATA";
        String PCM = "PRODUCT";
        String CMS = "CMS";
    }

}
