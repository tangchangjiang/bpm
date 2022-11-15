package org.o2.business.process.management.app.introduce;

import io.choerodon.core.oauth.CustomUserDetails;
import io.choerodon.core.oauth.DetailsHelper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.hzero.boot.imported.app.service.BatchImportHandler;
import org.hzero.boot.imported.app.service.IBatchImportService;
import org.hzero.boot.imported.infra.validator.annotation.ImportService;
import org.hzero.mybatis.domian.Condition;
import org.hzero.mybatis.util.Sqls;
import org.o2.business.process.management.app.service.BusinessProcessRedisService;
import org.o2.business.process.management.domain.entity.BusinessNode;
import org.o2.business.process.management.domain.repository.BusinessNodeRepository;
import org.o2.core.helper.JsonHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author tangcj
 * @version V1.0
 * @date 2022/8/23 15:34
 */
@ImportService(templateCode = "O2BPM_BUSINESS_PROCESS", sheetIndex = 1)
@Slf4j
public class BusinessNodeImportServiceImpl extends BatchImportHandler implements IBatchImportService {

    private final BusinessNodeRepository businessNodeRepository;
    private final BusinessProcessRedisService businessProcessRedisService;

    public BusinessNodeImportServiceImpl(BusinessNodeRepository businessNodeRepository, BusinessProcessRedisService businessProcessRedisService) {
        this.businessNodeRepository = businessNodeRepository;
        this.businessProcessRedisService = businessProcessRedisService;
    }

    @Override
    public Boolean doImport(List<String> data) {
        if (CollectionUtils.isNotEmpty(data)) {
            CustomUserDetails customUserDetails = DetailsHelper.getUserDetails();
            Long tenantId = customUserDetails.getTenantId();
            List<BusinessNode> dataList = new ArrayList<>(data.size());
            data.forEach(d -> {
                BusinessNode bp = JsonHelper.stringToObject(d, BusinessNode.class);
                bp.setTenantId(tenantId);
                dataList.add(bp);
            });
            // 去重
            List<BusinessNode> importList = new ArrayList<>(dataList.stream().collect(Collectors.toMap(BusinessNode::getBeanId, Function.identity(),
                    (a, b) -> a)).values());
            // 是否已经存在业务流程
            List<BusinessNode> businessNodes = businessNodeRepository.selectByCondition(Condition.builder(BusinessNode.class)
                    .andWhere(Sqls.custom().andEqualTo(BusinessNode.FIELD_TENANT_ID, tenantId)
                            .andIn(BusinessNode.FIELD_BEAN_ID, importList.stream().map(BusinessNode::getBeanId).collect(Collectors.toList()))).build());

            Map<String, BusinessNode> nodeMap = businessNodes.stream().collect(Collectors.toMap(BusinessNode::getBeanId, Function.identity()));
            // 筛选更新数据
            List<BusinessNode> updateList = importList.stream().filter(b -> nodeMap.containsKey(b.getBeanId())).peek(b -> {
                b.setObjectVersionNumber(nodeMap.get(b.getBeanId()).getObjectVersionNumber());
                b.setBizNodeId(nodeMap.get(b.getBeanId()).getBizNodeId());
            }).collect(Collectors.toList());

            // 移除更新的数据
            List<String> beanIds = updateList.stream().map(BusinessNode::getBeanId).collect(Collectors.toList());
            importList.removeIf(b -> beanIds.contains(b.getBeanId()));
            // 更新仓储数据
            businessNodeRepository.batchInsert(importList);
            businessNodeRepository.batchUpdateByPrimaryKey(updateList);
            // 刷新redis缓存
            importList.addAll(updateList);
            businessProcessRedisService.batchUpdateNodeStatus(importList, tenantId);
        }
        return true;
    }
}
