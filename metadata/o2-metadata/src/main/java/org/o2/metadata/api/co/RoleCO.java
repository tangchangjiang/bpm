package org.o2.metadata.api.co;

import lombok.Data;

/**
 * Description
 *
 * @author yipeng.zhu@hand-china.com 2022/8/25
 */
@Data
public class RoleCO {
    /**
     * ID
     */
    private Long id;
    /**
     * 编码
     */
    private String roleCode;
    /**
     * 名称
     */
    private String name;
    /**
     * 等级路径
     */
    private String  levelPath;
}
