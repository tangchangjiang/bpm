package org.o2.rule.engine.management.domain.repository;

import org.hzero.mybatis.base.BaseRepository;
import org.o2.rule.engine.management.domain.bo.RuleEntityBO;
import org.o2.rule.engine.management.domain.entity.RuleEntity;

import java.util.List;
import java.util.Map;

/**
 * 规则实体资源库
 *
 * @author xiang.zhao@hand-china.com 2022-10-10 17:46:13
 */
public interface RuleEntityRepository extends BaseRepository<RuleEntity> {
    /**
     * 列表查询规则实体
     *
     * @param ruleEntity 查询条件
     * @return 规则实体列表
     */
    List<RuleEntity> selectList(RuleEntity ruleEntity);

    /**
     * 保存redis
     *  @param tenantId 租户id
     * @param ruleEntity 规则实体
     */
    void saveRedis(Long tenantId, RuleEntityBO ruleEntity);

    /**
     * 批量保存redis
     *
     * @param tenantId 租户id
     * @param ruleEntityMap 规则实体
     */
    void batchSaveRedis(Long tenantId, Map<String, String> ruleEntityMap);

    /**
     * 根据编码查询规则实体
     *
     * @param tenantId       租户Id
     * @param ruleEntityCode 规则实体编码
     * @return 规则实体
     */
    RuleEntity queryRuleEntityByCode(Long tenantId, String ruleEntityCode);
}
