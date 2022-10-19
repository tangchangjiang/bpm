package org.o2.rule.engine.management.domain.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.o2.core.helper.JsonHelper;
import org.o2.rule.engine.management.domain.entity.Rule;
import org.o2.rule.engine.management.domain.entity.RuleEntityCondition;
import org.o2.rule.engine.management.domain.entity.RuleParam;
import org.o2.rule.engine.management.infra.constants.RuleEngineConstants;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author xiang.zhao@hand-chian.com 2022/10/13
 */
@Data
@Slf4j
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RuleConditionDTO {
    @ApiModelProperty("子条件集合")
    private List<RuleConditionDTO> children;
    @ApiModelProperty("最小条件集合")
    private RuleMiniConditionDTO node;
    @ApiModelProperty("是否为AND还是OR")
    private AndOr relation;

    /**
     * 构建条件字符串
     *
     * @param rule 规则
     * @param rel 关系
     * @return 字符串
     */
    public String build(Rule rule, AndOr rel) {
        log.info("relation {}, node {}", JsonHelper.objectToString(relation), JsonHelper.objectToString(node));
        final StringJoiner sj;
        if (rel == null) {
            sj = new StringJoiner(this.relation.getValue(), "(", ")");
        } else {
            sj = new StringJoiner(rel.getValue(), "(", ")");
        }
        //IF All Empty, Return False
        if (node == null && CollectionUtils.isEmpty(this.getChildren())) {
            return Boolean.FALSE.toString();
        }
        if (node != null) {
            final List<RuleMiniConditionParameterDTO> params = node.getParams();
            final List<String> paramCodes = params.stream().map(RuleMiniConditionParameterDTO::getParamCode).collect(Collectors.toList());
            //如果为基本组件且值为空,则不进行条件编译
            boolean skip = RuleEngineConstants.ComponentCode.BASIC.equals(node.getConditionCode()) &&
                    (!paramCodes.contains(RuleEngineConstants.BasicParameter.PARAMETER_OPERATOR) || !paramCodes.contains(RuleEngineConstants.BasicParameter.PARAMETER_VALUE));
            if (skip) {
                return Boolean.FALSE.toString();
            }
            sj.add(node.condition(rule));
        }
        if (CollectionUtils.isNotEmpty(this.getChildren())) {
            for (RuleConditionDTO child : this.getChildren()) {
                if (child.getRelation() == null) {
                    sj.add(child.build(rule, this.relation));
                } else {
                    sj.add(child.build(rule, null));
                }
            }
        }
        return sj.toString();
    }

    /**
     * 校验规则参数合法性
     *
     */
    public void valid() {
        if (node != null) {
            node.valid();
        }
        if (CollectionUtils.isNotEmpty(this.getChildren())) {
            for (RuleConditionDTO child : this.getChildren()) {
                if (child == null) {
                    continue;
                }
                child.valid();
            }
        }
    }

    /**
     * 校验规则参数合法性
     * @param conditionCodes 条件编码集合
     * @param paramCodes 条件参数编码集合
     */
    public void allCondCodeParamCode(List<String> conditionCodes, List<String> paramCodes) {
        if (node != null) {
            conditionCodes.add(node.getConditionCode());
            paramCodes.addAll(node.getParams().stream()
                    .map(RuleMiniConditionParameterDTO::getParamCode)
                    .collect(Collectors.toList()));
        }
        if (CollectionUtils.isNotEmpty(this.getChildren())) {
            for (RuleConditionDTO child : this.getChildren()) {
                child.allCondCodeParamCode(conditionCodes, paramCodes);
            }
        }
    }

    /**
     * 校验规则参数合法性
     * @param conditionCodes 条件编码集合
     */
    public void allConditionCode(List<String> conditionCodes) {
        if (node != null) {
            conditionCodes.add(node.getConditionCode());
        }
        if (CollectionUtils.isNotEmpty(this.getChildren())) {
            for (RuleConditionDTO child : this.getChildren()) {
                child.allConditionCode(conditionCodes);
            }
        }
    }

    /**
     * 校验规则参数合法性
     */
    public List<Long> allConditionId() {
        final List<Long> conditionIds = new ArrayList<>();
        if (node != null) {
            conditionIds.add(node.getConditionId());
        }
        if (CollectionUtils.isNotEmpty(this.getChildren())) {
            for (RuleConditionDTO child : this.getChildren()) {
                child.allConditionId();
            }
        }
        return conditionIds;
    }

    /**
     * 转化规则条件
     * @param conditions 条件集合
     * @param params 条件参数集合
     */
    public void convertCondition(List<RuleEntityCondition> conditions, List<RuleParam> params) {
        if (CollectionUtils.isEmpty(conditions) || CollectionUtils.isEmpty(params)) {
            return;
        }
        final Map<String, RuleEntityCondition> conditionMap = conditions.stream()
                .collect(Collectors.toMap(RuleEntityCondition::getConditionCode, Function.identity()));

        final Map<String, RuleParam> paramMap = params.stream()
                .collect(Collectors.toMap(RuleParam::getParamCode, Function.identity()));

        if (this.node != null) {
            final RuleEntityCondition entityCondition = conditionMap.get(node.getConditionCode());
            if (entityCondition != null) {
                node.setConditionName(entityCondition.getConditionName());
                node.setEnableFlag(entityCondition.getEnableFlag());
                node.setConditionCodeAlias(entityCondition.getConditionCodeAlias());
            }
            if (CollectionUtils.isNotEmpty(node.getParams())) {
                for (RuleMiniConditionParameterDTO paramDTO : node.getParams()) {
                    RuleParam param = paramMap.get(paramDTO.getParamCode());
                    convertParam(param, paramDTO);
                }
                node.getParams().sort(Comparator.comparing(RuleMiniConditionParameterDTO::getPriority));
            }
        }
    }

    /**
     * 转化规则参数
     * @param param 参数
     * @param paramDTO 参数dto
     */
    public void convertParam(RuleParam param, RuleMiniConditionParameterDTO paramDTO) {
        paramDTO.setRuleParamId(param.getRuleParamId());
        paramDTO.setEnableFlag(param.getEnableFlag());
        paramDTO.setBusinessModel(param.getBusinessModel());
        paramDTO.setMultiFlag(param.getMultiFlag());
        paramDTO.setParamCode(param.getParamCode());
        paramDTO.setNotNullFlag(param.getNotNullFlag());
        paramDTO.setParamEditTypeCode(param.getParamEditTypeCode());
        paramDTO.setValueFiledFrom(param.getValueFiledFrom());
        paramDTO.setValueFiledTo(param.getValueFiledTo());
        paramDTO.setParamFilters(param.getParamFilters());
    }
}
