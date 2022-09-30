package org.o2.business.process.management.app.introduce;

import io.choerodon.core.oauth.CustomUserDetails;
import io.choerodon.core.oauth.DetailsHelper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.hzero.boot.imported.app.service.BatchImportHandler;
import org.hzero.boot.imported.app.service.IBatchImportService;
import org.hzero.boot.imported.infra.validator.annotation.ImportService;
import org.hzero.core.base.BaseConstants;
import org.hzero.mybatis.domian.Condition;
import org.hzero.mybatis.util.Sqls;
import org.o2.business.process.management.domain.entity.BizNodeParameter;
import org.o2.business.process.management.domain.repository.BizNodeParameterRepository;
import org.o2.core.helper.JsonHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author tangcj
 * @version V1.0
 * @date 2022/8/23 15:44
 */
@ImportService(templateCode = "O2BPM_BUSINESS_PROCESS", sheetIndex = 2)
@Slf4j
public class BizNodeParameterImportService extends BatchImportHandler implements IBatchImportService {

    private final BizNodeParameterRepository bizNodeParameterRepository;

    public BizNodeParameterImportService(BizNodeParameterRepository bizNodeParameterRepository) {
        this.bizNodeParameterRepository = bizNodeParameterRepository;
    }

    @Override
    public Boolean doImport(List<String> data) {
        if (CollectionUtils.isNotEmpty(data)) {
            CustomUserDetails customUserDetails = DetailsHelper.getUserDetails();
            Long tenantId = customUserDetails.getTenantId();
            List<BizNodeParameter> dataList = new ArrayList<>(data.size());
            data.forEach(d ->{
                BizNodeParameter bp = JsonHelper.stringToObject(d, BizNodeParameter.class);
                bp.setTenantId(tenantId);
                dataList.add(bp);
            });
            // 去重
            List<BizNodeParameter> importList = new ArrayList<>(dataList.stream().collect(Collectors.toMap(np -> getMapKey(np.getBeanId(), np.getParamCode()), Function.identity(), (a,b) -> a)).values());
            // 是否已经存在业务流程节点参数
            List<BizNodeParameter> nodeParameters = bizNodeParameterRepository.selectByCondition(Condition.builder(BizNodeParameter.class)
                    .andWhere(Sqls.custom().andEqualTo(BizNodeParameter.FIELD_TENANT_ID, tenantId)
                            .andIn(BizNodeParameter.FIELD_BEAN_ID, importList.stream().map(BizNodeParameter::getBeanId).collect(Collectors.toList()))
                    .andIn(BizNodeParameter.FIELD_PARAM_CODE, importList.stream().map(BizNodeParameter::getParamCode).collect(Collectors.toList()))).build());

            Map<String, BizNodeParameter> nodeParamMap = nodeParameters.stream().collect(Collectors.toMap(p -> getMapKey(p.getBeanId(), p.getParamCode()), Function.identity()));
            // 筛选更新数据
            List<BizNodeParameter> updateList = importList.stream().filter(p -> nodeParamMap.containsKey(getMapKey(p.getBeanId(), p.getParamCode()))).peek(p -> {
                p.setObjectVersionNumber(nodeParamMap.get(getMapKey(p.getBeanId(), p.getParamCode())).getObjectVersionNumber());
                p.setBizNodeParameterId(nodeParamMap.get(getMapKey(p.getBeanId(), p.getParamCode())).getBizNodeParameterId());
            }).collect(Collectors.toList());

            // 移除更新的数据
            List<String> beanIds = updateList.stream().map(p -> getMapKey(p.getBeanId(), p.getParamCode())).collect(Collectors.toList());
            importList.removeIf(p -> beanIds.contains(getMapKey(p.getBeanId(), p.getParamCode())));
            // 更新仓储数据
            bizNodeParameterRepository.batchInsert(importList);
            bizNodeParameterRepository.batchUpdateByPrimaryKey(updateList);
        }
        return true;
    }


    protected String getMapKey(String... keys){
        StringBuilder sb = new StringBuilder();
        if(StringUtils.isNoneBlank(keys)){
            for(String key : keys){
                sb.append(key).append(BaseConstants.Symbol.COLON);
            }
            return sb.substring(0, sb.length() - 1);
        }
        return null;
    }

}
