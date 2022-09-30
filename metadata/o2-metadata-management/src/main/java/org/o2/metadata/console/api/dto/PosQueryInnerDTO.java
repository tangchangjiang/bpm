package org.o2.metadata.console.api.dto;

import lombok.Data;

import java.util.List;

@Data
public class PosQueryInnerDTO {
    /**
     * 服务点编码
     */
    private List<String> posCodes;
    /**
     * 服务点名称
     */
    private String posName;
}
