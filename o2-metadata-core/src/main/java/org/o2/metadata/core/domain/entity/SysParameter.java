package org.o2.metadata.core.domain.entity;

import io.choerodon.core.exception.CommonException;
import io.choerodon.mybatis.annotation.ModifyAudit;
import io.choerodon.mybatis.annotation.VersionAudit;
import io.choerodon.mybatis.domain.AuditDomain;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.o2.metadata.core.domain.repository.SysParameterRepository;
import org.o2.metadata.core.infra.constants.BasicDataConstants;
import org.apache.commons.collections.CollectionUtils;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

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
@Table(name = "o2md_sys_parameter")
public class SysParameter extends AuditDomain {

    public static final String FIELD_PARAMETER_SETTING_ID = "parameterId";
    public static final String FIELD_PARAMETER_CODE = "parameterCode";
    public static final String FIELD_PARAMETER_DESC = "parameterDesc";
    public static final String FIELD_PARAMETER_VALUE = "parameterValue";
    public static final String FIELD_IS_ACTIVE = "activeFlag";

    //
    // 业务方法(按public protected private顺序排列)
    // ------------------------------------------------------------------------------

    public void validateParameterCode(final SysParameterRepository sysParameterRepository) {
        final SysParameter entity = new SysParameter();
        entity.setParameterCode(this.parameterCode);
        final List<SysParameter> list = sysParameterRepository.select(entity);
        if (CollectionUtils.isNotEmpty(list)) {
            throw new CommonException(BasicDataConstants.ErrorCode.BASIC_DATA_DUPLICATE_CODE, "SysParameter(" + this.parameterCode + ")");
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
