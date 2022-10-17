package org.o2.rule.engine.client.domain.vo;

import lombok.Data;

import java.util.Date;

/**
 * 规则VO
 *
 * @author wei.cai@hand-china.com
 * @date 2022/10/17
 */
@Data
public class RuleVO {
    /**
     * 规则编码
     */
    private String ruleCode;
    /**
     * 规则名称
     */
    private String ruleName;
    /**
     * 规则实体编码
     */
    private String ruleEntityCode;
    /**
     * 规则实体别名
     */
    private String ruleEntityAlias;
    /**
     * 开始时间
     */
    private Date startTime;
    /**
     * 结束时间
     */
    private Date endTime;
    /**
     * 条件
     */
    private RuleConditionVO condition;
}
