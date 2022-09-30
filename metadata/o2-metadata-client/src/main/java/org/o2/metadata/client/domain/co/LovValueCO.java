package org.o2.metadata.client.domain.co;

import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.util.Map;

/**
 * 值集
 * @author peng.xu@hand-china.com 2022/1/21
 */
@ApiModel("值集")
@Data
public class LovValueCO {
    private String value;
    private String meaning;
    private String description;
    private String tag;
    private String parentValue;
    private Integer orderSeq;
    private Map<String, Object> metadata;
}
