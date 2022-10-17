package org.o2.rule.engine.management.app.filter.impl;

import org.o2.rule.engine.management.app.filter.FilterHandlerContext;
import org.o2.rule.engine.management.app.filter.FilterHandlerService;
import org.springframework.stereotype.Service;

/**
 * @author xiang.zhao@hand-chian.com 2022/10/16
 */
@Service("baseStoreFilterChain")
public class BaseStoreFilterChain implements FilterHandlerService {

    @Override
    public String getHandle() {
        return null;
    }

    @Override
    public String filter(FilterHandlerContext filterHandlerContext) {
        return filterHandlerContext.getOnlineShopCode();
    }

    @Override
    public int getOrder() {
        return 0;
    }
}
