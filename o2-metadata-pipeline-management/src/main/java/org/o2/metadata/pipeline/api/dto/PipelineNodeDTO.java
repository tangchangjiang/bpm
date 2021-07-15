package org.o2.metadata.pipeline.api.dto;

import lombok.Data;
import org.apache.commons.lang3.StringUtils;


/**
 * 流程器节点
 *
 * @author wenjun.deng01@hand-china.com 2019-07-22
 */
@Data
public class PipelineNodeDTO {
    /**
     * 当前行为
     */
    private Long curAction;
    /**
     * 下一个节点行为行为
     */
    private Long nextAction;
    /**
     * 决策类型，值集 OBSM.PIPELINE_STRATEGY
     */
    private String strategyType;
    /**
     * 当前节点描述
     */
    private String curDescription;

    public String uniqueKey() {
        return uniqueKey(this.curAction, this.strategyType);
    }

    public static String uniqueKey(final Long curAction, final String strategyType) {
        return curAction + "," + StringUtils.upperCase(strategyType);
    }
}
