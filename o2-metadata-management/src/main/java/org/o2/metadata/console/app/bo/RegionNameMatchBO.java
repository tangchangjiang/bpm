package org.o2.metadata.console.app.bo;

import lombok.Data;

/**
 *
 * 地区名称模糊匹配
 *
 * @author yipeng.zhu@hand-china.com 2021-10-19
 **/
@Data
public class RegionNameMatchBO {
    /**
     * 地区名称
     */
    private String regionName;

    /**
     *  级别
     */
    private Integer levelNumber;
}
