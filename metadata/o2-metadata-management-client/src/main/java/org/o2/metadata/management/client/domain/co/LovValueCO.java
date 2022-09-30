package org.o2.metadata.management.client.domain.co;

import lombok.Data;

import java.util.Map;

/**
 *
 *
 *
 * @author yipeng.zhu@hand-china.com 2021-09-08
 **/
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
