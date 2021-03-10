package org.o2.metadata.core.domain.entity;

import org.apache.commons.collections.CollectionUtils;
import org.hzero.boot.platform.lov.annotation.LovValue;
import org.o2.data.redis.client.RedisCacheClient;
import org.o2.metadata.core.domain.repository.SystemParameterRepository;
import org.o2.metadata.core.domain.vo.SysParameterVO;
import org.o2.metadata.core.infra.constants.BasicDataConstants;
import org.o2.metadata.core.infra.constants.MetadataConstants;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import io.choerodon.core.exception.CommonException;
import io.choerodon.mybatis.annotation.ModifyAudit;
import io.choerodon.mybatis.annotation.VersionAudit;
import io.choerodon.mybatis.domain.AuditDomain;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 系统参数
 *
 * @author hongyun.wang01@hand-china.com 2020-02-13
 */
@ApiModel("系统参数")
@VersionAudit
@ModifyAudit
@Table(name = "o2md_system_parameter")
@Data
@EqualsAndHashCode(callSuper = true)
public class SystemParameter extends AuditDomain {

    public static final String FIELD_PARAM_ID = "paramId";
    public static final String FIELD_PARAM_CODE = "paramCode";
    public static final String FIELD_PARAM_NAME = "paramName";
    public static final String FIELD_PARAM_TYPE = "paramTypeCode";
    public static final String FIELD_ACTIVE_FLAG = "activeFlag";
    public static final String FIELD_REMARK = "remark";
    public static final String FIELD_DEFAULT_VALUE = "defaultValue";
    public static final String FIELD_TENANT_ID = "tenantId";

    //
    // 业务方法(按public protected private顺序排列)
    // ------------------------------------------------------------------------------

    //
    // 数据库字段
    // ------------------------------------------------------------------------------

    @ApiModelProperty("表ID，主键")
    @Id
    @GeneratedValue
    private Long paramId;
    @ApiModelProperty(value = "编码", required = true)
    @NotBlank
    private String paramCode;
    @ApiModelProperty(value = "参数名称")
    private String paramName;
    @ApiModelProperty(value = "值集，KV（key-value） LIST(重复) SET（不重复）", required = true)
    @LovValue(MetadataConstants.ParamType.LOV_CODE)
    private String paramTypeCode;
    @ApiModelProperty(value = "是否启用", required = true)
    @NotNull
    private Integer activeFlag;
    @ApiModelProperty(value = "备注说明")
    private String remark;
    @ApiModelProperty(value = "默认值")
    private String defaultValue;
    @ApiModelProperty(value = "租户ID", required = true)
    @NotNull
    private Long tenantId;

    //
    // 非数据库字段
    // ------------------------------------------------------------------------------

    @Transient
    @ApiModelProperty("参数类型")
    private String paramTypeMeaning;


    //
    // 业务方法(按public protected private顺序排列)
    // ------------------------------------------------------------------------------

    public void validateParameterCode(final SystemParameterRepository sysParameterRepository) {
        final SystemParameter entity = new SystemParameter();
        entity.setTenantId(this.tenantId);
        entity.setParamCode(this.paramCode);
        final List<SystemParameter> list = sysParameterRepository.select(entity);
        if (CollectionUtils.isNotEmpty(list)) {
            throw new CommonException(BasicDataConstants.ErrorCode.BASIC_DATA_DUPLICATE_CODE, "SysParameter(" + this.paramCode + ")");
        }
    }

    /**
     * 转换保存
     *
     * @param sysParameter sysParameter
     * @return SysParameterVO
     */
    private SysParameterVO convert(final SystemParameter sysParameter) {
        final SysParameterVO sysParameterVO = new SysParameterVO();
        sysParameterVO.setParameterCode(sysParameter.getParamCode());
        sysParameterVO.setParameterValue(sysParameter.getDefaultValue());
        sysParameterVO.setActiveFlag(sysParameter.getActiveFlag());
        sysParameterVO.setTenantId(sysParameter.getTenantId());
        return sysParameterVO;
    }

    /**
     * 组黄map
     *
     * @param sysParameters sysParameters
     * @return map
     */
    public Map<Integer, List<SystemParameter>> groupMap(List<SystemParameter> sysParameters) {
        return sysParameters.stream().collect(Collectors.groupingBy(SystemParameter::getActiveFlag));
    }

    /**
     * 同步到redis
     *
     * @param sysParameters    sysParameters
     * @param redisCacheClient redisCacheClient
     */
    public void syncToRedis(final List<SystemParameter> sysParameters,
                            RedisCacheClient redisCacheClient) {
        final String cacheKey = String.format(MetadataConstants.SystemParameter.KEY, tenantId, MetadataConstants.ParamType.KV);
        Map<String, String> map = new HashMap<>();
        sysParameters.forEach(sp -> map.put(sp.getParamCode(), sp.getDefaultValue()));
        redisCacheClient.opsForHash().putAll(cacheKey, map);
    }


}
