package org.o2.business.process.management.app.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.choerodon.core.exception.CommonException;
import org.apache.commons.lang3.StringUtils;
import org.o2.business.process.management.app.service.BusinessProcessRedisService;
import org.o2.business.process.management.domain.entity.BusinessNode;
import org.o2.business.process.management.domain.entity.BusinessProcess;
import org.o2.business.process.management.domain.repository.BusinessProcessRedisRepository;
import org.o2.core.O2CoreConstants;
import org.o2.process.domain.engine.BpmnModel;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author tangcj
 * @version V1.0
 * @date 2022/8/10 15:35
 */
@Service
public class BusinessProcessRedisServiceImpl implements BusinessProcessRedisService {

    private final BusinessProcessRedisRepository businessProcessRedisRepository;

    private final ObjectMapper objectMapper;

    public BusinessProcessRedisServiceImpl(BusinessProcessRedisRepository businessProcessRedisRepository, ObjectMapper objectMapper) {
        this.businessProcessRedisRepository = businessProcessRedisRepository;
        this.objectMapper = objectMapper;
    }

    @Override
    public BpmnModel getBusinessProcessConfig(String processCode, Long tenantId) {
        String processConfigStr = businessProcessRedisRepository.getBusinessProcessConfig(processCode, tenantId);
        // 0租户兜底逻辑
        if(StringUtils.isBlank(processConfigStr)){
            tenantId = O2CoreConstants.tenantId;
            processConfigStr = businessProcessRedisRepository.getBusinessProcessConfig(processCode, tenantId);

            if(StringUtils.isBlank(processConfigStr)){
                return null;
            }
        }

        BpmnModel processContext;
        try {
            processContext = objectMapper.readValue(processConfigStr, BpmnModel.class);
        } catch (JsonProcessingException e) {
            throw new CommonException(e);
        }
        return processContext;
    }

    @Override
    public void batchUpdateNodeStatus(List<BusinessNode> businessNodes, Long tenantId) {
        Map<String, String> detailMap = businessNodes.stream().collect(Collectors.toMap(BusinessNode::getBeanId, a -> String.valueOf(a.getEnabledFlag())));
        businessProcessRedisRepository.batchUpdateNodeStatus(tenantId, detailMap);
    }

    @Override
    public void batchUpdateProcessConfig(List<BusinessProcess> processList, Long tenantId) {
        Map<String, String> detailMap = processList.stream().collect(Collectors.toMap(BusinessProcess::getProcessCode, BusinessProcess::getProcessJson));
        businessProcessRedisRepository.batchUpdateProcessConfig(tenantId, detailMap);
    }

    @Override
    public Long getProcessLastUpdateTime(String processCode, Long tenantId) {
        String lastModifiedTimeValue = businessProcessRedisRepository.getProcessLastUpdateTime(processCode, tenantId);
        return StringUtils.isEmpty(lastModifiedTimeValue) ? System.currentTimeMillis() : Long.parseLong(lastModifiedTimeValue);
    }


}
