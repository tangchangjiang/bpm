package org.o2.business.process.management.app.service.impl;

import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.hzero.mybatis.domian.Condition;
import org.hzero.mybatis.helper.UniqueHelper;
import org.hzero.mybatis.util.Sqls;
import org.o2.business.process.management.app.service.BizNodeParameterService;
import org.o2.business.process.management.domain.entity.BizNodeParameter;
import org.o2.business.process.management.domain.repository.BizNodeParameterRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;

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
    public List<BizNodeParameter> batchSave(List<BizNodeParameter> bizNodeParameterList, boolean insertFlag) {

        // 新增
        if (insertFlag) {
            bizNodeParameterList.forEach(item -> UniqueHelper.isUnique(item, BizNodeParameter.O2BPM_BIZ_NODE_PARAMETER_U1));
            bizNodeParameterRepository.batchInsertSelective(bizNodeParameterList);
        } else {
            // 更新
            bizNodeParameterList.forEach(item -> UniqueHelper.isUnique(item, BizNodeParameter.O2BPM_BIZ_NODE_PARAMETER_U1));
            bizNodeParameterRepository.batchUpdateByPrimaryKeySelective(bizNodeParameterList);
        }
        return bizNodeParameterList;
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public BizNodeParameter save(BizNodeParameter bizNodeParameter) {
        //保存业务节点参数表
        UniqueHelper.isUnique(bizNodeParameter, BizNodeParameter.O2BPM_BIZ_NODE_PARAMETER_U1);
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


    @Override
    public List<BizNodeParameter> getBizNodeParameterList(List<String> beanIdList, Long tenantId) {

        if (CollectionUtils.isEmpty(beanIdList)) {
            return Collections.emptyList();
        }
        return bizNodeParameterRepository.selectByCondition(Condition.builder(BizNodeParameter.class)
                .andWhere(Sqls.custom().andIn(BizNodeParameter.FIELD_BEAN_ID, beanIdList)
                        .andEqualTo(BizNodeParameter.FIELD_TENANT_ID, tenantId))
                .build());
    }
}
