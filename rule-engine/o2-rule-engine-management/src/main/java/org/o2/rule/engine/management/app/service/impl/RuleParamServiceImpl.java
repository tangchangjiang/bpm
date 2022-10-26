package org.o2.rule.engine.management.app.service.impl;

import org.hzero.mybatis.helper.UniqueHelper;
import org.o2.rule.engine.management.app.service.RuleParamService;
import org.o2.rule.engine.management.domain.entity.RuleParam;
import org.o2.rule.engine.management.domain.repository.RuleParamRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 规则参数应用服务默认实现
 *
 * @author xiang.zhao@hand-china.com 2022-10-10 17:46:13
 */
@Service
public class RuleParamServiceImpl implements RuleParamService {

    private final RuleParamRepository ruleParamRepository;

    public RuleParamServiceImpl(RuleParamRepository ruleParamRepository) {
        this.ruleParamRepository = ruleParamRepository;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public RuleParam save(RuleParam ruleParam) {
        //保存规则参数
        UniqueHelper.valid(ruleParam, RuleParam.O2RE_RULE_PARAM_U1);
        if (ruleParam.getRuleParamId() == null) {
            ruleParamRepository.insertSelective(ruleParam);
        } else {
            ruleParamRepository.updateOptional(ruleParam,
                    RuleParam.FIELD_PARAM_NAME,
                    RuleParam.FIELD_PARAM_ALIAS,
                    RuleParam.FIELD_ORDER_SEQ,
                    RuleParam.FIELD_PARAM_FORMAT_CODE,
                    RuleParam.FIELD_PARAM_EDIT_TYPE_CODE,
                    RuleParam.FIELD_MULTI_FLAG,
                    RuleParam.FIELD_NOT_NULL_FLAG,
                    RuleParam.FIELD_BUSINESS_MODEL,
                    RuleParam.FIELD_VALUE_FILED_FROM,
                    RuleParam.FIELD_VALUE_FILED_TO,
                    RuleParam.FIELD_ENABLE_FLAG,
                    RuleParam.FIELD_DEFAULT_MEANING,
                    RuleParam.FIELD_VALIDATORS,
                    RuleParam.FIELD_FILTERS
            );
        }

        return ruleParam;
    }
}
