package org.o2.rule.engine.management.domain.repository;

import org.hzero.mybatis.base.BaseRepository;
import org.o2.rule.engine.management.domain.bo.RuleEntityBO;
import org.o2.rule.engine.management.domain.entity.RuleEntity;

import java.util.List;

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
}
