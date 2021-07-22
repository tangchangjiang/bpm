package org.o2.metadata.api.vo;

import lombok.Data;

/**
 *
 * 运费模版信息
 *
 * @author yipeng.zhu@hand-china.com 2021-07-16
 **/
@Data
public class FreightInfoVO {
    /**
     * 运费模版编码
     */
    private  String freightTemplateCode;

    private  FreightTemplateVO headTemplate;

    private  FreightTemplateDetailVO regionTemplate;
}
