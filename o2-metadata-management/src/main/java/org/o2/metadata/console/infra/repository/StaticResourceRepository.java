package org.o2.metadata.console.infra.repository;

import org.hzero.mybatis.base.BaseRepository;
import org.o2.metadata.console.infra.entity.StaticResource;

import java.util.List;

/**
 * 静态资源文件表资源库
 *
 * @author zhanpeng.jiang@hand-china.com 2021-07-30 11:11:38
 */
public interface StaticResourceRepository extends BaseRepository<StaticResource> {

    /**
     * 查询满足条件的静态资源以及对应的配置信息的集合
     *
     * @param condition 静态资源
     * @return list of staticResource
     */
    List<StaticResource> listStaticResourceByCondition(StaticResource condition);
}
