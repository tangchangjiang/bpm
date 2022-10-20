package org.o2.business.process.management.app.introduce;

import io.choerodon.core.oauth.CustomUserDetails;
import io.choerodon.core.oauth.DetailsHelper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.hzero.boot.imported.app.service.BatchImportHandler;
import org.hzero.boot.imported.app.service.IBatchImportService;
import org.hzero.boot.imported.infra.validator.annotation.ImportService;
import org.hzero.mybatis.domian.Condition;
import org.hzero.mybatis.util.Sqls;
import org.o2.business.process.management.app.service.BusinessProcessRedisService;
import org.o2.business.process.management.domain.entity.BusinessProcess;
import org.o2.business.process.management.domain.repository.BusinessProcessRepository;
import org.o2.core.helper.JsonHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author tangcj
 * @version V1.0
 * @date 2022/8/23 14:06
 */
@ImportService(templateCode = "O2BPM_BUSINESS_PROCESS", sheetIndex = 0)
@Slf4j
public class BusinessProcessImportServiceImpl extends BatchImportHandler implements IBatchImportService {

    private final BusinessProcessRepository businessProcessRepository;
    private final BusinessProcessRedisService businessProcessRedisService;

    public BusinessProcessImportServiceImpl(BusinessProcessRepository businessProcessRepository, BusinessProcessRedisService businessProcessRedisService) {
        this.businessProcessRepository = businessProcessRepository;
        this.businessProcessRedisService = businessProcessRedisService;
    }

    @Override
    public Boolean doImport(List<String> data) {
        if (CollectionUtils.isNotEmpty(data)) {
            CustomUserDetails customUserDetails = DetailsHelper.getUserDetails();
            Long tenantId = customUserDetails.getTenantId();
            List<BusinessProcess> importList = new ArrayList<>(data.size());
            data.forEach(d ->{
                BusinessProcess bp = JsonHelper.stringToObject(d, BusinessProcess.class);
                bp.setTenantId(tenantId);
                importList.add(bp);
            });
            // 是否已经存在业务流程
            List<BusinessProcess> processList = businessProcessRepository.selectByCondition(Condition.builder(BusinessProcess.class)
                    .andWhere(Sqls.custom().andEqualTo(BusinessProcess.FIELD_TENANT_ID, tenantId)
                    .andIn(BusinessProcess.FIELD_PROCESS_CODE, importList.stream().map(BusinessProcess::getProcessCode).collect(Collectors.toList()))).build());

            Map<String, BusinessProcess> processMap = processList.stream().collect(Collectors.toMap(BusinessProcess::getProcessCode, Function.identity()));
            //筛选更新数据
            List<BusinessProcess> updateList = importList.stream().filter(b -> processMap.containsKey(b.getProcessCode())).peek(b -> {
                b.setObjectVersionNumber(processMap.get(b.getProcessCode()).getObjectVersionNumber());
                b.setBizProcessId(processMap.get(b.getProcessCode()).getBizProcessId());
            }).collect(Collectors.toList());

            //移除更新的数据
            List<String> processCodes = updateList.stream().map(BusinessProcess::getProcessCode).collect(Collectors.toList());
            importList.removeIf(b -> processCodes.contains(b.getProcessCode()));

            businessProcessRepository.batchInsert(importList);
            businessProcessRepository.batchUpdateByPrimaryKey(updateList);

            importList.addAll(updateList);
            businessProcessRedisService.batchUpdateProcessConfig(importList.stream().filter(i -> StringUtils.isNotBlank(i.getProcessJson())).collect(Collectors.toList()), tenantId);
        }
        return true;
    }
}
