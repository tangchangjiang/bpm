package org.o2.business.process.management.app.service.impl;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.o2.business.process.management.app.service.BusinessProcessRedisService;
import org.o2.business.process.management.domain.BusinessProcessContext;
import org.o2.business.process.management.domain.BusinessProcessNodeDO;
import org.o2.business.process.management.domain.repository.BusinessProcessRedisRepository;
import org.o2.core.O2CoreConstants;
import org.o2.core.helper.JsonHelper;
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

    public BusinessProcessRedisServiceImpl(BusinessProcessRedisRepository businessProcessRedisRepository) {
        this.businessProcessRedisRepository = businessProcessRedisRepository;
    }

    @Override
    public BusinessProcessContext getBusinessProcessConfig(String processCode, Long tenantId) {
        String processConfigStr = businessProcessRedisRepository.getBusinessProcessConfig(processCode, tenantId);
        // 0租户兜底逻辑
        if(StringUtils.isBlank(processConfigStr)){
            tenantId = O2CoreConstants.tenantId;
            processConfigStr = businessProcessRedisRepository.getBusinessProcessConfig(processCode, tenantId);

            if(StringUtils.isBlank(processConfigStr)){
                return null;
            }
        }

        BusinessProcessContext processContext = JsonHelper.stringToObject(processConfigStr, BusinessProcessContext.class);
        if(CollectionUtils.isNotEmpty(processContext.getAllNodeAction())){
            List<String> processNodes = processContext.getAllNodeAction().stream().map(BusinessProcessNodeDO::getBeanId).collect(Collectors.toList());
            Map<String, String> map = businessProcessRedisRepository.listNodeStatus(processNodes, tenantId);
            // 注意自动拆箱 空指针问题
            processContext.getAllNodeAction().removeIf(node -> !map.containsKey(node.getBeanId()) || !String.valueOf(O2CoreConstants.BooleanFlag.ENABLE).equals(map.get(node.getBeanId())));
        }
        processContext.setTenantId(tenantId);
        return processContext;
    }
}
