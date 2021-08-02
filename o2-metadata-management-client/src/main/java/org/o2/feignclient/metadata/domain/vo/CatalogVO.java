package org.o2.feignclient.metadata.domain.vo;

import lombok.Data;

import java.util.List;

/**
 *
 * 目录
 *
 * @author yipeng.zhu@hand-china.com 2021-07-30
 **/
@Data
public class CatalogVO {

    /**
     * 目录编码
     */
    private String catalogCode;

    /**
     * 名称
     */
    private String catalogName;

    /**
     * 目录版本集合
     */
    private List<CatalogVersionVO> catalogVersionList;

}
