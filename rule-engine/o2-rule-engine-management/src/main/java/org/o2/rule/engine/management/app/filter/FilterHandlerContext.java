package org.o2.rule.engine.management.app.filter;

import lombok.Data;
import org.o2.core.helper.UserHelper;
import org.o2.rule.engine.management.domain.entity.Rule;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 校验上下文
 *
 * @author wei.cai@hand-china.com 2021/8/4
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
    /**
     * SPU编码
     */
    private List<String> spuCodes;
    /**
     * 网店编码
     */
    private String onlineShopCode;

    public FilterHandlerContext(@NotNull Long tenantId, @NotNull Rule rule) {
        this.tenantId = tenantId;
        this.rule = rule;
        this.onlineShopCode = UserHelper.getOnlineShop();
    }

}
