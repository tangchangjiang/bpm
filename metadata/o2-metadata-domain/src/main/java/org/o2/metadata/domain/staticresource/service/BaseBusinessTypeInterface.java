package org.o2.metadata.domain.staticresource.service;

public interface BaseBusinessTypeInterface {
    /**
     * 获取业务类型
     *
     * @return class
     */
    String getBusinessTypeCode();

    /**
     * 获取处理class
     *
     * @return class
     */
    Class<? extends BaseBusinessTypeInterface> getHandlerClass();
}
