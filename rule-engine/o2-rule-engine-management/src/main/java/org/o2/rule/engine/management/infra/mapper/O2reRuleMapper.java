package org.o2.rule.engine.management.infra.mapper;

import org.apache.ibatis.annotations.Param;
import org.o2.rule.engine.management.domain.entity.Rule;
import org.o2.rule.engine.management.domain.vo.RuleVO;

import io.choerodon.mybatis.common.BaseMapper;

/**
 * 规则Mapper
 *
 * @author xiang.zhao@hand-china.com 2022-10-10 17:46:13
 */
public interface O2reRuleMapper extends BaseMapper<Rule> {


    /**
     * 根据编码查询规则
     *
     * @param tenantId 租户id
     * @param ruleCode 规则代码
     * @return {@link RuleVO}
     */
    RuleVO getRuleByCode(@Param("tenantId") Long tenantId, @Param("ruleCode") String ruleCode);
}
