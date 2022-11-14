package org.o2.metadata.infra.lovadapter.repository;

import org.o2.metadata.api.co.LovValuesCO;

import java.util.List;

/**
 *
 * 独立值集查询
 *
 * @author yipeng.zhu@hand-china.com 2021-09-23
 **/
public interface IdpLovQueryRepository {



    /**
     * 查询独立值集中指定值的 描述信息
     *
     * @param tenantId 租户id
     * @param lovCode  值集code
     * @param lovValue 值集value
     * @return String
     */
    String queryLovValueMeaning(Long tenantId,
                                String lovCode,
                                String lovValue);

    /**
     * 查询独立值
     * @param tenantId 租户id
     * @param lovCodes  值集code
     * @return co
     */
    List<LovValuesCO> queryIdpLov(Long tenantId, List<String> lovCodes);
}
