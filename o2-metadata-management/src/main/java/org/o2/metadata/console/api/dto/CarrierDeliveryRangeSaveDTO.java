package org.o2.metadata.console.api.dto;

import com.google.common.base.Preconditions;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.hzero.mybatis.domian.Condition;
import org.hzero.mybatis.util.Sqls;
import org.o2.core.copier.PropertiesCopier;
import org.o2.metadata.console.infra.entity.CarrierDeliveryRange;
import org.o2.metadata.console.infra.entity.Country;
import org.o2.metadata.console.infra.repository.CountryRepository;


import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * description
 *
 * @author zhanpeng.jiang@hand-china.com
 * @date 2021/05/27 10:18
 */
@Data
@Slf4j
public class CarrierDeliveryRangeSaveDTO {

    @ApiModelProperty(value = "国家编码")
    private String countryCode;

    @ApiModelProperty(value = "国家,id")
    private Long countryId;

    @ApiModelProperty(value = "省,id")
    @NotNull
    private Long regionId;

    @ApiModelProperty(value = "市,id")
    private Long cityId;

    @ApiModelProperty(value = "区,id")
    private Long districtId;

    @ApiModelProperty(value = "承运商id")
    @NotNull
    private Long carrierId;


    public CarrierDeliveryRange convertToCarrierDeliveryRange(CountryRepository countryRepository, Long tenantId) {
        if (StringUtils.isNotBlank(countryCode)) {
            setCountryIdByCode(countryRepository, tenantId);
        }
        CarrierDeliveryRange carrierDeliveryRange = new CarrierDeliveryRange();
        PropertiesCopier.copyEntities(this, carrierDeliveryRange);
        return carrierDeliveryRange;
    }


    private void setCountryIdByCode(CountryRepository countryRepository, Long tenantId) {
        List<Country> countries = countryRepository.selectByCondition(Condition.builder(Country.class)
                .andWhere(Sqls.custom().andEqualTo(Country.FIELD_COUNTRY_CODE, countryCode).andEqualTo(Country.FIELD_TENANT_ID, tenantId)).build());
        if (CollectionUtils.isNotEmpty(countries)) {
            Country country = countries.get(0);
            this.setCountryId(country.getCountryId());
        }
    }


    public void baseValidate() {
        log.info("CarrierDeliveryRangeSaveDTO#baseValidate start");
        Preconditions.checkArgument(StringUtils.isNoneBlank(this.countryCode)
                || null != this.countryId, "国家不能为空");
        Preconditions.checkArgument(null != this.carrierId, "承运商不能为空");
        Preconditions.checkArgument(null != this.regionId, "省不能为空");
    }
}
