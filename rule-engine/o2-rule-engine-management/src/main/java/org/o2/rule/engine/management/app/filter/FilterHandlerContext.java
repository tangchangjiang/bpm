package org.o2.rule.engine.management.app.filter;

import lombok.Data;
import org.o2.rule.engine.management.domain.entity.Rule;
import javax.validation.constraints.NotNull;

/**
 * 上下文
 *
 * @author xiang.zhao@hand-china.com 2022/10/19
 */
@Data
public class FilterHandlerContext {
    /**
     * 规则定义
     */
    private Rule rule;
    /**
     * 租户ID
     */
    private Long tenantId;

    public FilterHandlerContext(@NotNull Long tenantId, @NotNull Rule rule) {
        this.tenantId = tenantId;
        this.rule = rule;
    }

}
