package org.o2.metadata.core.domain.entity;

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
import org.o2.metadata.core.infra.constants.MetadataConstants;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.*;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

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
    public static final String FIELD__ACTIVE_DATE_FROM = "activedDateFrom";
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
    @LovValue(lovCode = MetadataConstants.WarehouseStatus.LOV_CODE)
    @NotBlank
    @Size(max = 255)
    private String warehouseStatusCode;

    @ApiModelProperty(value = "仓库类型,值集: O2MD.WAREHOUSE_TYPE （良品仓/不良品仓/退货仓）")
    @LovValue(lovCode = MetadataConstants.WarehouseType.LOV_CODE)
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
    private Long tenantId;

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


    //
    // 业务方法(按public protected private顺序排列)
    // ------------------------------------------------------------------------------

    public Map<String, Object> convertToWarehouseMap() {
        final Map<String, Object> warehouseMap = new HashMap<>(13);
        warehouseMap.put(MetadataConstants.WarehouseCache.POS_CODE,this.posCode);
        warehouseMap.put(MetadataConstants.WarehouseCache.WAREHOUSE_STATUS_CODE,this.warehouseStatusCode);
        warehouseMap.put(MetadataConstants.WarehouseCache.WAREHOUSE_TYPE_CODE,this.warehouseTypeCode);
        warehouseMap.put(MetadataConstants.WarehouseCache.PICKUP_FLAG,this.pickedUpFlag);
        warehouseMap.put(MetadataConstants.WarehouseCache.EXPRESSED_FLAG,this.expressedFlag);
        warehouseMap.put(MetadataConstants.WarehouseCache.SCORE,this.score);
        warehouseMap.put(MetadataConstants.WarehouseCache.ACTIVE_DATE_FROM,this.activedDateFrom);
        warehouseMap.put(MetadataConstants.WarehouseCache.ACTIVE_DATE_TO,this.activedDateFrom);
        warehouseMap.put(MetadataConstants.WarehouseCache.INV_ORGANIZATION_CODE,this.invOrganizationCode);
        warehouseMap.put(MetadataConstants.WarehouseCache.EXPRESS_LIMIT_QUANTITY,this.expressedQuantity);
        warehouseMap.put(MetadataConstants.WarehouseCache.EXPRESS_LIMIT_VALUE,this.expressLimitValue);
        warehouseMap.put(MetadataConstants.WarehouseCache.PICK_UP_LIMIT_QUANTITY,this.pickUpQuantity);
        warehouseMap.put(MetadataConstants.WarehouseCache.PICK_UP_LIMIT_VALUE,this.pickUpLimitValue);
        return warehouseMap;
    }
}