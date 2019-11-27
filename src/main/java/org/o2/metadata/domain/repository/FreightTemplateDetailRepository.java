package org.o2.metadata.domain.repository;

import org.hzero.mybatis.base.BaseRepository;
import org.o2.ext.metadata.domain.entity.FreightTemplateDetail;

import java.util.List;

/**
 * 运费模板明细资源库
 *
 * @author peng.xu@hand-china.com 2019/5/16
 */
public interface FreightTemplateDetailRepository extends BaseRepository<FreightTemplateDetail> {

    /**
     * 根据运费模板ID，查询默认运费模板明细
     *
     * @param templateId 运费模板ID
     * @return 运费模板明细列表
     */
    List<FreightTemplateDetail> queryDefaultFreightTemplateDetail(final Long templateId);

    /**
     * 根据运费模板ID，查询指定地区运费模板明细
     *
     * @param templateId 运费模板ID
     * @return 地区运费模板明细列表
     */
    List<FreightTemplateDetail> queryRegionFreightTemplateDetail(final Long templateId);

    /**
     * 根据运费模板ID，查询所有运费模板明细
     *
     * @param templateId 运费模板ID
     * @return 默认运费模板明细列表
     */
    List<FreightTemplateDetail> queryFreightTemplateDetailByTemplateId(final Long templateId);

    /**
     * 查询其他默认的运费模板明细
     *
     * @param freightTemplateDetail 需要排除的默认运费模板明细
     * @return 其他默认的运费模板明细
     */
    List<FreightTemplateDetail> queryOtherDefaultFreightTemplateDetail(FreightTemplateDetail freightTemplateDetail);
}
