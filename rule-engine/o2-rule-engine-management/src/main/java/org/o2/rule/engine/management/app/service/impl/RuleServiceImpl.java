package org.o2.rule.engine.management.app.service.impl;

import org.hzero.mybatis.helper.UniqueHelper;
import org.o2.rule.engine.management.app.service.RuleService;
import org.o2.rule.engine.management.domain.entity.Rule;
import org.o2.rule.engine.management.domain.repository.RuleRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import io.choerodon.mybatis.domain.AuditDomain;

/**
 * 规则应用服务默认实现
 *
 * @author xiang.zhao@hand-china.com 2022-10-10 17:46:13
 */
@Service
public class RuleServiceImpl implements RuleService {
                                                                                                
    private final RuleRepository ruleRepository;

    public RuleServiceImpl(RuleRepository ruleRepository) {
        this.ruleRepository = ruleRepository;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<Rule> batchSave(List<Rule> ruleList) {
        Map<AuditDomain.RecordStatus, List<Rule>> statusMap = ruleList.stream().collect(Collectors.groupingBy(Rule::get_status));
        // 删除
        if (statusMap.containsKey(AuditDomain.RecordStatus.delete)) {
            List<Rule> deleteList = statusMap.get(AuditDomain.RecordStatus.delete);
            ruleRepository.batchDeleteByPrimaryKey(deleteList);
        }
        // 更新
        if (statusMap.containsKey(AuditDomain.RecordStatus.update)) {
            List<Rule> updateList = statusMap.get(AuditDomain.RecordStatus.update);
            updateList.forEach(item -> {
                // TODO: 唯一性校验
                UniqueHelper.valid(item, Rule.O2RE_RULE_U1);
                ruleRepository.updateByPrimaryKeySelective(item);
            });
        }
        // 新增
        if (statusMap.containsKey(AuditDomain.RecordStatus.create)) {
            List<Rule> createList = statusMap.get(AuditDomain.RecordStatus.create);
            createList.forEach(item -> {
                // TODO: 唯一性校验
                UniqueHelper.valid(item, Rule.O2RE_RULE_U1);
                ruleRepository.insertSelective(item);
            });
        }
        return ruleList;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Rule save(Rule rule) {
        //保存规则
        UniqueHelper.valid(rule, Rule.O2RE_RULE_U1);
        if (rule.getRuleId() == null) {
            ruleRepository.insertSelective(rule);
        } else {
            ruleRepository.updateOptional(rule,
                    Rule.FIELD_RULE_CODE,
                    Rule.FIELD_RULE_NAME,
                    Rule.FIELD_ENTITY_CODE,
                    Rule.FIELD_RULE_DESCRIPTION,
                    Rule.FIELD_RULE_JSON,
                    Rule.FIELD_ENABLE_FLAG,
                    Rule.FIELD_START_TIME,
                    Rule.FIELD_END_TIME,
                    Rule.FIELD_TENANT_ID
            );
        }

        return rule;
    }
}
