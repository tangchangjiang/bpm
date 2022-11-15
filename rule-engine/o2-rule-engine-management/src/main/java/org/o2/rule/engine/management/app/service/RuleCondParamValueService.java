package org.o2.rule.engine.management.app.service;

import org.o2.rule.engine.management.domain.entity.RuleCondParamValue;
import java.util.List;

/**
 * 规则条件参数值应用服务
 *
 * @author xiang.zhao@hand-china.com 2022-10-10 17:46:13
 */
public interface RuleCondParamValueService {

    /**
     * 批量保存规则条件参数值
     *
     * @param ruleCondParamValueList 规则条件参数值对象列表
     * @return 规则条件参数值对象列表
     */
    List<RuleCondParamValue> batchSave(List<RuleCondParamValue> ruleCondParamValueList);

    /**
     * 保存规则条件参数值
     *
     * @param ruleCondParamValue 规则条件参数值对象
     * @return 规则条件参数值对象
     */
    RuleCondParamValue save(RuleCondParamValue ruleCondParamValue);
}
