package org.o2.feignclient.metadata.domain.vo;

import lombok.Data;

import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) {return true;}
        if (o == null || getClass() != o.getClass()) {return false;}
        FreightInfoVO that = (FreightInfoVO) o;
        return freightTemplateCode.equals(that.freightTemplateCode) &&
                headTemplate.equals(that.headTemplate) &&
                regionTemplate.equals(that.regionTemplate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(freightTemplateCode, headTemplate, regionTemplate);
    }
}
