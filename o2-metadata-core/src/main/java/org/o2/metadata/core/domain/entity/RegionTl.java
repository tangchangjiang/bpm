package org.o2.metadata.core.domain.entity;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;
import io.choerodon.mybatis.domain.AuditDomain;
import io.choerodon.mybatis.annotation.ModifyAudit;
import io.choerodon.mybatis.annotation.VersionAudit;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 地区定义多语言
 *
 * @author jiu.yang@hand-china.com 2019-12-19 09:41:52
 */
@ApiModel("地区定义多语言")
@VersionAudit
@ModifyAudit
@Table(name = "o2md_region_tl")
@Data
public class RegionTl extends AuditDomain {

    public static final String FIELD_REGION_ID = "regionId";
    public static final String FIELD_LANG = "lang";
    public static final String FIELD_REGION_NAME = "regionName";
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
    private Long regionId;
    @ApiModelProperty(value = "语言编码",required = true)
    private String lang;
    @ApiModelProperty(value = "区域名称",required = true)
    private String regionName;
    @ApiModelProperty(value = "租户ID",required = true)
    @NotNull
    private Long tenantId;


}
