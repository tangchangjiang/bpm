package org.o2.metadata.console.infra.mapper;

import io.choerodon.mybatis.common.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.o2.metadata.console.api.vo.FreightTemplateManagementVO;
import org.o2.metadata.console.infra.entity.FreightTemplate;

import java.util.List;

/**
 * 运费模板Mapper
 *
 * @author peng.xu@hand-china.com 2019/5/15
 */
public interface FreightTemplateMapper extends BaseMapper<FreightTemplate> {

    /**
     * 根据查询条件返回运费模板视图列表
     *
     * @param freightTemplate 查询条件
     * @return 运费模板视图列表
     */
    List<FreightTemplateManagementVO> listFreightTemplates(FreightTemplate freightTemplate);

    /**
     * 根据运费模板ID，查询运费模板
     *
     * @param templateId 运费模板ID
     * @return
     */
    FreightTemplate queryFreightTemplateById(@Param(value = "templateId") Long templateId);


    /**
     * 默认模版信息
     * @param  tenantId 租户ID
     * @return  默认模版信息
     */
    FreightTemplate getDefaultTemplate(@Param(value = "tenantId")Long tenantId);

    /**
     *  通过租户ID查询所有
     * @param tenantId 租户ID
     * @return list
     */
    List<FreightTemplate> selectAllByTenantId(@Param("tenantId") Long tenantId);
}
