package org.o2.metadata.infra.lovadapter.repository;


import org.hzero.boot.platform.lov.dto.LovValueDTO;

import java.util.List;

/**
 *
 * 独立值集查询
 *
 * @author yipeng.zhu@hand-china.com 2021-09-23
 **/
public interface IdpLovQueryRepository {

    /**
     * 独立值集详细信息
     *
     * @param lovCode  值集code
     * @param tenantId 租户id
     * @return List<LovValueDTO>
     */
    List<LovValueDTO> queryLovValue(Long tenantId,
                                    String lovCode);

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
}