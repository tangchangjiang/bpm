package org.o2.metadata.console.infra.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.choerodon.mybatis.annotation.ModifyAudit;
import io.choerodon.mybatis.annotation.MultiLanguage;
import io.choerodon.mybatis.annotation.MultiLanguageField;
import io.choerodon.mybatis.annotation.VersionAudit;
import io.choerodon.mybatis.domain.AuditDomain;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hzero.boot.platform.lov.annotation.LovValue;
import org.o2.metadata.console.infra.constant.WarehouseConstants;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.*;
import java.util.Date;
import java.util.List;

/**
 * 仓库
 *
 * @author yuying.shi@hand-china.com 2020-03-02
 */
@Data
@EqualsAndHashCode(callSuper = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@ApiModel("仓库表")
@MultiLanguage
@VersionAudit
@ModifyAudit
@Table(name = "o2md_warehouse")
public class Warehouse extends AuditDomain {

    public static final String FIELD_WAREHOUSE_ID = "warehouseId";
    public static final String FIELD_POS_ID = "posId";
    public static final String FIELD_WAREHOUSE_CODE = "warehouseCode";
    public static final String FIELD_WAREHOUSE_NAME = "warehouseName";
    public static final String FIELD_WAREHOUSE_STATUS_CODE = "warehouseStatusCode";
    public static final String FIELD_WAREHOUSE_TYPE_CODE = "warehouseTypeCode";
    public static final String FIELD_PICKUP_QUANTITY = "pickUpQuantity";
    public static final String FIELD_EXPRESS_QUANTITY = "expressedQuantity";
    public static final String FIELD_PICKUP_FLAG = "pickedUpFlag";
    public static final String FIELD_EXPRESS_FLAG = "expressedFlag";
    public static final String FIELD_SCORE = "score";
    public static final String FIELD_ACTIVE_DATE_FROM = "activedDateFrom";
    public static final String FIELD_ACTIVE_DATE_TO = "activedDateTo";
    public static final String FIELD_INV_ORGANIZATION_CODE = "invOrganizationCode";
    public static final String FIELD_TENANT_ID = "tenantId";


    //
    // 数据库字段
    // ------------------------------------------------------------------------------

    @ApiModelProperty("表ID，主键，供其他表做外键")
    @Id
    @GeneratedValue
    private Long warehouseId;


    @ApiModelProperty(value = "服务点id，关联到 o2md_pos.pos_id")
    @NotNull
    private Long posId;

    @ApiModelProperty(value = "仓库编码")
    @NotBlank
    @Size(max = 255)
    private String warehouseCode;

    @ApiModelProperty(value = "仓库名称")
    @NotBlank
    @Size(max = 255)
    @MultiLanguageField
    private String warehouseName;

    @ApiModelProperty(value = "仓库状态,值集：O2MD.WAREHOUSE_STATUS")
    @LovValue(lovCode = WarehouseConstants.WarehouseStatus.LOV_CODE)
    @NotBlank
    @Size(max = 255)
    private String warehouseStatusCode;

    @ApiModelProperty(value = "仓库类型,值集: O2MD.WAREHOUSE_TYPE （良品仓/不良品仓/退货仓）")
    @LovValue(lovCode = WarehouseConstants.WarehouseType.LOV_CODE)
    @NotBlank
    @Size(max = 255)
    private String warehouseTypeCode;

    @ApiModelProperty(value = "自提发货接单量")
    private Long pickUpQuantity;

    @ApiModelProperty(value = "配送发货接单量")
    private Long expressedQuantity;

    @ApiModelProperty("仓库自提标识")
    @NotNull
    @Max(1)
    @Min(0)
    private Integer pickedUpFlag;

    @ApiModelProperty("仓库快递发货")
    @NotNull
    @Max(1)
    @Min(0)
    private Integer expressedFlag;

    @ApiModelProperty(value = "仓库评分")
    private Long score;

    @ApiModelProperty(value = "有效日期从")
    @NotNull
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date activedDateFrom;

    @ApiModelProperty(value = "有效日期从")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date activedDateTo;

    @ApiModelProperty(value = "库存组织编码(物权归属)")
    @Size(max = 18)
    private String invOrganizationCode;

    @ApiModelProperty(value = "租户ID")
    @NotNull
    @MultiLanguageField
    private Long tenantId;

    @ApiModelProperty(value = "生效状态")
    @NotNull
    private Integer activeFlag;

    @ApiModelProperty(value = "仓库到店退标示")
    @NotNull
    private Integer storeReturnFlag;

    //
    // 非数据库字段
    // ------------------------------------------------------------------------------


    @Transient
    private String posCode;

    @Transient
    private String posName;

    @Transient
    private String warehouseStatusMeaning;

    @Transient
    private String warehouseTypeMeaning;

    @Transient
    private String expressLimitValue;

    @Transient
    private String pickUpLimitValue;

    @Transient
    private String warehouseStatus;

    @Transient
    private String countryCode;
    @Transient
    private String regionCode;
    @Transient
    private String cityCode;
    @Transient
    private String districtCode;
    @Transient
    private String streetName;
    @Transient
    private String contact;
    @Transient
    private String mobilePhone;
    @Transient
    private String phoneNumber;

    @Transient
    private List<String> warehouseCodes;

}
