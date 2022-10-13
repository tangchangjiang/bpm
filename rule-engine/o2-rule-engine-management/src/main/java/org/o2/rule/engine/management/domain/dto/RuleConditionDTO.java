package org.o2.rule.engine.management.domain.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.o2.rule.engine.management.domain.entity.Rule;
import org.o2.rule.engine.management.infra.constants.RuleEngineConstants;
import java.util.List;
import java.util.Map;
import java.util.StringJoiner;

/**
 * @author xiang.zhao@hand-chian.com 2022/10/13
 */
@Data
public class RuleConditionDTO {
    @ApiModelProperty("实体条件Id")
    private Long ruleEntityConditionId;
    @ApiModelProperty("子条件集合")
    private List<RuleConditionDTO> children;
    @ApiModelProperty("最小条件集合")
    private List<RuleMiniConditionDTO> node;
    @ApiModelProperty("是否为AND还是OR")
    private AndOr relation;

    /**
     * 构建条件字符串
     *
     * @param rule 规则
     * @return 字符串
     */
    public String build(Rule rule) {
        final StringJoiner sj = new StringJoiner(this.relation.getValue(), "(", ")");
        //IF All Empty, Return False
        if (CollectionUtils.isEmpty(this.getNode()) && CollectionUtils.isEmpty(this.getChildren())) {
            return Boolean.FALSE.toString();
        }
        if (CollectionUtils.isNotEmpty(this.getNode())) {
            for (RuleMiniConditionDTO miniConditionVO : this.getNode()) {
                final Map<String, RuleMiniConditionParameterDTO> miniCondition = miniConditionVO.convertToMap();
                //如果为基本组件且值为空,则不进行条件编译
                boolean skip = RuleEngineConstants.ComponentCode.BASIC.equals(miniConditionVO.getCode()) && (null == miniCondition.get(RuleEngineConstants.BasicParameter.PARAMETER_OPERATOR) ||
                        StringUtils.isEmpty(miniCondition.get(RuleEngineConstants.BasicParameter.PARAMETER_OPERATOR).getParameterValue()) ||
                        null == miniCondition.get(RuleEngineConstants.BasicParameter.PARAMETER_VALUE) ||
                        StringUtils.isEmpty(miniCondition.get(RuleEngineConstants.BasicParameter.PARAMETER_VALUE).getParameterValue()));
                if (skip) {
                    continue;
                }
                sj.add(miniConditionVO.condition(rule));
            }
        }
        if (CollectionUtils.isNotEmpty(this.getChildren())) {
            for (RuleConditionDTO child : this.getChildren()) {
                sj.add(child.build(rule));
            }
        }
        return sj.toString();
    }

    /**
     * 校验规则参数合法性
     *
     */
    public void valid() {
        //IF All Empty, Return False
        if (CollectionUtils.isEmpty(this.getNode()) && CollectionUtils.isEmpty(this.getChildren())) {
            return;
        }
        if (CollectionUtils.isNotEmpty(this.getNode())) {
            for (RuleMiniConditionDTO miniConditionVO : this.getNode()) {
                miniConditionVO.valid();
            }
        }
        if (CollectionUtils.isNotEmpty(this.getChildren())) {
            for (RuleConditionDTO child : this.getChildren()) {
                child.valid();
            }
        }
    }
}
