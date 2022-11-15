package org.o2.metadata.console.infra.strategy;

import com.google.common.collect.Maps;
import io.choerodon.core.convertor.ApplicationContextHelper;
import io.choerodon.core.exception.CommonException;
import org.o2.core.O2CoreConstants;
import org.o2.metadata.console.infra.constant.MetadataConstants;
import org.o2.metadata.domain.staticresource.service.BaseBusinessTypeInterface;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class BusinessTypeStrategyDispatcher {

    private static Map<Class<? extends BaseBusinessTypeInterface>, Map<String, BaseBusinessTypeInterface>> baseMap;

    private BusinessTypeStrategyDispatcher() {
    }

    public static void init() {
        final Map<String, BaseBusinessTypeInterface> interfaceMap =
                ApplicationContextHelper.getContext().getBeansOfType(BaseBusinessTypeInterface.class);
        Collection<BaseBusinessTypeInterface> businessTypeInterfaceList = interfaceMap.values();
        final Map<Class<? extends BaseBusinessTypeInterface>, List<BaseBusinessTypeInterface>> classListMap =
                businessTypeInterfaceList.stream().collect(Collectors.groupingBy(BaseBusinessTypeInterface::getHandlerClass));
        baseMap = Maps.newHashMapWithExpectedSize(classListMap.size());
        classListMap.forEach((clazz, businessClassList) -> {
            final Map<String, BaseBusinessTypeInterface> businessStrategyMap =
                    businessClassList.stream().collect(Collectors.toMap(BaseBusinessTypeInterface::getBusinessTypeCode, Function.identity(), (k1,
                                                                                                                                              k2) -> k1));
            baseMap.put(clazz, businessStrategyMap);
        });
    }

    public static <T extends BaseBusinessTypeInterface> T getService(final String businessTypeCode, Class<T> clazz) {
        final Map<String, BaseBusinessTypeInterface> businessStrategyMap = baseMap.get(clazz);
        if (businessStrategyMap == null) {
            throw new CommonException(MetadataConstants.ErrorCode.BUSINESS_STRATEGY_INIT_WRONG);
        }
        final T serviceImpl = (T) businessStrategyMap.getOrDefault(businessTypeCode, businessStrategyMap.get(O2CoreConstants.BusinessType.B2C));
        if (serviceImpl == null) {
            throw new CommonException(MetadataConstants.ErrorCode.BUSINESS_SERVICE_STRATEGY_FAILED);
        }
        return serviceImpl;
    }

}
