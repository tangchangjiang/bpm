package org.o2.metadata.console.api.dto;

import lombok.Data;

import java.util.List;

/**
 *
 *  用户信息查询
 *
 * @author yipeng.zhu@hand-china.com 2021-11-01
 **/
@Data
public class IamUserQueryInnerDTO {
    /**
     * 用户ID
     */
    @Deprecated
    private List<Long> idList;
    /**
     * 用户ID
     */
    private List<String> ids;

    /**
     * 租户ID
     */
    private Long tenantId;

    /**
     * 用户Name
     */
    private String realName;
}
