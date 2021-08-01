package org.o2.feignclient.metadata.infra.constants;

import org.apache.commons.lang3.StringUtils;
import org.hzero.core.base.BaseConstants;

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

        /**
         * 构造CMS配置文件资源编码，需要用siteCode作为后缀区分不同站点的cms配置文件
         *
         * @param siteCode 站点编码
         * @return CMS配置文件资源编码
         */
        static String buildCmsResourceCode(String siteCode) {
            return O2CMS_DECORATION + BaseConstants.Symbol.LOWER_LINE + StringUtils.upperCase(siteCode);
        }

        /**
         * 构造商品模块资源编码
         *
         * @param catalogCode        目录编码
         * @param catalogVersionCode 目录版本编码
         * @return 商品类别文件资源编码
         */
        static String buildPcmCategoryCode(String catalogCode, String catalogVersionCode) {
            return String.format("%s_%s_%s", O2PCM_CATEGORY, catalogCode, catalogVersionCode);
        }
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
