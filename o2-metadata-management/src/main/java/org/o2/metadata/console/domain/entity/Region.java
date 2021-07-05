package org.o2.metadata.console.domain.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.choerodon.mybatis.annotation.ModifyAudit;
import io.choerodon.mybatis.annotation.MultiLanguage;
import io.choerodon.mybatis.annotation.MultiLanguageField;
import io.choerodon.mybatis.annotation.VersionAudit;
import io.choerodon.mybatis.domain.AuditDomain;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hzero.boot.platform.lov.annotation.LovValue;
import org.hzero.core.util.Regexs;
import org.o2.metadata.console.infra.constant.BasicDataConstants;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.*;
import java.util.List;

/**
 * @author tingting.wang@hand-china.com 2019-3-25
 */
@Data
@EqualsAndHashCode(callSuper = true)
@VersionAudit
@ModifyAudit
@Table(name = "hpfm_region")
@MultiLanguage
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Region extends AuditDomain {
    public static final String FIELD_REGION_ID = "regionId";
    public static final String FIELD_COUNTRY_ID = "countryId";
    public static final String FIELD_REGION_CODE = "regionCode";
    public static final String FIELD_REGION_NAME = "regionName";
    public static final String FIELD_PARENT_REGION_ID = "parentRegionId";
    public static final String FIELD_LEVEL_PATH = "levelPath";
    public static final String FIELD_ENABLED_FLAG = "enabledFlag";
    public static final String FIELD_AREA_CODE = "areaCode";
    //
    // 数据库字段
    // ------------------------------------------------------------------------------

    @ApiModelProperty("地区ID")
    @Id
    @GeneratedValue
    private Long regionId;

    @ApiModelProperty("地区编码")
    @NotBlank
    @Pattern(regexp = Regexs.CODE_UPPER)
    private String regionCode;

    @ApiModelProperty("地区名称")
    @NotBlank
    @MultiLanguageField
    private String regionName;

    @ApiModelProperty("国家ID")
    private Long countryId;

    @ApiModelProperty("父地区ID")
    private Long parentRegionId;

    @ApiModelProperty("等级路径")
    @JsonIgnore
    private String levelPath;

    @ApiModelProperty("是否启用")
    @NotNull
    @Max(1)
    @Min(0)
    private Integer enabledFlag;


    //
    // 非数据库字段
    // ------------------------------------------------------------------------------

    @ApiModelProperty(value = "子类", hidden = true)
    @Transient
    private List<Region> children;


    @Transient
    @ApiModelProperty("大区，值集O2MD.AREA_CODE")
    @LovValue(lovCode = BasicDataConstants.AreaCode.LOV_CODE)
    private String areaCode;

    @ApiModelProperty(value = "大区名称")
    @Transient
    private String areaMeaning;

    @ApiModelProperty(value = "租户ID")
    @MultiLanguageField
    private Long tenantId;
}

