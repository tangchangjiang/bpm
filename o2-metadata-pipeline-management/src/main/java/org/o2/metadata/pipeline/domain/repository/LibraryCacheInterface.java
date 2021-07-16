package org.o2.metadata.pipeline.domain.repository;

/**
 * 库文件缓存接口
 *
 * @author wei.cai@hand-china.com 2020/5/9
 */
public interface LibraryCacheInterface<T> {

    /**
     * 对库文件进行缓存
     * @param t 缓存对象
     */
    void cache(T t);

    /**
     * 移除库文件缓存
     * @param t 缓存对象
     */
    void removeCache(T t);

    /**
     * 获取缓存key
     * @param t 缓存队形
     * @return 返回缓存key
     */
    String cacheKey(T t);

}
