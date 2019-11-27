package org.o2.metadata.infra.mapper;

import io.choerodon.mybatis.common.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.o2.ext.metadata.domain.entity.FreightTemplate;
import org.o2.ext.metadata.domain.vo.FreightTemplateVO;

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
    List<FreightTemplateVO> listFreightTemplates(FreightTemplate freightTemplate);

    /**
     * 根据运费模板ID，查询运费模板
     *
     * @param templateId 运费模板ID
     * @return
     */
    FreightTemplate queryFreightTemplateById(@Param(value = "templateId") Long templateId);

    /**
     * 查询运费模板关联上架平台商品的数量
     *
     * @param freightTemplate 运费模板
     * @return
     */
    int freightTemplateRelateProCount(FreightTemplate freightTemplate);
}
