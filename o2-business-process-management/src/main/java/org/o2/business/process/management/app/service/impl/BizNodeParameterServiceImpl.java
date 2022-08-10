package org.o2.business.process.management.app.service.impl;

import lombok.RequiredArgsConstructor;
import org.o2.business.process.management.domain.entity.BizNodeParameter;
import org.o2.business.process.management.domain.repository.BizNodeParameterRepository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.stream.Collectors;
import io.choerodon.mybatis.domain.AuditDomain;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.o2.business.process.management.app.service.BizNodeParameterService;
import java.util.List;

import org.hzero.mybatis.helper.UniqueHelper;

/**
 * 业务节点参数表应用服务默认实现
 *
 * @author zhilin.ren@hand-china.com 2022-08-10 14:31:01
 */
@Service
@RequiredArgsConstructor
public class BizNodeParameterServiceImpl implements BizNodeParameterService {

    private final BizNodeParameterRepository bizNodeParameterRepository;
    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<BizNodeParameter> batchSave(List<BizNodeParameter> bizNodeParameterList) {
        Map<AuditDomain.RecordStatus, List<BizNodeParameter>> statusMap = bizNodeParameterList.stream().collect(Collectors.groupingBy(BizNodeParameter::get_status));
        // 删除
        if (statusMap.containsKey(AuditDomain.RecordStatus.delete)) {
            List<BizNodeParameter> deleteList = statusMap.get(AuditDomain.RecordStatus.delete);
            bizNodeParameterRepository.batchDeleteByPrimaryKey(deleteList);
        }
        // 更新
        if (statusMap.containsKey(AuditDomain.RecordStatus.update)) {
            List<BizNodeParameter> updateList = statusMap.get(AuditDomain.RecordStatus.update);
            updateList.forEach(item -> {
                // TODO: 唯一性校验
                UniqueHelper.valid(item,BizNodeParameter.O2BPM_BIZ_NODE_PARAMETER_U1);
                bizNodeParameterRepository.updateByPrimaryKeySelective(item);
            });
        }
        // 新增
        if (statusMap.containsKey(AuditDomain.RecordStatus.create)) {
            List<BizNodeParameter> createList = statusMap.get(AuditDomain.RecordStatus.create);
            createList.forEach(item -> {
                // TODO: 唯一性校验
                UniqueHelper.valid(item,BizNodeParameter.O2BPM_BIZ_NODE_PARAMETER_U1);
                bizNodeParameterRepository.insertSelective(item);
            });
        }
        return bizNodeParameterList;
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public BizNodeParameter save(BizNodeParameter bizNodeParameter) {
        //保存业务节点参数表
        UniqueHelper.valid(bizNodeParameter,BizNodeParameter.O2BPM_BIZ_NODE_PARAMETER_U1);
        if (bizNodeParameter.getBizNodeParameterId() == null) {
            bizNodeParameterRepository.insertSelective(bizNodeParameter);
        } else {
            bizNodeParameterRepository.updateOptional(bizNodeParameter,
                    BizNodeParameter.FIELD_PARAM_CODE,
                    BizNodeParameter.FIELD_PARAM_NAME,
                    BizNodeParameter.FIELD_BEAN_ID,
                    BizNodeParameter.FIELD_PARAM_FORMAT_CODE,
                    BizNodeParameter.FIELD_PARAM_EDIT_TYPE_CODE,
                    BizNodeParameter.FIELD_NOTNULL_FLAG,
                    BizNodeParameter.FIELD_BUSINESS_MODEL,
                    BizNodeParameter.FIELD_VALUE_FILED_FROM,
                    BizNodeParameter.FIELD_VALUE_FILED_TO,
                    BizNodeParameter.FIELD_SHOW_FLAG,
                    BizNodeParameter.FIELD_ENABLED_FLAG,
                    BizNodeParameter.FIELD_DEFAULT_VALUE,
                    BizNodeParameter.FIELD_DEFAULT_MEANING,
                    BizNodeParameter.FIELD_PARENT_FIELD,
                    BizNodeParameter.FIELD_DEFAULT_VALUE_TYPE,
                    BizNodeParameter.FIELD_TENANT_ID
            );
        }

        return bizNodeParameter;
    }
}
