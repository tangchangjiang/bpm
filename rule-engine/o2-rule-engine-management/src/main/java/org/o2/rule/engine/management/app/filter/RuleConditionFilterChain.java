package org.o2.rule.engine.management.app.filter;

import java.util.List;

/**
 * 优惠券效验责任链
 *
 * @author yipeng.zhu@hand-china.com 2020/7/15
 */
public interface RuleConditionFilterChain {

    /**
     * 通过Code获取Service
     *
     * @param filterCodes filter编码
     * @return 多Service
     */
    List<FilterHandlerService> getHandlers(String... filterCodes);

    /**
     * 获取所有Service
     *
     * @return 多Service
     */
    List<FilterHandlerService> getAllHandlers();

    /**
     * 过滤
     *
     * @param filterHandlerContext 校验上下文
     * @param filterCodes 编码
     * @return 是否校验成功
     */
    String filter(FilterHandlerContext filterHandlerContext, String... filterCodes);

}
