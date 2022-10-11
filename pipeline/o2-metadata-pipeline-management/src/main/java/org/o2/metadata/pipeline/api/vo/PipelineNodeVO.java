package org.o2.metadata.pipeline.api.vo;

import lombok.Data;
import org.apache.commons.lang3.StringUtils;

/**
 * 流程器节点VO
 *
 * @author mark.bao@hand-china.com 2019-03-22
 */
@Data
public class PipelineNodeVO {
    private Long nodeId;
    private String strategyType;
    private String actionType;
    private String curBeanId;
    private String nextBeanId;
    private String script;

    public String uniqueKey() {
        return uniqueKey(this.curBeanId, this.strategyType);
    }

    public static String uniqueKey(final String curBeanId, final String strategyType) {
        return curBeanId + "," + StringUtils.upperCase(strategyType);
    }
}
