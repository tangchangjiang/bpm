package org.o2.rule.engine.management.infra.mapper;

import org.apache.ibatis.annotations.Param;
import org.o2.rule.engine.management.domain.entity.Rule;
import org.o2.rule.engine.management.domain.vo.RuleVO;

import java.util.List;

import io.choerodon.mybatis.common.BaseMapper;

/**
 * 规则Mapper
 *
 * @author xiang.zhao@hand-china.com 2022-10-10 17:46:13
 */
public interface O2reRuleMapper extends BaseMapper<Rule> {
    /**
     * 查询规则列表
     *
     * @param rule 规则查询参数
     * @return {@link Rule}
     */
    List<Rule> ruleList(Rule rule);

    /**
     * 根据id查询规则
     *
     * @param tenantId 租户id
     * @param ruleId 规则id
     * @return {@link Rule}
     */
    Rule getRuleById(@Param("tenantId") Long tenantId, @Param("ruleId") Long ruleId);

    /**
     * 根据编码查询规则
     *
     * @param tenantId 租户id
     * @param ruleCode 规则代码
     * @return {@link RuleVO}
     */
    RuleVO getRuleByCode(@Param("tenantId") Long tenantId, @Param("ruleCode") String ruleCode);
}
