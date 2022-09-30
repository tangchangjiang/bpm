package org.o2.metadata.management.client.domain.co;

import lombok.Data;

/**
 *
 * 运费模版信息
 *
 * @author yipeng.zhu@hand-china.com 2021-07-16
 **/
@Data
public class FreightInfoCO {
    /**
     * 运费模版编码
     */
    private  String freightTemplateCode;

    private FreightTemplateCO headTemplate;

    private FreightTemplateDetailCO regionTemplate;
}
