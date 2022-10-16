package org.o2.rule.engine.management.app.validator.impl;

import com.fasterxml.jackson.core.io.BigDecimalParser;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.o2.rule.engine.management.app.validator.RuleParamValidator;
import org.o2.rule.engine.management.domain.dto.RuleMiniConditionParameterDTO;
import org.o2.rule.engine.management.infra.constants.RuleEngineConstants;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;

import io.choerodon.core.exception.CommonException;

/**
 * @author xiang.zhao@hand-chian.com 2022/10/13
 */
@Slf4j
@Service("bigDecimalValidatorImpl")
public class BigDecimalValidatorImpl implements RuleParamValidator {
    @Override
    public void validate(RuleMiniConditionParameterDTO parameter) {
        boolean result = false;
        if (RuleEngineConstants.ParameterType.BIG_DECIMAL.equals(parameter.getParamFormatCode()) && StringUtils.isNotBlank(parameter.getParameterValue())) {
            try {
                BigDecimal value = BigDecimalParser.parse(parameter.getParameterValue());
                result = true;
            } catch (NumberFormatException e) {
                log.warn("value {} type error: ", parameter.getParameterValue());
            }
        }
        if (result) {
            throw new CommonException("");
        }
    }
}
