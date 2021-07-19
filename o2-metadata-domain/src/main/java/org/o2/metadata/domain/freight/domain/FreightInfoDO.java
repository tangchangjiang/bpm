package org.o2.metadata.domain.freight.domain;


/**
 *
 * 运费模版信息
 *
 * @author yipeng.zhu@hand-china.com 2021-07-16
 **/

public class FreightInfoDO {
    /**
     * 运费模版编码
     */
    private  String freightTemplateCode;

    private  String headTemplate;

    private  String cityTemplate;

    private  String defaultTemplate;

    public String getFreightTemplateCode() {
        return freightTemplateCode;
    }

    public void setFreightTemplateCode(String freightTemplateCode) {
        this.freightTemplateCode = freightTemplateCode;
    }

    public String getHeadTemplate() {
        return headTemplate;
    }

    public void setHeadTemplate(String headTemplate) {
        this.headTemplate = headTemplate;
    }

    public String getCityTemplate() {
        return cityTemplate;
    }

    public void setCityTemplate(String cityTemplate) {
        this.cityTemplate = cityTemplate;
    }

    public String getDefaultTemplate() {
        return defaultTemplate;
    }

    public void setDefaultTemplate(String defaultTemplate) {
        this.defaultTemplate = defaultTemplate;
    }
}
