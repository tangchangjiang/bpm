package org.o2.metadata.domain.entity;

import io.choerodon.mybatis.annotation.ModifyAudit;
import io.choerodon.mybatis.annotation.VersionAudit;
import io.choerodon.mybatis.domain.AuditDomain;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

/**
 * 版本目录
 *
 * @author jiu.yang@hand-china.com 2019-12-02 15:33:52
 */
@Data
@ApiModel("版本目录")
@VersionAudit
@ModifyAudit
@Table(name = "o2md_catalog_version")
public class CatalogVersion extends AuditDomain {

    public static final String FIELD_CATALOG_VERSION_ID = "catalogVersionId";
    public static final String FIELD_CATALOG_VERSION_CODE = "catalogVersionCode";
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
    @ApiModelProperty(value = "版本目录编码",required = true)
    @NotBlank
    private String catalogVersionCode;
    @ApiModelProperty(value = "版本目录名称",required = true)
    @NotBlank
    private String catalogVersionName;
    @ApiModelProperty(value = "版本目录表述")
    private String catalogVersionDescription;
    @ApiModelProperty(value = "版本ID",required = true)
    @NotNull
    private Long catalogId;
    @ApiModelProperty(value = "租户ID",required = true)
    @NotNull
    private Long tenantId;

}
