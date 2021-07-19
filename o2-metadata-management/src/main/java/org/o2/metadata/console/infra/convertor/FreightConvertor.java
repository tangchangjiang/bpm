package org.o2.metadata.console.infra.convertor;

import org.o2.metadata.console.api.vo.FreightInfoVO;
import org.o2.metadata.console.infra.entity.FreightInfo;
import org.o2.metadata.domain.freight.domain.FreightInfoDO;

/**
 *
 * 运费模版转换
 *
 * @author yipeng.zhu@hand-china.com 2021-07-19
 **/
public class FreightConvertor {
    public static FreightInfoDO poToDoObject(FreightInfo freightInfo) {

        if (freightInfo == null) {
            return null;
        }
        FreightInfoDO freightInfoDO = new FreightInfoDO();
        freightInfoDO.setFreightTemplateCode(freightInfo.getFreightTemplateCode());
        freightInfoDO.setHeadTemplate(freightInfo.getHeadTemplate());
        freightInfoDO.setRegionTemplate(freightInfo.getRegionTemplate());
        return freightInfoDO;
    }

    public static FreightInfoVO doToVoObject(FreightInfoDO freightInfoDO) {

        if (freightInfoDO == null) {
            return null;
        }
        FreightInfoVO freightInfoVO = new FreightInfoVO();
        freightInfoVO.setFreightTemplateCode(freightInfoDO.getFreightTemplateCode());
        freightInfoVO.setHeadTemplate(freightInfoDO.getHeadTemplate());
        freightInfoVO.setRegionTemplate(freightInfoDO.getRegionTemplate());
        return freightInfoVO;
    }
}
