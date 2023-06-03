package org.o2.metadata.console.app.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.hzero.core.base.BaseAppService;
import org.hzero.core.base.BaseConstants;
import org.hzero.mybatis.domian.Condition;
import org.hzero.mybatis.util.Sqls;
import org.o2.core.exception.O2CommonException;
import org.o2.core.helper.TransactionalHelper;
import org.o2.core.response.BatchOperateResponse;
import org.o2.metadata.console.api.co.ResponseCO;
import org.o2.metadata.console.api.co.SystemParameterCO;
import org.o2.metadata.console.api.dto.SystemParameterQueryInnerDTO;
import org.o2.metadata.console.app.service.LovAdapterService;
import org.o2.metadata.console.app.service.SysParamService;
import org.o2.metadata.console.infra.constant.SystemParameterConstants;
import org.o2.metadata.console.infra.convertor.SysParameterConverter;
import org.o2.metadata.console.infra.convertor.SystemParamValueConverter;
import org.o2.metadata.console.infra.entity.SystemParamValue;
import org.o2.metadata.console.infra.entity.SystemParameter;
import org.o2.metadata.console.infra.redis.SystemParameterRedis;
import org.o2.metadata.console.infra.repository.SystemParamValueRepository;
import org.o2.metadata.console.infra.repository.SystemParameterRepository;
import org.o2.metadata.domain.systemparameter.domain.SystemParameterDO;
import org.o2.metadata.domain.systemparameter.service.SystemParameterDomainService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author yong.nie@hand-china.com
 * @date 2020/6/30 21:26
 **/
@Slf4j
@Service
public class SysParamServiceImpl extends BaseAppService implements SysParamService {

    private final SystemParameterDomainService systemParameterDomainService;
    private final SystemParameterRepository systemParameterRepository;
    private final SystemParameterRedis systemParameterRedis;
    private final SystemParamValueRepository systemParamValueRepository;
    private final TransactionalHelper transactionalHelper;
    private final LovAdapterService lovAdapterService;

    public SysParamServiceImpl(SystemParameterDomainService systemParameterDomainService,
                               SystemParameterRepository systemParameterRepository,
                               SystemParameterRedis systemParameterRedis, SystemParamValueRepository systemParamValueRepository,
                               TransactionalHelper transactionalHelper,
                               LovAdapterService lovAdapterService) {

        this.systemParameterDomainService = systemParameterDomainService;
        this.systemParameterRepository = systemParameterRepository;
        this.systemParameterRedis = systemParameterRedis;
        this.systemParamValueRepository = systemParamValueRepository;
        this.transactionalHelper = transactionalHelper;
        this.lovAdapterService = lovAdapterService;
    }

    @Override
    public SystemParameterCO getSystemParameter(String paramCode, Long tenantId) {
        SystemParameterDO systemParameterDO = systemParameterDomainService.getSystemParameter(paramCode, tenantId);
        return SysParameterConverter.doToCoObject(systemParameterDO);
    }

    @Override
    public List<SystemParameterCO> listSystemParameters(List<String> paramCodes, Long tenantId) {
        return SysParameterConverter.doToCoListObjects(systemParameterDomainService.listSystemParameters(paramCodes, tenantId));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveSystemParameter(SystemParameter systemParameter, Long tenantId) {
        // 编码唯一性
        final Sqls sqls = Sqls.custom();
        sqls.andEqualTo(SystemParameter.FIELD_PARAM_CODE, systemParameter.getParamCode());
        sqls.andEqualTo(SystemParameter.FIELD_TENANT_ID, tenantId);
        int number = systemParameterRepository.selectCountByCondition(
                Condition.builder(SystemParameter.class).andWhere(sqls).build());
        if (number > 0) {
            throw new O2CommonException(null, SystemParameterConstants.ErrorCode.ERROR_SYSTEM_PARAM_CODE_UNIQUE, SystemParameterConstants.ErrorCode.ERROR_SYSTEM_PARAM_CODE_UNIQUE);
        }
        systemParameterRepository.insertSelective(systemParameter);
        systemParameterRedis.updateToRedis(systemParameter, tenantId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateSystemParameter(SystemParameter systemParameter, Long tenantId) {
        systemParameterRedis.extraOperate(systemParameter.getParamCode(), systemParameter.getTenantId());
        systemParameterRepository.updateByPrimaryKey(systemParameter);
        systemParameterRedis.updateToRedis(systemParameter, systemParameter.getTenantId());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResponseCO updateSysParameter(SystemParameterQueryInnerDTO systemParameterQueryInnerDTO, Long tenantId) {
        ResponseCO co = new ResponseCO();
        SystemParameter  select = new SystemParameter();
        select.setParamCode(systemParameterQueryInnerDTO.getParamCode());
        select.setTenantId(tenantId);
        List<SystemParameter>  systemParameterList = systemParameterRepository.select(select);
        if (CollectionUtils.isEmpty(systemParameterList) || systemParameterList.size() > 1) {
            co.setSuccess(BaseConstants.FIELD_FAILED);
            return co;
        }

        SystemParameter systemParameter = systemParameterList.get(0);
        SystemParamValue selectValue = new SystemParamValue();
        selectValue.setParamId(systemParameter.getParamId());
        selectValue.setTenantId(tenantId);
        List<SystemParamValue> systemParamValueList =  systemParamValueRepository.select(selectValue);
        if (CollectionUtils.isEmpty(systemParamValueList)) {
            co.setSuccess(BaseConstants.FIELD_FAILED);
            return co;
        }

        Map<String, String> map = systemParameterQueryInnerDTO.getHashMap();
        for (SystemParamValue value : systemParamValueList) {
            String  paramValue = map.get(value.getParamKey());
            if (StringUtils.isEmpty(paramValue)) {
                continue;
            }
            value.setParamValue(paramValue);
        }

        systemParamValueRepository.batchUpdateByPrimaryKeySelective(systemParamValueList);
        systemParameterRedis.updateToRedis(systemParameter, tenantId);
        co.setSuccess(BaseConstants.FIELD_SUCCESS);
        return co;
    }

    @Override
    public void copySysParam(Long paramId, Long tenantId) {
        SystemParameter sysParam = queryAndVerifyBeforeCopy(paramId, Collections.singletonList(tenantId));
        copySystemParam(sysParam, tenantId);
    }

    @Override
    public BatchOperateResponse copySysParamOfSite(Long paramId, List<Long> tenantIds) {
        SystemParameter sysParam = queryAndVerifyBeforeCopy(paramId, tenantIds);
        BatchOperateResponse batchOperateResponse = new BatchOperateResponse();
        List<Long> success = new ArrayList<>(tenantIds.size());
        Map<Long, String> errorMap = new HashMap<>();
        for (Long tenantId : tenantIds) {
            try {
                copySystemParam(sysParam, tenantId);
                success.add(tenantId);
            } catch (Exception ex) {
                errorMap.put(tenantId, getMessage(ex.getMessage()));
            }
        }
        batchOperateResponse.generateResponse(success.size());
        if (MapUtils.isEmpty(errorMap)) {
            return batchOperateResponse;
        }
        // 设置错误信息
        try {
            List<String> tenant = batchOperateResponse.getErrors().stream().map(BatchOperateResponse.Error::getKey).collect(Collectors.toList());
            Map<String, String> params = new HashMap<>();
            params.put("tenantIdList", String.join(BaseConstants.Symbol.COMMA, tenant));
            List<Map<String, Object>> maps = lovAdapterService.queryLovValueMeaning(BaseConstants.DEFAULT_TENANT_ID, "O2MD.TENANT", params);
            Map<String, String> tenantMap = maps.stream().collect(Collectors.toMap(m -> String.valueOf(m.get("tenantId")), m -> String.valueOf(m.get("tenantName")), (s1, s2) -> s2));
            errorMap.forEach((id, errMsg) -> {
                String tenantName = tenantMap.get(id.toString());
                // 如果查询到租户名称，则异常信息key设置为租户名称
                if (StringUtils.isNotBlank(tenantName)) {
                    batchOperateResponse.addErrorMessage(tenantName, errMsg);
                } else {
                    batchOperateResponse.addErrorMessage(id.toString(), errMsg);
                }
            });
        } catch (Exception ex) {
            log.error("query tenantName failed", ex);
            errorMap.forEach((id, errMsg) -> batchOperateResponse.addErrorMessage(id.toString(), errMsg));
        }

        return batchOperateResponse;
    }

    /**
     * 复制前校验
     *
     * @param paramId   系统参数Id
     * @param tenantIds 租户Id
     * @return 系统参数
     */
    protected SystemParameter queryAndVerifyBeforeCopy(Long paramId, List<Long> tenantIds) {
        if (null == paramId) {
            throw new O2CommonException(null, SystemParameterConstants.ErrorCode.ERROR_SYSTEM_PARAM_NO_SELECT);
        }
        if (CollectionUtils.isEmpty(tenantIds)) {
            throw new O2CommonException(null, SystemParameterConstants.ErrorCode.ERROR_TENANT_NO_SELECT);
        }
        SystemParameter sysParam = systemParameterRepository.selectByPrimaryKey(paramId);
        if (Objects.isNull(sysParam)) {
            throw new O2CommonException(null, SystemParameterConstants.ErrorCode.ERROR_SYSTEM_PARAM_NOT_EXIST);
        }
        // 只可复制预定义数据
        if (!BaseConstants.DEFAULT_TENANT_ID.equals(sysParam.getTenantId())) {
            throw new O2CommonException(null, SystemParameterConstants.ErrorCode.ERROR_SYSTEM_PARAM_ONLY_COPY_PREDEFINE);
        }
        return sysParam;
    }

    /**
     * 复制系统参数
     *
     * @param sysParam       系统参数
     * @param copyToTenantId 租户Id
     */
    protected void copySystemParam(SystemParameter sysParam, Long copyToTenantId) {
        int existCount = systemParameterRepository.selectCountByCondition(Condition.builder(SystemParameter.class).andWhere(
                Sqls.custom().andEqualTo(SystemParameter.FIELD_PARAM_CODE, sysParam.getParamCode())
                        .andEqualTo(SystemParameter.FIELD_TENANT_ID, copyToTenantId)
        ).build());

        // 请勿重复复制
        if (existCount > 0) {
            throw new O2CommonException(null, SystemParameterConstants.ErrorCode.ERROR_SYSTEM_PARAM_ALREADY_EXISTS);
        }

        SystemParameter newSysParam = SysParameterConverter.poToPoObject(sysParam);
        newSysParam.setTenantId(copyToTenantId);

        List<SystemParamValue> sysParamValues = systemParamValueRepository.selectByCondition(Condition.builder(SystemParamValue.class).andWhere(
                Sqls.custom().andEqualTo(SystemParamValue.FIELD_PARAM_ID, sysParam.getParamId())
                        .andEqualTo(SystemParamValue.FIELD_TENANT_ID, sysParam.getTenantId())
        ).build());
        List<SystemParamValue> newSysParamValues = SystemParamValueConverter.poToPoList(sysParamValues);

        transactionalHelper.transactionOperation(() -> {
            // 保存DB
            systemParameterRepository.insertSelective(newSysParam);
            if (CollectionUtils.isNotEmpty(newSysParamValues)) {
                newSysParamValues.forEach(item -> {
                    item.setParamId(newSysParam.getParamId());
                    item.setTenantId(copyToTenantId);
                });
                systemParamValueRepository.batchInsertSelective(newSysParamValues);
            }
            // 同步Redis
            systemParameterRedis.updateToRedis(newSysParam, copyToTenantId);
        });
    }
}
