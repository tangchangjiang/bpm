package org.o2.metadata.console.app.service.lang;

/**
 * 多语言Operation
 * @author rui.ling@hand-china.com 2023/02/14
 */
public interface MultiLangOperation<T> {
    /**
     * 执行方法
     *
     * @param lang 语言
     */
    T execute(String lang);
}
