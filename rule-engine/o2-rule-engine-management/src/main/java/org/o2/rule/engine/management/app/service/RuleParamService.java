package org.o2.rule.engine.management.app.service;

import org.o2.rule.engine.management.domain.entity.RuleParam;
import java.util.List;


/**
 * 规则参数应用服务
 *
 * @author xiang.zhao@hand-china.com 2022-10-10 17:46:13
 */
public interface RuleParamService {

    
    /**
     * 批量保存规则参数
     *
     * @param ruleParamList 规则参数对象列表
     * @return 规则参数对象列表
     */
    List<RuleParam> batchSave(List<RuleParam> ruleParamList);


    /**
     * 保存规则参数
     *
     * @param ruleParam 规则参数对象
     * @return 规则参数对象
     */
    RuleParam save(RuleParam ruleParam);
}
