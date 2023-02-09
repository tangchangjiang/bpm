package org.o2.metadata.console.api.dto;

import java.util.Map;

import lombok.Data;

/**
 * 值集内容查询DTO
 *
 * @author longcheng.yang@hand-china.com 9/2/2023
 */

@Data
public class QueryLovValueMeaningDTO {

    /**
     * LOV编码
     */
    private String lovCode;

    /**
     * 查询Map
     */
    private Map<String, String> queryLovValueMap;

    /**
     * page
     */
    private Integer page;

    /**
     * size
     */
    private Integer size;
}
