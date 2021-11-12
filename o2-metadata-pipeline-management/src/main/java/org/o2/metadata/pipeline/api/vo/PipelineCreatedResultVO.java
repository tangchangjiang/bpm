package org.o2.metadata.pipeline.api.vo;

import lombok.Data;

/**
 *
 * 流程器vo
 *
 * @author yipeng.zhu@hand-china.com 2021-11-12
 **/
@Data
public class PipelineCreatedResultVO {
   private String code;

   private Boolean success;

   private String msg;
}
