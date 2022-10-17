package org.o2.rule.engine.management.domain.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.o2.rule.engine.management.domain.entity.Rule;
import org.o2.rule.engine.management.domain.entity.RuleEntityCondition;
import org.o2.rule.engine.management.domain.entity.RuleParam;
import org.o2.rule.engine.management.infra.constants.RuleEngineConstants;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.StringJoiner;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author xiang.zhao@hand-chian.com 2022/10/13
 */
@Data
public class RuleConditionDTO {
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
                boolean skip = RuleEngineConstants.ComponentCode.BASIC.equals(miniConditionVO.getConditionCode()) && (null == miniCondition.get(RuleEngineConstants.BasicParameter.PARAMETER_OPERATOR) ||
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

    /**
     * 校验规则参数合法性
     * @param conditionCodes 条件编码集合
     * @param paramCodes 条件参数编码集合
     */
    public void allCondCodeParamCode(List<String> conditionCodes, List<String> paramCodes) {
        if (CollectionUtils.isNotEmpty(this.getNode())) {
            conditionCodes.addAll(this.getNode().stream().map(RuleMiniConditionDTO::getConditionCode).collect(Collectors.toList()));
            paramCodes.addAll(this.getNode().stream()
                    .map(RuleMiniConditionDTO::getParams)
                    .flatMap(Collection::stream)
                    .map(RuleMiniConditionParameterDTO::getParameterCode)
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
        if (CollectionUtils.isNotEmpty(this.getNode())) {
            conditionCodes.addAll(this.getNode().stream().map(RuleMiniConditionDTO::getConditionCode).collect(Collectors.toList()));
        }
        if (CollectionUtils.isNotEmpty(this.getChildren())) {
            for (RuleConditionDTO child : this.getChildren()) {
                child.allConditionCode(conditionCodes);
            }
        }
    }

    /**
     * 校验规则参数合法性
     * @param conditions 条件集合
     * @param params 条件参数集合
     */
    public void convert(List<RuleEntityCondition> conditions, List<RuleParam> params) {
        if (CollectionUtils.isEmpty(conditions) || CollectionUtils.isEmpty(params)) {
            return;
        }
        final Map<String, RuleEntityCondition> conditionMap = conditions.stream()
                .collect(Collectors.toMap(RuleEntityCondition::getConditionCode, Function.identity()));

        final Map<String, RuleParam> paramMap = params.stream()
                .collect(Collectors.toMap(RuleParam::getParamCode, Function.identity()));

        if (CollectionUtils.isNotEmpty(this.getNode())) {
            for (RuleMiniConditionDTO ruleMiniConditionDTO : this.getNode()) {
                final RuleEntityCondition entityCondition = conditionMap.get(ruleMiniConditionDTO.getConditionCode());
                if (entityCondition != null) {
                    entityCondition.setConditionName(entityCondition.getConditionName());
                    entityCondition.setEnableFlag(entityCondition.getEnableFlag());
                }
                if (CollectionUtils.isNotEmpty(ruleMiniConditionDTO.getParams())) {
                    for (RuleMiniConditionParameterDTO paramDTO : ruleMiniConditionDTO.getParams()) {
                        RuleParam param = paramMap.get(paramDTO.getParameterCode());
                        convertParam(param, paramDTO);
                    }
                }
            }
        }
    }

    /**
     * 校验规则参数合法性
     * @param param 参数
     * @param paramDTO 参数dto
     */
    public void convertParam(RuleParam param, RuleMiniConditionParameterDTO paramDTO) {
        paramDTO.setParameterId(param.getRuleParamId());
        paramDTO.setEnableFlag(param.getEnableFlag());
        paramDTO.setBusinessModel(param.getBusinessModel());
        paramDTO.setMultiFlag(param.getMultiflag());
        paramDTO.setParameterCode(param.getParamCode());
        paramDTO.setNotNullFlag(param.getNotNullFlag());
        paramDTO.setParamEditTypeCode(param.getParamEditTypeCode());
        paramDTO.setValueFiledFrom(param.getValueFiledFrom());
        paramDTO.setValueFiledTo(param.getValueFiledTo());
        paramDTO.setParamFilters(param.getParamFilters());
    }
}
