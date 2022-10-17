package org.o2.rule.engine.management.domain.repository;

import org.hzero.mybatis.base.BaseRepository;
import org.o2.rule.engine.management.domain.entity.Rule;
import org.o2.rule.engine.management.domain.vo.RuleVO;
import java.util.List;

/**
 * 规则资源库
 *
 * @author xiang.zhao@hand-china.com 2022-10-10 17:46:13
 */
public interface RuleRepository extends BaseRepository<Rule> {

    /**
     * 根据id查询规则
     *
     * @param tenantId 租户id
     * @param ruleId 规则id
     * @return {@link Rule}
     */
    Rule getRuleById(Long tenantId, Long ruleId);

    /**
     * 根据编码查询规则
     *
     * @param tenantId 租户id
     * @param ruleCode 规则代码
     * @return {@link RuleVO}
     */
    RuleVO getRuleByCode(Long tenantId, String ruleCode);

    /**
     * 推送规则到Redis
     *
     * @param tenantId 租户id
     * @param rules 规则
     */
    void loadToCache(Long tenantId, List<Rule> rules);

    /**
     * 删除Redis规则信息
     *
     * @param tenantId 租户id
     * @param rules 规则
     */
    void removeCache(Long tenantId, List<Rule> rules);
}
