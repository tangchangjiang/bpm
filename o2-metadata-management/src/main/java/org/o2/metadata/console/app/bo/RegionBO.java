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
    private Long regionId;

    private String regionCode;

    private String regionName;

    private List<RegionBO> children;

    public RegionBO() {

    }

    public RegionBO(final Long regionId, final String regionCode, final String regionName, final List<RegionBO> children) {
        this.regionId = regionId;
        this.regionCode = regionCode;
        this.regionName = regionName;
        this.children = children;
    }
}
