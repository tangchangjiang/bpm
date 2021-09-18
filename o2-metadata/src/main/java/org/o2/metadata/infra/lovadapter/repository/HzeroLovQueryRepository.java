package org.o2.metadata.infra.lovadapter.repository;


/**
 * hzero lov查询工具
 *
 * @Date 2021年08月23日
 * @Author yipeng.zhu@hand-china.com
 */
public interface HzeroLovQueryRepository {


    /**
     * 查询值集中指定值的 描述信息（meaning）
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
