package org.o2.metadata.domain.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.choerodon.mybatis.annotation.ModifyAudit;
import io.choerodon.mybatis.annotation.VersionAudit;
import io.choerodon.mybatis.domain.AuditDomain;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hzero.boot.platform.lov.annotation.LovValue;
import org.o2.core.O2CoreConstants;
import org.o2.metadata.domain.repository.AddressMappingRepository;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
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
@VersionAudit
@ModifyAudit
@Table(name = "o2md_address_mapping")
public class AddressMapping extends AuditDomain {
    public static final String FIELD_ADDRESS_MAPPING_ID = "addressMappingId";
    public static final String FIELD_REGION_ID = "regionId";
    public static final String FIELD_ADDRESS_TYPE_CODE = "addressTypeCode";
    public static final String FIELD_PLATFORM_TYPE_CODE = "catalogId";
    public static final String FIELD_ADDRESS_MAPPING_EX_CODE = "externalCode";
    public static final String FIELD_ADDRESS_MAPPING_EX_NAME = "externalName";
    public static final String FIELD_IS_ACTIVE = "activeFlag";
    //
    // 业务方法(按public protected private顺序排列)
    // ------------------------------------------------------------------------------

    public boolean exist(final AddressMappingRepository addressMappingRepository) {
        if (this.addressMappingId != null) {
            return addressMappingRepository.existsWithPrimaryKey(this);
        } else {
            final AddressMapping addressMapping = new AddressMapping();
            addressMapping.setCatalogId(this.catalogId);
            addressMapping.setRegionId(this.regionId);
            return addressMappingRepository.selectCount(addressMapping) > 0;
        }
    }

    //
    // 数据库字段
    // ------------------------------------------------------------------------------

    @ApiModelProperty("表ID，主键，供其他表做外键")
    @Id
    @GeneratedValue
    private Long addressMappingId;

    @ApiModelProperty(value = "region 表id关联")
    private Long regionId;

    @ApiModelProperty(value = "地址类型.值集:O2MD.ADDRESS_TYPE")
    @LovValue(lovCode = O2CoreConstants.AddressType.LOV_CODE)
    private String addressTypeCode;


    private String catalogId;

    @ApiModelProperty(value = "外部区域代码")
    private String externalCode;

    @ApiModelProperty(value = "外部区域名称")
    private String externalName;

    @ApiModelProperty(value = "是否启用")
    private Integer activeFlag;

    //
    // 非数据库字段
    // ------------------------------------------------------------------------------

    @Transient
    @ApiModelProperty(value = "查询条件 内部区域代码")
    private String regionCode;

    @Transient
    @ApiModelProperty(value = "查询条件 内部区域名称")
    private String regionName;

    @Transient
    @ApiModelProperty(value = "平台类型含义", hidden = true)
    private String platformTypeMeaning;

    @Transient
    @ApiModelProperty(value = "地址类型含义", hidden = true)
    private String addressTypeMeaning;

    @ApiModelProperty(value = "租户ID")
    private Long tenantId;

    /**
     * 显示region的层级目录结构，只在详情里展示
     */
    @Transient
    @ApiModelProperty(hidden = true)
    private List<Long> regionPathIds = new ArrayList<>(4);
    @Transient
    @ApiModelProperty(hidden = true)
    private List<String> regionPathCodes = new ArrayList<>(4);
    @Transient
    @ApiModelProperty(hidden = true)
    private List<String> regionPathNames = new ArrayList<>(4);
}
