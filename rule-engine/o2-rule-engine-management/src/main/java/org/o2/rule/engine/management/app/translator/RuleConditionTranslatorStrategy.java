package org.o2.rule.engine.management.app.translator;

import org.o2.rule.engine.management.domain.dto.RuleMiniConditionParameterDTO;
import org.o2.rule.engine.management.domain.entity.Rule;
import java.util.List;

/**
 * 条件翻译策略类
 *
 * @author xiang.zhao@hand-china.com 2020/9/7
 */
public interface RuleConditionTranslatorStrategy {

    /**
     * 通过编码进行翻译
     * @param rule 规则
     * @param code 编码
     * @param parameters 参数值
     * @return 返回值
     */
    String translate(Rule rule, String code, List<RuleMiniConditionParameterDTO> parameters);


}
