package org.o2.rule.engine.management.app.validator.impl;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.o2.rule.engine.management.app.validator.RuleParamValidator;
import org.o2.rule.engine.management.domain.dto.RuleMiniConditionParameterDTO;
import org.o2.rule.engine.management.infra.constants.RuleEngineConstants;
import org.springframework.stereotype.Service;

import io.choerodon.core.exception.CommonException;

/**
 * 整型参数校验
 *
 * @author xiang.zhao@hand-chian.com 2022/10/13
 */
@Slf4j
@Service("integerValidatorImpl")
public class IntegerValidatorImpl implements RuleParamValidator {
    @Override
    public void validate(RuleMiniConditionParameterDTO parameter) {
        boolean result = false;
        if (RuleEngineConstants.ParameterType.INTEGER.equals(parameter.getParamFormatCode()) && StringUtils.isNotBlank(parameter.getParameterValue())) {
            try {
                Integer value = Integer.valueOf(parameter.getParameterValue());
                result = true;
            } catch (NumberFormatException e) {
                log.error("parameter value {} parameter type {}: ", parameter.getParameterValue(), parameter.getParamFormatCode());
            }
        }
        if (result) {
            throw new CommonException("");
        }
    }
}
