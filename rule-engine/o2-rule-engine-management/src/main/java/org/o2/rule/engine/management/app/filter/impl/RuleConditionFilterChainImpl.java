package org.o2.rule.engine.management.app.filter.impl;

import org.o2.rule.engine.management.app.filter.FilterHandlerContext;
import org.o2.rule.engine.management.app.filter.FilterHandlerService;
import org.o2.rule.engine.management.app.filter.RuleConditionFilterChain;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.stereotype.Service;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author xiang.zhao@hand-chian.com 2022/10/17
 */
@Service
@ConditionalOnBean(value = {FilterHandlerService.class})
public class RuleConditionFilterChainImpl implements RuleConditionFilterChain {
    private final List<FilterHandlerService> filterHandlerServices;
    private final Map<String, List<FilterHandlerService>> codeTypeListMap;

    public RuleConditionFilterChainImpl(final List<FilterHandlerService> filterHandlerServices) {
        this.filterHandlerServices = filterHandlerServices;
        codeTypeListMap = new HashMap<>();
        init();
    }

    /**
     * 初始化
     */
    private void init() {
        final Map<String, List<FilterHandlerService>> listMap = filterHandlerServices.stream().
                sorted(Comparator.comparing(FilterHandlerService::getOrder)).
                collect(Collectors.groupingBy(FilterHandlerService::getHandle));
        this.codeTypeListMap.putAll(listMap);
    }

    @Override
    public List<FilterHandlerService> getHandlers(String... filterCodes) {
        final List<FilterHandlerService> serviceList = new ArrayList<>();
        for (String code : filterCodes) {
            serviceList.addAll(codeTypeListMap.getOrDefault(code, Collections.emptyList()));
        }
        return serviceList;
    }

    @Override
    public List<FilterHandlerService> getAllHandlers() {
        return null;
    }

    @Override
    public String filter(FilterHandlerContext filterHandlerContext, String... filterCodes) {
        return null;
    }
}
