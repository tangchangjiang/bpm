package org.o2.rule.engine.management.domain.repository;

import org.hzero.mybatis.base.BaseRepository;
import org.o2.rule.engine.management.domain.entity.Rule;
import org.o2.rule.engine.management.domain.vo.RuleVO;

/**
 * 规则资源库
 *
 * @author xiang.zhao@hand-china.com 2022-10-10 17:46:13
 */
public interface RuleRepository extends BaseRepository<Rule> {

    /**
     * 根据编码查询规则
     *
     * @param tenantId 租户id
     * @param ruleCode 规则代码
     * @return {@link RuleVO}
     */
    RuleVO getRuleByCode(Long tenantId, String ruleCode);
}
