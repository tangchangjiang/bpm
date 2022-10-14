package org.o2.rule.engine.management.app.service.impl;

import org.o2.rule.engine.management.app.service.RuleCondParamValueService;
import org.o2.rule.engine.management.domain.entity.RuleCondParamValue;
import org.o2.rule.engine.management.domain.repository.RuleCondParamValueRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import io.choerodon.mybatis.domain.AuditDomain;


/**
 * 规则条件参数值应用服务默认实现
 *
 * @author xiang.zhao@hand-china.com 2022-10-10 17:46:13
 */
@Service
public class RuleCondParamValueServiceImpl implements RuleCondParamValueService {

    private final RuleCondParamValueRepository ruleCondParamValueRepository;

    public RuleCondParamValueServiceImpl(RuleCondParamValueRepository ruleCondParamValueRepository) {
        this.ruleCondParamValueRepository = ruleCondParamValueRepository;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<RuleCondParamValue> batchSave(List<RuleCondParamValue> ruleCondParamValueList) {
        Map<AuditDomain.RecordStatus, List<RuleCondParamValue>> statusMap = ruleCondParamValueList.stream().collect(Collectors.groupingBy(RuleCondParamValue::get_status));
        // 删除
        if (statusMap.containsKey(AuditDomain.RecordStatus.delete)) {
            List<RuleCondParamValue> deleteList = statusMap.get(AuditDomain.RecordStatus.delete);
            ruleCondParamValueRepository.batchDeleteByPrimaryKey(deleteList);
        }
        // 更新
        if (statusMap.containsKey(AuditDomain.RecordStatus.update)) {
            List<RuleCondParamValue> updateList = statusMap.get(AuditDomain.RecordStatus.update);
            updateList.forEach(item -> {
                // TODO: 唯一性校验
                ruleCondParamValueRepository.updateByPrimaryKeySelective(item);
            });
        }
        // 新增
        if (statusMap.containsKey(AuditDomain.RecordStatus.create)) {
            List<RuleCondParamValue> createList = statusMap.get(AuditDomain.RecordStatus.create);
            createList.forEach(item -> {
                // TODO: 唯一性校验
                ruleCondParamValueRepository.insertSelective(item);
            });
        }
        return ruleCondParamValueList;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public RuleCondParamValue save(RuleCondParamValue ruleCondParamValue) {
        //保存规则条件参数值
        if (ruleCondParamValue.getCondParamValueId() == null) {
            ruleCondParamValueRepository.insertSelective(ruleCondParamValue);
        } else {
            ruleCondParamValueRepository.updateOptional(ruleCondParamValue,
                    RuleCondParamValue.FIELD_PARAM_ID,
                    RuleCondParamValue.FIELD_RULE_CONDITION_ID,
                    RuleCondParamValue.FIELD_PARAM_VALUE,
                    RuleCondParamValue.FIELD_TENANT_ID
            );
        }

        return ruleCondParamValue;
    }
}
