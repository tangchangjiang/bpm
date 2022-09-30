package org.o2.metadata.console.api.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hzero.boot.platform.lov.annotation.LovValue;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Transient;
import javax.validation.constraints.NotBlank;

/**
 * description 平台匹配结果
 *
 * @author zhilin.ren@hand-china.com 2021/08/02 21:52
 */
@Data
public class PlatformInfMappingVO {
    @ApiModelProperty("表主键")
    @Id
    @GeneratedValue
    private Long platformInfMappingId;
    @ApiModelProperty(value = "信息类型，值集：O2MD.INF_TYPE", required = true)
    @NotBlank
    @LovValue(lovCode = "O2MD.INF_TYPE")
    private String infTypeCode;
    @ApiModelProperty(value = "平台编码", required = true)
    @NotBlank
    private String platformCode;
    @ApiModelProperty(value = "信息编码", required = true)
    @NotBlank
    private String infCode;
    @ApiModelProperty(value = "信息名称", required = true)
    @NotBlank
    private String infName;
    @ApiModelProperty(value = "平台信息编码", required = true)
    @NotBlank
    private String platformInfCode;
    @ApiModelProperty(value = "平台信息名称", required = true)
    @NotBlank
    private String platformInfName;
    @ApiModelProperty(value = "租户ID", required = true)
    private Long tenantId;

    //
    // 非数据库字段
    // ------------------------------------------------------------------------------

    @Transient
    @ApiModelProperty(value = "平台名称")
    private String platformName;

    @Transient
    @ApiModelProperty(value = "信息类型")
    private String infTypeMeaning;

}
