package org.o2.metadata.console.api.vo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;
import org.hzero.boot.platform.lov.annotation.LovValue;
import org.o2.metadata.console.infra.constant.MetadataConstants;

import java.util.Date;


/**
 * description
 *
 * @author zhilin.ren@hand-china.com 2021/09/07 14:09
 */
@ApiModel(value = "静态资源")
@Data
@Builder
public class StaticResourceConfigVO {

    public static final String FIELD_RESOURCE_LEVEL_MEANING = "resourceLevelMeaning";
    public static final String FIELD_DIFFERENT_LANG_FLAG_MEANING = "differentLangFlagMeaning";
    @ApiModelProperty("主键，自增")
    private Long resourceConfigId;
    @ApiModelProperty(value = "静态资源编码", required = true)
    private String resourceCode;
    @ApiModelProperty(value = "静态资源层级", required = true)
    @LovValue(value = MetadataConstants.PublicLov.STATIC_RESOURCE_LOV_CODE, meaningField = FIELD_RESOURCE_LEVEL_MEANING)
    private String resourceLevel;
    @ApiModelProperty(value = "静态资源访问jsonKey", required = true)
    private String jsonKey;
    @ApiModelProperty(value = "租户ID", required = true)
    private Long tenantId;
    @ApiModelProperty(value = "描述")
    private String description;
    @ApiModelProperty(value = "是否区分多语言", required = true)
    @LovValue(value = MetadataConstants.PublicLov.DIFFERENT_LANG_FLAG, meaningField = FIELD_DIFFERENT_LANG_FLAG_MEANING)
    private Integer differentLangFlag;
    @ApiModelProperty(value = "上传目录")
    private String uploadFolder;

    @ApiModelProperty(value = "来源模块,值集:O2MD.DOMAIN_MODULE")
    @LovValue(value = MetadataConstants.PublicLov.SOURCE_MODULE_CODE)
    private String sourceModuleCode;
    @ApiModelProperty(value = "来源程序")
    private String sourceProgram;
    private String resourceLevelMeaning;
    private String differentLangFlagMeaning;
    @ApiModelProperty("来源模块")
    private String sourceModuleMeaning;

    @ApiModelProperty(hidden = true)
    @JsonIgnore
    private Long createdBy;
    @ApiModelProperty(hidden = true)
    @JsonIgnore
    private Long lastUpdatedBy;
    @ApiModelProperty("创建人")
    private String createdName;
    @ApiModelProperty("更新人")
    private String updateName;

}
