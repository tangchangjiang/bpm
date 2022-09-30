package org.o2.metadata.console.infra.entity;

import lombok.Data;

/**
 *
 * 运费模版信息
 *
 * @author yipeng.zhu@hand-china.com 2021-07-16
 **/
@Data
public class FreightInfo {
    /**
     * 运费模版编码
     */
    private  String freightTemplateCode;

    private  FreightTemplate headTemplate;

    private  FreightTemplateDetail regionTemplate;
}
