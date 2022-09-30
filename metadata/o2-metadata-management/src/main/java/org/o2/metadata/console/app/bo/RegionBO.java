package org.o2.metadata.console.app.bo;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.util.List;

/**
 * @author tingting@hand-china.com
 * @date 2019/4/27
 */

@ApiModel("省份")
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RegionBO {

    private String regionCode;

    private String regionName;

    private String parentRegionCode;

    private List<RegionBO> children;

}
