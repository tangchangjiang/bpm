package org.o2.business.process.management.app.service.impl;

import io.choerodon.mybatis.domain.AuditDomain;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.hzero.mybatis.domian.Condition;
import org.hzero.mybatis.helper.UniqueHelper;
import org.hzero.mybatis.util.Sqls;
import org.o2.business.process.management.api.dto.BusinessExportDTO;
import org.o2.business.process.management.api.dto.BusinessProcessQueryDTO;
import org.o2.business.process.management.api.vo.BusinessExportVO;
import org.o2.business.process.management.api.vo.BusinessNodeExportVO;
import org.o2.business.process.management.app.service.BusinessProcessService;
import org.o2.business.process.management.domain.entity.BusinessProcess;
import org.o2.business.process.management.domain.repository.BusinessNodeRepository;
import org.o2.business.process.management.domain.repository.BusinessProcessRedisRepository;
import org.o2.business.process.management.domain.repository.BusinessProcessRepository;
import org.o2.business.process.management.infra.convert.ViewJsonConvert;
import org.o2.process.domain.engine.BpmnModel;
import org.o2.process.domain.engine.definition.Activity.ServiceTask;
import org.o2.process.domain.engine.definition.BaseElement;
import org.o2.process.domain.engine.definition.flow.ConditionalFlow;
import org.o2.process.domain.engine.process.preruntime.validator.BpmnModelValidator;
import org.o2.process.domain.infra.ProcessEngineConstants;
import org.o2.rule.engine.client.RuleEngineManagementClient;
import org.o2.user.helper.IamUserHelper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
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

    private final BusinessNodeRepository businessNodeRepository;

    private final BusinessProcessRedisRepository businessProcessRedisRepository;

    private final RuleEngineManagementClient ruleEngineManagementClient;

    public BusinessProcessServiceImpl(BusinessProcessRepository businessProcessRepository, BusinessNodeRepository businessNodeRepository, BusinessProcessRedisRepository businessProcessRedisRepository, RuleEngineManagementClient ruleEngineManagementClient) {
        this.businessProcessRepository = businessProcessRepository;
        this.businessNodeRepository = businessNodeRepository;
        this.businessProcessRedisRepository = businessProcessRedisRepository;
        this.ruleEngineManagementClient = ruleEngineManagementClient;
    }


    @Override
    public List<BusinessProcess> listBusinessProcess(BusinessProcessQueryDTO queryDTO) {
        List<BusinessProcess> result = businessProcessRepository.selectByCondition(Condition.builder(BusinessProcess.class).andWhere(Sqls.custom()
                .andEqualTo(BusinessProcess.FIELD_TENANT_ID, queryDTO.getTenantId(), false)
                .andLike(BusinessProcess.FIELD_DESCRIPTION, queryDTO.getDescription(), true)
                .andEqualTo(BusinessProcess.FIELD_PROCESS_CODE, queryDTO.getProcessCode(), true)
                .andEqualTo(BusinessProcess.FIELD_BUSINESS_TYPE_CODE, queryDTO.getBusinessTypeCode(), true)
                .andEqualTo(BusinessProcess.FIELD_ENABLED_FLAG, queryDTO.getEnabledFlag(), true)).build());
        for(BusinessProcess process : result){
            process.setCreatedOperator(IamUserHelper.getRealName(process.getCreatedBy().toString()));
            process.setUpdatedOperator(IamUserHelper.getRealName(process.getLastUpdatedBy().toString()));
        }
        return result;
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

        Set<String> ruleCodes = new HashSet<>();

        if(businessProcess.getBizProcessId() != null){
            // 通过viewJson转换成processJson
            buildProcessJson(businessProcess, ruleCodes);
        }

        //保存业务流程定义表
        UniqueHelper.isUnique(businessProcess, BusinessProcess.O2BPM_BUSINESS_PROCESS_U1);
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
        // 加载到缓存中
        businessProcessRedisRepository.updateProcessConfig(businessProcess.getProcessCode(), businessProcess.getProcessJson(), businessProcess.getTenantId());
        // 发布规则
        if (CollectionUtils.isNotEmpty(ruleCodes)){
            ruleEngineManagementClient.useRule(businessProcess.getTenantId(), new ArrayList<>(ruleCodes));
        }
        return businessProcess;
    }

    protected void buildProcessJson(BusinessProcess businessProcess, Set<String> ruleCodes) {
        List<BaseElement> baseElements = ViewJsonConvert.viewJsonConvert(businessProcess.getViewJson());
        BpmnModel bpmnModel = new BpmnModel();
        bpmnModel.setFlowElements(baseElements);
        bpmnModel.setProcessCode(businessProcess.getProcessCode());
        bpmnModel.setEnabledFlag(businessProcess.getEnabledFlag());
        bpmnModel.setProcessName(businessProcess.getDescription());
        bpmnModel.setTenantId(businessProcess.getTenantId());

        // 合法性校验
        BpmnModelValidator.validate(bpmnModel);

        bpmnModel.getFlowElements().stream().filter(f -> ProcessEngineConstants.FlowElementType.CONDITIONAL_FLOW.equals(f.getType()))
                .map(ConditionalFlow.class::cast).forEach(c -> ruleCodes.add(c.getRuleCode()));

        businessProcess.setProcessJson(ViewJsonConvert.bpmnToJson(bpmnModel));
    }

    @Override
    public List<BusinessExportVO> businessExport(BusinessExportDTO businessExportDTO) {
        List<BusinessExportVO> businessExportList = businessProcessRepository.listBusinessForExport(businessExportDTO);
        businessExportList.forEach(e -> e.setBpmnModel(StringUtils.isBlank(e.getProcessJson()) ? new BpmnModel() : ViewJsonConvert.processJsonConvert(e.getProcessJson())));
        Set<String> beanIds = businessExportList.stream().flatMap(b -> b.getBpmnModel().getServiceTask().stream())
                .map(ServiceTask::getBeanId).collect(Collectors.toSet());
        Map<String, BusinessNodeExportVO> nodeExportMap = businessNodeRepository.listNodeForExport(beanIds, businessExportDTO.getTenantId())
                .stream().collect(Collectors.toMap(BusinessNodeExportVO::getBeanId, Function.identity()));
        businessExportList.forEach(e -> {
            List<BusinessNodeExportVO> businessNodeExportList = new ArrayList<>();
            e.getBpmnModel().getServiceTask().forEach(node -> businessNodeExportList.add(nodeExportMap.get(node.getBeanId())));
            e.setNodeExportList(businessNodeExportList);
        });
        return businessExportList;
    }
}
