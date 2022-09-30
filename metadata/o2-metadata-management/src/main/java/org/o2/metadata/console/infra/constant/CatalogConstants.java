package org.o2.metadata.console.infra.constant;

/**
 *
 * 目录&目录版本常量
 *
 * @author yipeng.zhu@hand-china.com 2021-11-11
 **/
public interface CatalogConstants {

    interface ErrorCode {
        String O2MD_CATALOG_VERSION_CODE_UNIQUE = "o2md.catalog_version_unique_error";
        String O2MD_CATALOG_VERSION_NAME_UNIQUE = "o2md.catalog_version_name_unique_error";
        String O2MD_CATALOG_NAME_UNIQUE = "o2md.catalog_name_unique_error";
        String O2MD_CATALOG_CODE_UNIQUE = "o2md.error.catalog_code.not.unique";
        String  O2MD_CATALOG_CODE_NOT_UPDATE ="o2md.error.catalog_code.forbidden.update";
        String O2MD_ERROR_CATALOG_FORBIDDEN = "o2md.error.catalog.forbidden";
        String O2MD_CATALOG_VERSION_CODE_FORBIDDEN_UPDATE ="o2md.error.catalog_version_code.forbidden.update";
    }
}
