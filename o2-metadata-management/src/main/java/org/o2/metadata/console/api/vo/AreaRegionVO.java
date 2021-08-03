package org.o2.metadata.console.api.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hzero.boot.platform.lov.annotation.LovValue;
import org.o2.metadata.console.app.bo.RegionBO;
import org.o2.metadata.console.infra.constant.MetadataConstants;

import javax.persistence.Transient;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author tingting@hand-china.com
 * @date 2019/4/27
 */

@ApiModel("地区下的省份")
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AreaRegionVO implements Serializable, Comparable {
    @ApiModelProperty("大区，值集O2MD.AREA_CODE")
    @LovValue(lovCode = MetadataConstants.AreaCode.LOV_CODE)
    private String areaCode;

    @ApiModelProperty(value = "大区名称")
    @Transient
    private String areaMeaning;

    private List<RegionBO> regionList;

    public AreaRegionVO() {
        this.regionList = new ArrayList<>();
    }

    @Override
    public int compareTo(final Object o) {
        if (o instanceof AreaRegionVO) {
            final AreaRegionVO areaRegionVO = (AreaRegionVO) o;
            return areaRegionVO.getAreaCode().compareTo(this.getAreaCode());
        } else {
            return 1;
        }
    }
}
