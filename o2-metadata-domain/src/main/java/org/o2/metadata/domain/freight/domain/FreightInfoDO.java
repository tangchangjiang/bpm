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

    private  String regionTemplate;

    public String getRegionTemplate() {
        return regionTemplate;
    }

    public void setRegionTemplate(String regionTemplate) {
        this.regionTemplate = regionTemplate;
    }

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

    @Override
    public String toString() {
        return "FreightInfoDO{" +
                "freightTemplateCode='" + freightTemplateCode + '\'' +
                ", headTemplate='" + headTemplate + '\'' +
                ", regionTemplate='" + regionTemplate + '\'' +
                '}';
    }
}
