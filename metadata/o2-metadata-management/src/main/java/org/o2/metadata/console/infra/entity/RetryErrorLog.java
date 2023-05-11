package org.o2.metadata.console.infra.entity;

import io.choerodon.mybatis.annotation.ModifyAudit;
import io.choerodon.mybatis.annotation.VersionAudit;
import io.choerodon.mybatis.domain.AuditDomain;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * 元数据错误日志表
 *
 * @author chao.yang05@hand-china.com 2023-05-10 17:35:16
 */
@EqualsAndHashCode(callSuper = true)
@ApiModel("元数据错误日志表")
@VersionAudit
@ModifyAudit
@Table(name = "o2md_retry_error_log")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RetryErrorLog extends AuditDomain {

    public static final String FIELD_ERROR_LOG_ID = "errorLogId";
    public static final String FIELD_CASE_CODE = "caseCode";
    public static final String FIELD_ERROR_TIME = "errorTime";
    public static final String FIELD_ERROR_MESSAGE = "errorMessage";
    public static final String FIELD_PROCESS_STATUS = "processStatus";
    public static final String FIELD_RETRY = "retry";
    public static final String FIELD_REQUEST_BODY = "requestBody";
    public static final String FIELD_PROCESS_TIME = "processTime";
    public static final String FIELD_TENANT_ID = "tenantId";

    //
    // 业务方法(按public protected private顺序排列)
    // ------------------------------------------------------------------------------

    //
    // 数据库字段
    // ------------------------------------------------------------------------------


    @ApiModelProperty("表Id，主键")
    @Id
    @GeneratedValue
    private Long errorLogId;
    @ApiModelProperty(value = "场景编码", required = true)
    @NotBlank
    private String caseCode;
    @ApiModelProperty(value = "报错时间，精确到秒", required = true)
    @NotNull
    private Date errorTime;
    @ApiModelProperty(value = "报错信息")
    private String errorMessage;
    @ApiModelProperty(value = "PROCESS_ERROR :  ECP 逻辑处理错误;VALIDATED_ERROR :  数据质量问题校验错误;SUCCESS重试后处理成功;SKIP :  重试发现无需处理，置为跳过", required = true)
    @NotBlank
    private String processStatus;
    @ApiModelProperty(value = "重试次数，初始为0，每次重试后更新：+1", required = true)
    @NotNull
    private Long retry;
    @ApiModelProperty(value = "请求报文，及消费出错的报文信息，记录用于重试的入参", required = true)
    @NotBlank
    private String requestBody;
    @ApiModelProperty(value = "处理时间，每次重试后更新，默认新增记录时等于error_time", required = true)
    @NotNull
    private Date processTime;
    @ApiModelProperty(value = "租户id", required = true)
    @NotNull
    private Long tenantId;

	//
    // 非数据库字段
    // ------------------------------------------------------------------------------

    /**
     * 处理数量
     */
    @Transient
    private Long processTotal;
}

