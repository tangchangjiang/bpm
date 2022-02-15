package org.o2.metadata.console.infra.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.choerodon.mybatis.annotation.ModifyAudit;
import io.choerodon.mybatis.annotation.MultiLanguage;
import io.choerodon.mybatis.annotation.MultiLanguageField;
import io.choerodon.mybatis.annotation.VersionAudit;
import io.choerodon.mybatis.domain.AuditDomain;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 版本目录
 *
 * @author jiu.yang@hand-china.com 2019-12-02 15:33:52
 */
@Data
@Builder
@ApiModel("版本目录")
@VersionAudit
@ModifyAudit
@MultiLanguage
@Table(name = "o2md_catalog_version")
@JsonInclude(JsonInclude.Include.NON_NULL)
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class CatalogVersion extends AuditDomain {

    public static final String FIELD_CATALOG_VERSION_ID = "catalogVersionId";
    public static final String FIELD_CATALOG_VERSION_CODE = "catalogVersionId";
    public static final String FIELD_CATALOG_VERSION_NAME = "catalogVersionName";
    public static final String FIELD_CATALOG_VERSION_DESCRIPTION = "catalogVersionDescription";
    public static final String FIELD_CATALOG_ID = "catalogId";
    public static final String FIELD_TENANT_ID = "tenantId";

    //
    // 业务方法(按public protected private顺序排列)
    // ------------------------------------------------------------------------------

    //
    // 数据库字段
    // ------------------------------------------------------------------------------


    @ApiModelProperty("主键")
    @Id
    @GeneratedValue
    private Long catalogVersionId;
    @ApiModelProperty(value = "版本目录编码")
    @NotNull
    private String catalogVersionCode;
    @ApiModelProperty(value = "版本目录名称")
    @NotNull
    @MultiLanguageField
    private String catalogVersionName;
    @ApiModelProperty(value = "版本目录表述")
    @MultiLanguageField
    private String catalogVersionRemarks;
    @ApiModelProperty(value = "版本ID")
    @NotNull
    private Long catalogId;
    @ApiModelProperty(value = "租户ID")
    @NotNull
    @MultiLanguageField
    private Long tenantId;
    @ApiModelProperty(value = "是否生效")
    private Integer activeFlag;

    //
    // 非数据库字段
    // ------------------------------------------------------------------------------

    @ApiModelProperty(value = "目录编码")
    @Transient
    private String catalogCode;

    @ApiModelProperty(value = "目录编码")
    @Transient
    private List<String> catalogCodes;


    @Transient
    private String catalogName;
    @Transient
    private String catalogRemarks;

}
