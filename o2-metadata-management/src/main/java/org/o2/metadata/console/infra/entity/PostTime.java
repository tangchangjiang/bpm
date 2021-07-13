package org.o2.metadata.console.infra.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.choerodon.core.exception.CommonException;
import io.choerodon.mybatis.annotation.ModifyAudit;
import io.choerodon.mybatis.annotation.VersionAudit;
import io.choerodon.mybatis.domain.AuditDomain;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.validator.constraints.Range;
import org.hzero.core.base.BaseConstants;
import org.o2.core.O2CoreConstants;
import org.o2.metadata.console.infra.constant.MetadataConstants;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalTime;

/**
 * 服务点接单和派单时间
 *
 * @author tingting.wang@hand-china.com 2019-3-25
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel("服务点接单和派单时间")
@VersionAudit
@ModifyAudit
@Table(name = "o2md_post_time")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PostTime extends AuditDomain {

    public static final String FIELD_POST_TIME_ID = "postTimeId";
    public static final String FIELD_POS_ID = "posId";
    public static final String FIELD_WEEK = "week";
    public static final String FIELD_RECEIVE_START_TIME = "receiveStartTime";
    public static final String FIELD_RECEIVE_END_TIME = "receiveEndTime";
    public static final String FIELD_DISTRIBUTE_START_TIME = "distributeStartTime";
    public static final String FIELD_DISTRIBUTE_END_TIME = "distributeEndTime";

    //
    // 业务方法(按public protected private顺序排列)
    // ------------------------------------------------------------------------------

    /**
     * 填充时间区间，如果是半闭合区间补充上 LocalTime.MIN 或 LocalTime.MAX
     *
     * @return 如果两个区间都为空，则判断为没有意义的对象，返回 false
     */
    public boolean initTimeRange() {
        final int distributeTimeRange = getTimeRangeType(this.distributeStartTime, this.distributeEndTime);

        if (O2CoreConstants.RangeType.SEMI_CLOSED == distributeTimeRange) {
            final TimeRange timeRange = new TimeRange(this.distributeStartTime, this.distributeEndTime);
            this.setDistributeStartTime(timeRange.startTime);
            this.setDistributeEndTime(timeRange.endTime);
        }
        final int receiveTimeRange = getTimeRangeType(this.receiveStartTime, this.receiveEndTime);
        if (O2CoreConstants.RangeType.CLOSED == receiveTimeRange) {
            final TimeRange timeRange = new TimeRange(this.receiveStartTime, this.receiveEndTime);
            this.setReceiveStartTime(timeRange.startTime);
            this.setReceiveEndTime(timeRange.endTime);
        }
        // 是否是没有意义的 PostTime
        return distributeTimeRange + receiveTimeRange != O2CoreConstants.RangeType.BLANK;
    }

    private int getTimeRangeType(final LocalTime start, final LocalTime end) {
        return isNull(start) + isNull(end);
    }

    private int isNull(final Object o) {
        return o == null ? 0 : 1;
    }

    void validate() {
        if (this.distributeEndTime != null && this.distributeStartTime != null && this.distributeStartTime.isAfter(this.distributeEndTime)) {
            throw new CommonException(MetadataConstants.ErrorCode.BASIC_DATA_DATE_RANGE_ERROR,
                    this.distributeStartTime, this.distributeEndTime);
        }
        if (this.receiveStartTime != null && this.receiveEndTime != null && this.receiveStartTime.isAfter(this.receiveEndTime)) {
            throw new CommonException(MetadataConstants.ErrorCode.BASIC_DATA_DATE_RANGE_ERROR,
                    this.receiveStartTime, this.receiveEndTime);
        }
    }

    //
    // 数据库字段
    // ------------------------------------------------------------------------------

    @ApiModelProperty("表ID，主键，供其他表做外键")
    @Id
    @GeneratedValue
    private Long postTimeId;

    @ApiModelProperty(value = "服务点id")
    private Long posId;

    @ApiModelProperty(value = "星期，1 代表星期一，7 代表星期天")
    @Range(min = 1, max = 7)
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

    //
    // 非数据库字段
    // ------------------------------------------------------------------------------

    /**
     * 时间范围
     */
    private class TimeRange {
        final LocalTime startTime;
        final LocalTime endTime;

        TimeRange(final LocalTime startTime, final LocalTime endTime) {
            this.startTime = startTime != null ? startTime : LocalTime.MIN;
            this.endTime = endTime != null ? endTime : LocalTime.MAX;
        }
    }
}
