package org.o2.metadata.console.api.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.choerodon.mybatis.annotation.ModifyAudit;
import io.choerodon.mybatis.annotation.VersionAudit;
import io.choerodon.mybatis.domain.AuditDomain;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hzero.core.base.BaseConstants;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalTime;

/**
 * 服务点接单和派单时间
 *
 * @author tingting.wang@hand-china.com 2019-3-25
 */
@EqualsAndHashCode(callSuper = true)
@Data
@ApiModel("服务点接单和派单时间")
@VersionAudit
@ModifyAudit
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PostTimeVO extends AuditDomain {

    @ApiModelProperty("表ID，主键，供其他表做外键")
    private Long postTimeId;

    @ApiModelProperty(value = "服务点id")
    private Long posId;

    @ApiModelProperty(value = "星期，1 代表星期一，7 代表星期天")
    private Long week;

    @JsonFormat(pattern = BaseConstants.Pattern.TIME_SS)
    @DateTimeFormat(pattern = BaseConstants.Pattern.TIME_SS)
    @ApiModelProperty(value = "接单开始时间 格式 HH:mm:ss")
    private LocalTime receiveStartTime;

    @JsonFormat(pattern = BaseConstants.Pattern.TIME_SS)
    @DateTimeFormat(pattern = BaseConstants.Pattern.TIME_SS)
    @ApiModelProperty(value = "接单结束时间 格式 HH:mm:ss")
    private LocalTime receiveEndTime;

    @JsonFormat(pattern = BaseConstants.Pattern.TIME_SS)
    @DateTimeFormat(pattern = BaseConstants.Pattern.TIME_SS)
    @ApiModelProperty(value = "派单开始时间 格式 HH:mm:ss")
    private LocalTime distributeStartTime;

    @JsonFormat(pattern = BaseConstants.Pattern.TIME_SS)
    @DateTimeFormat(pattern = BaseConstants.Pattern.TIME_SS)
    @ApiModelProperty(value = "派单结束时间 格式 HH:mm:ss")
    private LocalTime distributeEndTime;

    @ApiModelProperty(value = "租户ID")
    private Long tenantId;

}
