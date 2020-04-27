package org.o2.metadata.core.domain.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.choerodon.core.exception.CommonException;
import io.choerodon.mybatis.annotation.ModifyAudit;
import io.choerodon.mybatis.annotation.MultiLanguage;
import io.choerodon.mybatis.annotation.MultiLanguageField;
import io.choerodon.mybatis.annotation.VersionAudit;
import io.choerodon.mybatis.domain.AuditDomain;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.o2.core.helper.FastJsonHelper;
import org.o2.data.redis.client.RedisCacheClient;
import org.o2.metadata.core.domain.repository.SysParameterRepository;
import org.o2.metadata.core.domain.vo.SysParameterVO;
import org.o2.metadata.core.infra.constants.BasicDataConstants;
import org.apache.commons.collections.CollectionUtils;
import org.o2.metadata.core.infra.constants.MetadataConstants;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 系统参数设置
 *
 * @author tingting.wang@hand-china.com 2019-3-25
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel("系统参数设置")
@VersionAudit
@ModifyAudit
@MultiLanguage
@Table(name = "o2md_sys_parameter")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SysParameter extends AuditDomain {

    public static final String FIELD_PARAMETER_SETTING_ID = "parameterId";
    public static final String FIELD_PARAMETER_CODE = "parameterCode";
    public static final String FIELD_PARAMETER_DESC = "parameterDesc";
    public static final String FIELD_PARAMETER_VALUE = "parameterValue";
    public static final String FIELD_IS_ACTIVE = "activeFlag";
    public static final String FIELD_TENANT_ID = "tenantId";

    //
    // 业务方法(按public protected private顺序排列)
    // ------------------------------------------------------------------------------

    public void validateParameterCode(final SysParameterRepository sysParameterRepository) {
        final SysParameter entity = new SysParameter();
        entity.setTenantId(this.tenantId);
        entity.setParameterCode(this.parameterCode);
        final List<SysParameter> list = sysParameterRepository.select(entity);
        if (CollectionUtils.isNotEmpty(list)) {
            throw new CommonException(BasicDataConstants.ErrorCode.BASIC_DATA_DUPLICATE_CODE, "SysParameter(" + this.parameterCode + ")");
        }
    }

    /**
     * 转换保存
     * @param sysParameter  sysParameter
     * @return SysParameterVO
     */
    private SysParameterVO convert (final SysParameter sysParameter) {
        final SysParameterVO sysParameterVO = new SysParameterVO();
        sysParameterVO.setParameterCode(sysParameter.getParameterCode());
        sysParameterVO.setParameterValue(sysParameter.getParameterValue());
        sysParameterVO.setActiveFlag(sysParameter.getActiveFlag());
        sysParameterVO.setTenantId(sysParameter.getTenantId());
        return sysParameterVO;
    }

    /**
     * 组黄map
     * @param sysParameters sysParameters
     * @return map
     */
    public Map<Integer, List<SysParameter>> groupMap (List<SysParameter> sysParameters) {
        return sysParameters.stream().collect(Collectors.groupingBy(SysParameter::getActiveFlag));
    }

    /**
     * 同步到redis
     * @param sysParameters    sysParameters
     * @param redisCacheClient redisCacheClient
     */
    public void syncToRedis (final List<SysParameter> sysParameters,
                              RedisCacheClient redisCacheClient) {
        // 根据是否有效分组 无效 删除
        final Map<Integer, List<SysParameter>> sysParametersMap = this.groupMap(sysParameters);
        for (Map.Entry<Integer, List<SysParameter>>  sysParameterEntry : sysParametersMap.entrySet()){
            List<String> keyList = new ArrayList<>();
            Map<String,String> map = new HashMap<>();
            for (SysParameter sysParameter : sysParameterEntry.getValue()) {
                final String cacheKey = MetadataConstants.SysParameterCache.sysParameterKey(sysParameter.getParameterCode(), tenantId);
                final String value = FastJsonHelper.objectToString(this.convert(sysParameter));
                keyList.add(cacheKey);
                map.put(cacheKey,value);
            }
            if (sysParameterEntry.getKey() == 1) {
                redisCacheClient.opsForValue().multiSet(map);
            } else {
                redisCacheClient.delete(keyList);
            }
        }
    }


    //
    // 数据库字段
    // ------------------------------------------------------------------------------

    @ApiModelProperty("表ID，主键，供其他表做外键")
    @Id
    @GeneratedValue
    private Long parameterId;

    @ApiModelProperty(value = "键值")
    @NotBlank
    private String parameterValue;

    @ApiModelProperty(value = "是否激活")
    @NotNull
    private Integer activeFlag;

    @ApiModelProperty(value = "参数编码")
    @NotBlank
    private String parameterCode;

    @ApiModelProperty(value = "参数说明")
    @MultiLanguageField
    private String parameterDesc;

    //
    // 非数据库字段
    // ------------------------------------------------------------------------------

    @ApiModelProperty(value = "最后更新人")
    @Transient
    private String lastUpdateByName;

    @ApiModelProperty(value = "组织ID")
    private Long tenantId;
}
