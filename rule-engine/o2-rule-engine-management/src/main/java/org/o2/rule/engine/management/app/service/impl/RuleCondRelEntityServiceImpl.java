package org.o2.rule.engine.management.app.service.impl;

import org.o2.rule.engine.management.app.service.RuleCondRelEntityService;
import org.o2.rule.engine.management.domain.entity.RuleCondRelEntity;
import org.o2.rule.engine.management.domain.repository.RuleCondRelEntityRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import io.choerodon.mybatis.domain.AuditDomain;


/**
 * 规则关联条件应用服务默认实现
 *
 * @author xiang.zhao@hand-china.com 2022-10-10 17:46:13
 */
@Service
public class RuleCondRelEntityServiceImpl implements RuleCondRelEntityService {

    private final RuleCondRelEntityRepository ruleCondRelEntityRepository;

    public RuleCondRelEntityServiceImpl(RuleCondRelEntityRepository ruleCondRelEntityRepository) {
        this.ruleCondRelEntityRepository = ruleCondRelEntityRepository;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<RuleCondRelEntity> batchSave(List<RuleCondRelEntity> ruleCondRelEntityList) {
        Map<AuditDomain.RecordStatus, List<RuleCondRelEntity>> statusMap = ruleCondRelEntityList.stream().collect(Collectors.groupingBy(RuleCondRelEntity::get_status));
        // 删除
        if (statusMap.containsKey(AuditDomain.RecordStatus.delete)) {
            List<RuleCondRelEntity> deleteList = statusMap.get(AuditDomain.RecordStatus.delete);
            ruleCondRelEntityRepository.batchDeleteByPrimaryKey(deleteList);
        }
        // 更新
        if (statusMap.containsKey(AuditDomain.RecordStatus.update)) {
            List<RuleCondRelEntity> updateList = statusMap.get(AuditDomain.RecordStatus.update);
            updateList.forEach(item -> {
                // TODO: 唯一性校验
                ruleCondRelEntityRepository.updateByPrimaryKeySelective(item);
            });
        }
        // 新增
        if (statusMap.containsKey(AuditDomain.RecordStatus.create)) {
            List<RuleCondRelEntity> createList = statusMap.get(AuditDomain.RecordStatus.create);
            createList.forEach(item -> {
                // TODO: 唯一性校验
                ruleCondRelEntityRepository.insertSelective(item);
            });
        }
        return ruleCondRelEntityList;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public RuleCondRelEntity save(RuleCondRelEntity ruleCondRelEntity) {
        //保存规则关联条件
        if (ruleCondRelEntity.getRuleCondRelEntityId() == null) {
            ruleCondRelEntityRepository.insertSelective(ruleCondRelEntity);
        } else {
            ruleCondRelEntityRepository.updateOptional(ruleCondRelEntity,
                    RuleCondRelEntity.FIELD_RULE_ID,
                    RuleCondRelEntity.FIELD_RULE_CODE,
                    RuleCondRelEntity.FIELD_RULE_ENTITY_COND_ID,
                    RuleCondRelEntity.FIELD_TENANT_ID
            );
        }

        return ruleCondRelEntity;
    }
}
