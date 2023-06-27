package org.o2.metadata.console.app.bo;

import lombok.Data;

import java.util.Map;

/**
 * 运费模板业务对象
 *
 * @author peng.xu@hand-china.com 2019-06-18
 */
@Data
public class FreightBO {

    /**
     * 运费模板ID
     */
    private Long templateId;

    /**
     * 运费模板编码
     */
    private String templateCode;

    /**
     * 运费模板名称
     */
    private String templateName;

    /**
     * 是否包邮
     */
    private Integer deliveryFreeFlag;

    /**
     * 计价方式
     */
    private String valuationType;

    /**
     * 计价单位
     */
    private String valuationUom;

    /**
     * tenantId
     */
    private Long tenantId;
    /***
     * 默认运费模板标记，新建的时候默认为0"
     */
    private Integer dafaultFlag;

    private Map<String, Map<String, String>> tlsMap;


}
