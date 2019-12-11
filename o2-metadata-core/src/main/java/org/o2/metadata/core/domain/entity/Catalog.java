package org.o2.metadata.core.domain.entity;

import io.choerodon.mybatis.annotation.ModifyAudit;
import io.choerodon.mybatis.annotation.VersionAudit;
import io.choerodon.mybatis.domain.AuditDomain;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

/**
 * 版本
 *
 * @author jiu.yang@hand-china.com 2019-12-02 15:33:52
 */
@Data
@ApiModel("版本")
@VersionAudit
@ModifyAudit
@Table(name = "o2md_catalog")
@Builder
public class Catalog extends AuditDomain {

    public static final String FIELD_CATALOG_ID = "catalogId";
    public static final String FIELD_CATALOG_CODE = "catalogId";
    public static final String FIELD_CATALOG_NAME = "catalogName";
    public static final String FIELD_CATALOG_DESCRIPTION = "catalogDescription";
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
    private Long catalogId;
    @ApiModelProperty(value = "版本编码",required = true)
    private String catalogCode;
    @ApiModelProperty(value = "版本名称",required = true)
    private String catalogName;
    @ApiModelProperty(value = "版本描述")
    private String catalogDescription;
    @ApiModelProperty(value = "租户ID",required = true)
    @NotNull
    private Long tenantId;

}
