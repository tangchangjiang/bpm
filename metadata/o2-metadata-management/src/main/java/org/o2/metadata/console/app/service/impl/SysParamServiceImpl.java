package org.o2.metadata.console.app.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.hzero.core.base.BaseConstants;
import org.hzero.mybatis.domian.Condition;
import org.hzero.mybatis.util.Sqls;
import org.o2.core.exception.O2CommonException;
import org.o2.core.helper.TransactionalHelper;
import org.o2.metadata.console.api.co.ResponseCO;
import org.o2.metadata.console.api.co.SystemParameterCO;
import org.o2.metadata.console.api.dto.SystemParameterQueryInnerDTO;
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

import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * @author yong.nie@hand-china.com
 * @date 2020/6/30 21:26
 **/
@Slf4j
@Service
public class SysParamServiceImpl implements SysParamService {

    private final SystemParameterDomainService systemParameterDomainService;
    private final SystemParameterRepository systemParameterRepository;
    private final SystemParameterRedis systemParameterRedis;
    private final SystemParamValueRepository systemParamValueRepository;
    private final TransactionalHelper transactionalHelper;

    public SysParamServiceImpl(SystemParameterDomainService systemParameterDomainService,
                               SystemParameterRepository systemParameterRepository,
                               SystemParameterRedis systemParameterRedis, SystemParamValueRepository systemParamValueRepository,
                               TransactionalHelper transactionalHelper) {

        this.systemParameterDomainService = systemParameterDomainService;
        this.systemParameterRepository = systemParameterRepository;
        this.systemParameterRedis = systemParameterRedis;
        this.systemParamValueRepository = systemParamValueRepository;
        this.transactionalHelper = transactionalHelper;
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
        if (null == paramId) {
            throw new O2CommonException(null, SystemParameterConstants.ErrorCode.ERROR_SYSTEM_PARAM_NO_SELECT);
        }
        if (null == tenantId) {
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
        int existCount = systemParameterRepository.selectCountByCondition(Condition.builder(SystemParameter.class).andWhere(
                Sqls.custom().andEqualTo(SystemParameter.FIELD_PARAM_CODE, sysParam.getParamCode())
                        .andEqualTo(SystemParameter.FIELD_TENANT_ID, tenantId)
        ).build());
        // 请勿重复复制
        if (existCount > 0) {
            throw new O2CommonException(null, SystemParameterConstants.ErrorCode.ERROR_SYSTEM_PARAM_ALREADY_EXISTS);
        }

        SystemParameter newSysParam = SysParameterConverter.poToPoObject(sysParam);
        newSysParam.setTenantId(tenantId);

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
                    item.setTenantId(tenantId);
                });
                systemParamValueRepository.batchInsertSelective(newSysParamValues);
            }
            // 同步Redis
            systemParameterRedis.updateToRedis(newSysParam, tenantId);
        });
    }

}
