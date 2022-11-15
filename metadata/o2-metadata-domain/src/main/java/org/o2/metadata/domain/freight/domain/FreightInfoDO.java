package org.o2.metadata.domain.freight.domain;

/**
 * 运费模版信息
 *
 * @author yipeng.zhu@hand-china.com 2021-07-16
 **/

public class FreightInfoDO {
    /**
     * 运费模版编码
     */
    private String freightTemplateCode;

    private FreightTemplateDO headTemplate;

    private FreightTemplateDetailDO regionTemplate;

    public String getFreightTemplateCode() {
        return freightTemplateCode;
    }

    public void setFreightTemplateCode(String freightTemplateCode) {
        this.freightTemplateCode = freightTemplateCode;
    }

    public FreightTemplateDO getHeadTemplate() {
        return headTemplate;
    }

    public void setHeadTemplate(FreightTemplateDO headTemplate) {
        this.headTemplate = headTemplate;
    }

    public FreightTemplateDetailDO getRegionTemplate() {
        return regionTemplate;
    }

    public void setRegionTemplate(FreightTemplateDetailDO regionTemplate) {
        this.regionTemplate = regionTemplate;
    }

    @Override
    public String toString() {
        return "FreightInfoDO{" +
                "freightTemplateCode='" + freightTemplateCode + '\'' +
                ", headTemplate=" + headTemplate +
                ", regionTemplate=" + regionTemplate +
                '}';
    }
}
