package org.o2.metadata.console.app.bo;

import lombok.Data;

import java.io.Serializable;

/**
 *
 * 静态文件
 *
 * @author: yipeng.zhu@hand-china.com 2020-05-20 13:35
 **/
@Data
public class RegionCacheBO implements Serializable {
    private String regionName;


    private String parentRegionCode;


    private String regionCode;


    private String countryCode;
}
