package org.o2.metadata.console.api.dto;

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
public class RegionDTO {
    private Long regionId;

    private String regionCode;

    private String regionName;

    private List<RegionDTO> children;

    public RegionDTO() {

    }

    public RegionDTO(final Long regionId, final String regionCode, final String regionName, final List<RegionDTO> children) {
        this.regionId = regionId;
        this.regionCode = regionCode;
        this.regionName = regionName;
        this.children = children;
    }
}
