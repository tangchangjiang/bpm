package org.o2.metadata.console.app.bo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * 承运商物流成本计算BO
 *
 * @author zhilin.ren@hand-china.com 2022/10/12 11:56
 */
@Data
public class CarrierLogisticsCostBO {

    private String carrierCode;
    @ApiModelProperty(value = "重泡比")
    private Long volumeConversion;
    @ApiModelProperty(value = "关联运费模板，关联表o2md_freigth_template")
    private String templateCode;
    @ApiModelProperty(value = "计价方式，值集视图O2MD.UOM_TYPE")
    private String valuationType;
    @ApiModelProperty(value = "计价单位，值集视图O2MD.UOM")
    private String valuationUom;

    /**
     * 候选列表
     */
    private List<CarrierLogisticsCostDetailBO> carrierLogisticsCostDetailList;

    /**
     * 最终选择
     */
    private CarrierLogisticsCostDetailBO carrierLogisticsCostDetailBO;
}
