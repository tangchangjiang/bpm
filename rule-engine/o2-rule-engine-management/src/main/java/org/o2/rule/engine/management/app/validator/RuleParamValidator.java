package org.o2.rule.engine.management.app.validator;

import org.o2.rule.engine.management.domain.dto.RuleMiniConditionParameterDTO;

/**
 * 规则校验接口
 *
 * @author xiang.zhao@hand-chian.com 2022/10/13
 */
public interface RuleParamValidator {

    /**
     * 验证参数合法性
     *
     * @param parameter 参数
     * @return boolean
     */
    void validate(RuleMiniConditionParameterDTO parameter);
}
