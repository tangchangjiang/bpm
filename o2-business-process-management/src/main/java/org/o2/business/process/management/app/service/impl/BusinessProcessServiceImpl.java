package org.o2.business.process.management.app.service.impl;

import io.choerodon.mybatis.domain.AuditDomain;
import org.apache.commons.lang3.StringUtils;
import org.hzero.mybatis.domian.Condition;
import org.hzero.mybatis.helper.UniqueHelper;
import org.hzero.mybatis.util.Sqls;
import org.o2.business.process.management.api.dto.BusinessProcessQueryDTO;
import org.o2.business.process.management.app.service.BusinessProcessService;
import org.o2.business.process.management.domain.entity.BusinessProcess;
import org.o2.business.process.management.domain.repository.BusinessProcessRedisRepository;
import org.o2.business.process.management.domain.repository.BusinessProcessRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 业务流程定义表应用服务默认实现
 *
 * @author youlong.peng@hand-china.com
 * @date 2022-08-10 14:23:57
 */
@Service
public class BusinessProcessServiceImpl implements BusinessProcessService {
                                                                                
    private final BusinessProcessRepository businessProcessRepository;

    private final BusinessProcessRedisRepository businessProcessRedisRepository;

    public BusinessProcessServiceImpl(BusinessProcessRepository businessProcessRepository, BusinessProcessRedisRepository businessProcessRedisRepository) {
        this.businessProcessRepository = businessProcessRepository;
        this.businessProcessRedisRepository = businessProcessRedisRepository;
    }


    @Override
    public List<BusinessProcess> listBusinessProcess(BusinessProcessQueryDTO queryDTO) {
        return businessProcessRepository.selectByCondition(Condition.builder(BusinessProcess.class).andWhere(Sqls.custom()
                .andEqualTo(BusinessProcess.FIELD_TENANT_ID, queryDTO.getTenantId(), false)
                .andLikeRight(BusinessProcess.FIELD_DESCRIPTION, queryDTO.getDescription())
                .andEqualTo(BusinessProcess.FIELD_PROCESS_CODE, queryDTO.getProcessCode())
                .andEqualTo(BusinessProcess.FIELD_BUSINESS_TYPE_CODE, queryDTO.getBusinessTypeCode())
                .andEqualTo(BusinessProcess.FIELD_ENABLED_FLAG, queryDTO.getEnabledFlag())).build());
    }

    @Override
    @Deprecated()
    @Transactional(rollbackFor = Exception.class)
    public List<BusinessProcess> batchSave(List<BusinessProcess> businessProcessList) {
        Map<AuditDomain.RecordStatus, List<BusinessProcess>> statusMap = businessProcessList.stream().collect(Collectors.groupingBy(BusinessProcess::get_status));
        // 删除
        if (statusMap.containsKey(AuditDomain.RecordStatus.delete)) {
            List<BusinessProcess> deleteList = statusMap.get(AuditDomain.RecordStatus.delete);
            businessProcessRepository.batchDeleteByPrimaryKey(deleteList);
        }
        // 更新
        if (statusMap.containsKey(AuditDomain.RecordStatus.update)) {
            List<BusinessProcess> updateList = statusMap.get(AuditDomain.RecordStatus.update);
            updateList.forEach(item -> {
                // TODO: 唯一性校验
                UniqueHelper.valid(item,BusinessProcess.O2BPM_BUSINESS_PROCESS_U1);
                businessProcessRepository.updateByPrimaryKeySelective(item);
            });
        }
        // 新增
        if (statusMap.containsKey(AuditDomain.RecordStatus.create)) {
            List<BusinessProcess> createList = statusMap.get(AuditDomain.RecordStatus.create);
            createList.forEach(item -> {
                // TODO: 唯一性校验
                UniqueHelper.valid(item,BusinessProcess.O2BPM_BUSINESS_PROCESS_U1);
                businessProcessRepository.insertSelective(item);
            });
        }
        return businessProcessList;
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public BusinessProcess save(BusinessProcess businessProcess) {
        //保存业务流程定义表
        UniqueHelper.isUnique(businessProcess,BusinessProcess.O2BPM_BUSINESS_PROCESS_U1);
        if (businessProcess.getBizProcessId() == null) {
            businessProcessRepository.insertSelective(businessProcess);
        } else {
            businessProcessRepository.updateOptional(businessProcess,
                    BusinessProcess.FIELD_PROCESS_CODE,
                    BusinessProcess.FIELD_DESCRIPTION,
                    BusinessProcess.FIELD_ENABLED_FLAG,
                    BusinessProcess.FIELD_PROCESS_JSON,
                    BusinessProcess.FIELD_VIEW_JSON,
                    BusinessProcess.FIELD_BUSINESS_TYPE_CODE,
                    BusinessProcess.FIELD_TENANT_ID
            );
        }
        if(StringUtils.isNotBlank(businessProcess.getProcessJson())){
            businessProcessRedisRepository.updateProcessConfig(businessProcess.getProcessCode(), businessProcess.getProcessJson(), businessProcess.getTenantId());
        }
        return businessProcess;
    }
}
