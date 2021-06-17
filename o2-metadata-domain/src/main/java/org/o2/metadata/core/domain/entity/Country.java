package org.o2.metadata.core.domain.entity;

import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import org.hzero.core.util.Regexs;


import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.*;

import io.choerodon.mybatis.annotation.ModifyAudit;
import io.choerodon.mybatis.annotation.MultiLanguage;
import io.choerodon.mybatis.annotation.MultiLanguageField;
import io.choerodon.mybatis.annotation.VersionAudit;
import io.choerodon.mybatis.domain.AuditDomain;

/**
 * @author tingting.wang@hand-china.com 2019-3-25
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(callSuper = true)
@VersionAudit
@ModifyAudit
@MultiLanguage
@Table(name = "hpfm_country")
public class Country extends AuditDomain {

    public static final String FIELD_COUNTRY_ID = "countryId";
    public static final String FIELD_COUNTRY_CODE = "countryCode";
    public static final String FIELD_COUNTRY_NAME = "countryName";
    public static final String FIELD_ENABLED_FLAG = "enabledFlag";
    public static final String FIELD_TENANT_ID = "tenantId";

    //
    // 数据库字段
    // ------------------------------------------------------------------------------

    @ApiModelProperty("国家ID")
    @Id
    @GeneratedValue
    private Long countryId;

    @ApiModelProperty("国家编码")
    @NotBlank
    @Size(max = 30)
    @Pattern(regexp = Regexs.CODE_UPPER)
    private String countryCode;

    @ApiModelProperty("国家名称")
    @NotBlank
    @Size(max = 120)
    @MultiLanguageField
    private String countryName;

    @ApiModelProperty("是否启用")
    @Max(1)
    @Min(0)
    private Integer enabledFlag;

    @ApiModelProperty(value = "租户ID")
    @MultiLanguageField
    private Long tenantId;
}