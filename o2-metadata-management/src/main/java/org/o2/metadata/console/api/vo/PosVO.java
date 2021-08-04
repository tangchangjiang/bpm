package org.o2.metadata.console.api.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.choerodon.mybatis.domain.AuditDomain;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hzero.boot.platform.lov.annotation.LovValue;
import org.hzero.core.base.BaseConstants;
import org.o2.metadata.console.infra.constant.MetadataConstants;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.List;

/**
 * @author tingting.wang@hand-china.com 2019-3-25
 */

@ApiModel("服务点视图")
@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
@EqualsAndHashCode(callSuper = true)
public class PosVO extends AuditDomain {

    @ApiModelProperty("表ID，主键，供其他表做外键")
    private Long posId;

    @ApiModelProperty(value = "服务点编码")
    private String posCode;

    @ApiModelProperty(value = "服务点名称")
    private String posName;

    @ApiModelProperty(value = "服务点状态")
    @LovValue(lovCode = MetadataConstants.PosStatus.LOV_CODE)
    private String posStatusCode;

    @ApiModelProperty(value = "服务点类型,值集O2MD.POS_TYPE")
    @LovValue(lovCode = MetadataConstants.PosType.LOV_CODE)
    private String posTypeCode;

    @ApiModelProperty(value = "营业类型")
    @LovValue(lovCode = MetadataConstants.BusinessType.LOV_CODE)
    private String businessTypeCode;

    @JsonFormat(pattern = BaseConstants.Pattern.DATE)
    @DateTimeFormat(pattern = BaseConstants.Pattern.DATE)
    @ApiModelProperty(value = "开店日期")
    private LocalDate openDate;

    @ApiModelProperty(value = "详细地址")
    private Long addressId;

    @ApiModelProperty(value = "营业时间")
    private String businessTime;

    private Long pickUpLimitQuantity;

    @ApiModelProperty(value = "店铺公告信息")
    private String notice;

    @ApiModelProperty(value = "门店快递发货接单量", hidden = true)

    private Long expressLimitQuantity;

    @ApiModelProperty(value = "门店快递发货接单量", hidden = true)
    private PosAddressVO address;

    @ApiModelProperty(value = "服务点状态含义", hidden = true)
    private String posStatusMeaning;

    @ApiModelProperty(value = "服务点类型含义", hidden = true)
    private String posTypeMeaning;

    @ApiModelProperty(value = "营业点类型含义", hidden = true)
    private String businessTypeMeaning;

    @ApiModelProperty(value = "服务点接单和派单时间", hidden = true)
    private List<PostTimeVO> postTimes;

    @ApiModelProperty(value = "承运商名称", hidden = true)
    private String carrierName;

    @ApiModelProperty(value = "承运商ID")
    private Long carrierId;

    @ApiModelProperty(value = "组织ID", hidden = true)
    private Long tenantId;


    @ApiModelProperty("省id")
    private String regionCode;

    @ApiModelProperty(value = "省名称", hidden = true)
    private String regionName;

    @ApiModelProperty("市id")
    private String cityCode;

    @ApiModelProperty(value = "市名称", hidden = true)
    private String cityName;

    @ApiModelProperty(value = "区id")
    private String districtCode;

    @ApiModelProperty(value = "区名称", hidden = true)
    private String districtName;

    @ApiModelProperty(value = "街道地址", hidden = true)
    private String streetName;
}
