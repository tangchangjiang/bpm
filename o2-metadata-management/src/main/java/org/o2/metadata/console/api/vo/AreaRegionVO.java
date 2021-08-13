package org.o2.metadata.console.api.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import org.o2.metadata.console.app.bo.RegionBO;

import java.util.List;

/**
 * @author tingting@hand-china.com
 * @date 2019/4/27
 */

@ApiModel("地区下的省份")
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AreaRegionVO {
    private String areaCode;

    private String areaMeaning;

    private List<RegionBO> children;



}
