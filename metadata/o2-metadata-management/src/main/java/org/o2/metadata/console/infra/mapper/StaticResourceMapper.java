package org.o2.metadata.console.infra.mapper;

import org.o2.metadata.console.api.co.StaticResourceAndConfigCO;
import org.o2.metadata.console.api.dto.StaticResourceListDTO;
import org.o2.metadata.console.infra.entity.StaticResource;


import io.choerodon.mybatis.common.BaseMapper;

import java.util.List;

/**
 * 静态资源文件表Mapper
 *
 * @author zhanpeng.jiang@hand-china.com 2021-07-30 11:11:38
 */
public interface StaticResourceMapper extends BaseMapper<StaticResource> {
    /**
     * 查询满足条件的静态资源以及对应的配置信息的集合
     *
     * @param condition 静态资源
     * @return list of staticResource
     */
    List<StaticResource> listStaticResourceByCondition(StaticResource condition);

    /**
     * 根据条件查询json_key和resourceUrl
     * @param staticResourceListDTO 查询条件
     * @return List<StaticResourceAndConfigCO> 结果
     */
    List<StaticResourceAndConfigCO> getStaticResourceAndConfig(StaticResourceListDTO staticResourceListDTO);
}
