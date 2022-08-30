package org.o2.metadata.infra.constants;

/**
 * Description
 *
 * @author yipeng.zhu@hand-china.com 2022/8/30
 */
public interface MetadataCacheConstants {
    /**
     * 缓存名称
     */
    interface CacheName {
        String ACROSS = "O2MD_ACROSS";
    }

    interface CacheKey {
        String FETCH_USER_ROLE_BY_CODE = "fetchRoleInner_%s";


        /**
         * 获取角色
         * @param tenantId tenantId
         * @return keyPrefix
         */
        static String getFetchRolePrefix(Long tenantId) {
            return String.format(FETCH_USER_ROLE_BY_CODE, tenantId);
        }
    }
}
