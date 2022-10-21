package org.o2.rule.engine.management.domain.repository;

import org.hzero.mybatis.base.BaseRepository;
import org.o2.rule.engine.management.domain.entity.RuleParam;

import java.util.List;

/**
 * 规则参数资源库
 *
 * @author xiang.zhao@hand-china.com 2022-10-10 17:46:13
 */
public interface RuleParamRepository extends BaseRepository<RuleParam> {

    /**
     * 更新时间倒序查询参数列表
     *
     * @param ruleParam 查询条件
     * @return 参数列表
     */
    List<RuleParam> selectList(RuleParam ruleParam);
}
