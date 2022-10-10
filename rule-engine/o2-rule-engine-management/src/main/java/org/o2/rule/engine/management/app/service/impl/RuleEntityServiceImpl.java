package org.o2.rule.engine.management.app.service.impl;

import org.hzero.mybatis.helper.UniqueHelper;
import org.o2.rule.engine.management.app.service.RuleEntityService;
import org.o2.rule.engine.management.domain.entity.RuleEntity;
import org.o2.rule.engine.management.domain.repository.RuleEntityRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import io.choerodon.mybatis.domain.AuditDomain;

/**
 * 规则实体应用服务默认实现
 *
 * @author xiang.zhao@hand-china.com 2022-10-10 17:46:13
 */
@Service
public class RuleEntityServiceImpl implements RuleEntityService {

    private final RuleEntityRepository ruleEntityRepository;

    public RuleEntityServiceImpl(RuleEntityRepository ruleEntityRepository) {
        this.ruleEntityRepository = ruleEntityRepository;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<RuleEntity> batchSave(List<RuleEntity> ruleEntityList) {
        Map<AuditDomain.RecordStatus, List<RuleEntity>> statusMap = ruleEntityList.stream().collect(Collectors.groupingBy(RuleEntity::get_status));
        // 删除
        if (statusMap.containsKey(AuditDomain.RecordStatus.delete)) {
            List<RuleEntity> deleteList = statusMap.get(AuditDomain.RecordStatus.delete);
            ruleEntityRepository.batchDeleteByPrimaryKey(deleteList);
        }
        // 更新
        if (statusMap.containsKey(AuditDomain.RecordStatus.update)) {
            List<RuleEntity> updateList = statusMap.get(AuditDomain.RecordStatus.update);
            updateList.forEach(item -> {
                // TODO: 唯一性校验
                UniqueHelper.valid(item, RuleEntity.O2RE_RULE_ENTITY_U1);
                ruleEntityRepository.updateByPrimaryKeySelective(item);
            });
        }
        // 新增
        if (statusMap.containsKey(AuditDomain.RecordStatus.create)) {
            List<RuleEntity> createList = statusMap.get(AuditDomain.RecordStatus.create);
            createList.forEach(item -> {
                // TODO: 唯一性校验
                UniqueHelper.valid(item, RuleEntity.O2RE_RULE_ENTITY_U1);
                ruleEntityRepository.insertSelective(item);
            });
        }
        return ruleEntityList;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public RuleEntity save(RuleEntity ruleEntity) {
        //保存规则实体
        UniqueHelper.valid(ruleEntity, RuleEntity.O2RE_RULE_ENTITY_U1);
        if (ruleEntity.getRuleEntityId() == null) {
            ruleEntityRepository.insertSelective(ruleEntity);
        } else {
            ruleEntityRepository.updateOptional(ruleEntity,
                    RuleEntity.FIELD_RULE_ENTITY_CODE,
                    RuleEntity.FIELD_RULE_ENTITY_NAME,
                    RuleEntity.FIELD_RULE_ENTITY_ALIAS,
                    RuleEntity.FIELD_DESCRIPTION,
                    RuleEntity.FIELD_TENANT_ID
            );
        }

        return ruleEntity;
    }
}
