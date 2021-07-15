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
     * 根据运费模板ID，查询所有运费模板明细
     *
     * @param templateId 运费模板ID
     * @return 运费模板明细列表
     */
    List<FreightTemplateDetail> queryFreightTemplateDetailByTemplateId(@Param(value = "templateId") Long templateId);

    /**
     * 查询其他默认的运费模板明细
     *
     * @param freightTemplateDetail 需要排除的默认运费模板明细
     * @return 其他默认的运费模板明细
     */
    List<FreightTemplateDetail> queryOtherDefaultFreightTemplateDetail(FreightTemplateDetail freightTemplateDetail);
}
