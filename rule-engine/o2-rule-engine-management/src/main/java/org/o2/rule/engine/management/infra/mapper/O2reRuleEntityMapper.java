package org.o2.rule.engine.management.infra.mapper;

import org.o2.rule.engine.management.domain.entity.RuleEntity;
import io.choerodon.mybatis.common.BaseMapper;

import java.util.List;

/**
 * 规则实体Mapper
 *
 * @author xiang.zhao@hand-china.com 2022-10-10 17:46:13
 */
public interface O2reRuleEntityMapper extends BaseMapper<RuleEntity> {
    /**
     * 规则实体列表查询
     *
     * @param ruleEntity 查询条件
     * @return 规则实体列表
     */
    List<RuleEntity> selectList(RuleEntity ruleEntity);
}
