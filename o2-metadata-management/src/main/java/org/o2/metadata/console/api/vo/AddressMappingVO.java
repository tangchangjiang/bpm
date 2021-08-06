package org.o2.metadata.console.api.vo;

import com.fasterxml.jackson.annotation.JsonInclude;

import io.choerodon.mybatis.domain.AuditDomain;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hzero.boot.platform.lov.annotation.LovValue;
import org.o2.core.O2CoreConstants;
import java.util.ArrayList;
import java.util.List;

/**
 * 地址匹配
 *
 * @author tingting.wang@hand-china.com 2019-3-25
 */
@Data
@EqualsAndHashCode(callSuper = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@ApiModel("地址匹配")
public class AddressMappingVO extends AuditDomain {

    @ApiModelProperty("表ID，主键，供其他表做外键")
    private Long addressMappingId;

    @ApiModelProperty(value = "region 关联")
    private String regionCode;

    @ApiModelProperty(value = "地址类型.值集:O2MD.ADDRESS_TYPE")
    @LovValue(lovCode = O2CoreConstants.AddressType.LOV_CODE)
    private String addressTypeCode;



    @ApiModelProperty(value = "外部区域代码")
    private String externalCode;

    @ApiModelProperty(value = "外部区域名称")
    private String externalName;

    @ApiModelProperty(value = "是否启用")
    private Integer activeFlag;

    @ApiModelProperty(value = "租户ID")
    private Long tenantId;


    @ApiModelProperty(value = "版本ID")
    private Long catalogId;

    @ApiModelProperty(value = "查询条件 内部区域名称")
    private String regionName;

    @ApiModelProperty(value = "平台类型含义", hidden = true)
    private String platformTypeMeaning;

    @ApiModelProperty(value = "地址类型含义", hidden = true)
    private String addressTypeMeaning;


    /**
     * 显示region的层级目录结构，只在详情里展示
     */
    @ApiModelProperty(hidden = true)
    private List<Long> regionPathIds = new ArrayList<>(4);
    @ApiModelProperty(hidden = true)
    private List<String> regionPathCodes = new ArrayList<>(4);
    @ApiModelProperty(hidden = true)
    private List<String> regionPathNames = new ArrayList<>(4);

    @ApiModelProperty(value = "版本编码")
    private String catalogCode;

    @ApiModelProperty(value = "版本名称",required = true)
    private String catalogName;
}
