package org.o2.metadata.console.api.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.choerodon.mybatis.annotation.ModifyAudit;
import io.choerodon.mybatis.annotation.VersionAudit;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Table;
import java.util.List;

/**
 * 地址匹配
 *
 * @author tingting.wang@hand-china.com 2019-3-25
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@ApiModel("地址匹配")
@VersionAudit
@ModifyAudit
@Table(name = "o2md_address_mapping")
public class AddressMappingDTO  {

    @ApiModelProperty(value = "region 关联")
    private String regionCode;

    @ApiModelProperty(value = "地址类型.值集:O2MD.ADDRESS_TYPE")
    private String addressTypeCode;

    @ApiModelProperty(value = "外部区域代码")
    private String externalCode;

    @ApiModelProperty(value = "外部区域名称")
    private String externalName;

    @ApiModelProperty(value = "是否启用")
    private Integer activeFlag;

    @ApiModelProperty(value = "租户ID")
    private Long tenantId;

    @ApiModelProperty(value = "查询条件 内部区域名称")
    private String regionName;

    @ApiModelProperty(value = "版本编码")
    private String catalogCode;

    @ApiModelProperty(value = "版本名称")
    private String catalogName;

    private List<String> regionCodes;
}
