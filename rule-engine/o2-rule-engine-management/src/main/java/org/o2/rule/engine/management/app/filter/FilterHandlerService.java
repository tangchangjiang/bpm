package org.o2.rule.engine.management.app.filter;

import org.springframework.core.Ordered;

/**
 * 规则过滤策略服务
 *
 * @author yipeng.zhu@hand-china.com 2022-10-15
 **/
public interface FilterHandlerService extends Ordered {

    /**
     * 获取处理handle
     *
     * @return 编码
     */
    String getHandle();

    /**
     * 获取结果
     *
     * @param filterHandlerContext    上下文
     * @return 是否校验成功
     */
    String filter(FilterHandlerContext filterHandlerContext);

}
