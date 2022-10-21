package org.o2.rule.engine.management.domain.repository;

import org.hzero.mybatis.base.BaseRepository;
import org.o2.rule.engine.management.domain.bo.RuleQueryBO;
import org.o2.rule.engine.management.domain.entity.Rule;
import org.o2.rule.engine.management.domain.vo.RuleVO;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 规则资源库
 *
 * @author xiang.zhao@hand-china.com 2022-10-10 17:46:13
 */
public interface RuleRepository extends BaseRepository<Rule> {

    /**
     * 查询规则列表
     *
     * @param rule 规则查询参数
     * @return {@link Rule}
     */
    List<Rule> ruleList(Rule rule);

    /**
     * 查询规则列表
     *
     * @param query 查询参数
     * @return {@link Rule}
     */
    List<Rule> findRuleList(RuleQueryBO query);

    /**
     * 根据id查询规则
     *
     * @param tenantId 租户id
     * @param ruleId 规则id
     * @param ruleCode 规则编码
     * @return {@link Rule}
     */
    Rule getRuleDetail(Long tenantId, Long ruleId, String ruleCode);

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

    /**
     * 查询实体更新时间
     *
     * @param tenantId    租户id
     * @param entityCodes 实体规范
     * @return {@link Map}<{@link String}, {@link Date}>
     */
    Map<String, Date> getEntitiesUpdateTime(Long tenantId, List<String> entityCodes);
}
