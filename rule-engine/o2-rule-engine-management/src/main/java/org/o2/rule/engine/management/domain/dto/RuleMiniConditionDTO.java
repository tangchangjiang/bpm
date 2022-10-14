package org.o2.rule.engine.management.domain.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.hzero.core.base.BaseConstants;
import org.o2.rule.engine.management.app.validator.RuleParamValidator;
import org.o2.rule.engine.management.domain.entity.Rule;
import org.o2.rule.engine.management.infra.util.RuleConditionTranslatorHelper;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import io.choerodon.core.convertor.ApplicationContextHelper;

/**
 * @author xiang.zhao@hand-chian.com 2022/10/13
 */
@Data
public class RuleMiniConditionDTO {
    @ApiModelProperty("条件标准编码")
    private String code;
    @ApiModelProperty("最小条件名称")
    private String name;
    @ApiModelProperty("最小条件名称")
    private String componentCode;
    @ApiModelProperty("参数VOS")
    private List<RuleMiniConditionParameterDTO> params;

    /**
     * 转成MAP
     * @return map
     */
    public Map<String, RuleMiniConditionParameterDTO> convertToMap() {
        return params.stream().collect(Collectors.toMap(RuleMiniConditionParameterDTO::getParameterCode, Function.identity()));
    }

    /**
     * 构建条件值
     *
     * @param rule 规则
     * @return 返回值
     */
    public String condition(Rule rule) {
        return RuleConditionTranslatorHelper.translate(rule, this.code, this.params);
    }

    /**
     * 校验规则参数合法性
     *
     */
    public void valid() {
        for (RuleMiniConditionParameterDTO param : params) {
            if (StringUtils.isBlank(param.getValidators())) {
                continue;
            }
            for (String validBean : param.getValidators().split(BaseConstants.Symbol.COMMA)) {
                RuleParamValidator validator = ApplicationContextHelper.getContext().getBean(validBean, RuleParamValidator.class);
                validator.validate(param);
            }
        }
    }
}
