package org.o2.metadata.console.infra.mapper;

import io.choerodon.mybatis.common.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.o2.metadata.console.infra.entity.FreightTemplateDetail;

import java.util.List;

/**
 * 运费模板明细Mapper
 *
 * @author peng.xu@hand-china.com 2019/5/16
 */
public interface FreightTemplateDetailMapper extends BaseMapper<FreightTemplateDetail> {

    /**
     * 根据运费模板ID，查询默认运费模板明细
     *
     * @param templateId 运费模板ID
     * @return 运费模板明细列表
     */
    List<FreightTemplateDetail> queryDefaultFreightTemplateDetails(@Param(value = "templateId") Long templateId);

    /**
     * 根据运费模板ID，查询地区运费模板明细
     *
     * @param templateId 运费模板ID
     * @return 运费模板明细列表
     */
    List<FreightTemplateDetail> queryRegionFreightTemplateDetails(@Param(value = "templateId") Long templateId);


    /**
     *  通过租户ID查询所有
     * @param tenantId 租户ID
     * @return list
     */
    List<FreightTemplateDetail> selectAllByTenantId(@Param("tenantId") Long tenantId);
}
