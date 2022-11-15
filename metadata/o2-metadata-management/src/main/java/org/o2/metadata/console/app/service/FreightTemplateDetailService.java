package org.o2.metadata.console.app.service;

import org.o2.metadata.console.infra.entity.FreightTemplateDetail;

import java.util.List;

/**
 * 运费模板明细服务
 *
 * @author peng.xu@hand-china.com 2019/5/17
 */
public interface FreightTemplateDetailService {

    /**
     * 新增运费模板明细
     *
     * @param freightTemplateDetailList 新增的运费模板明细列表
     * @param isRegion                  是否属于指定区域的运费模板明细列表
     * @return 插入成功的运费模板明细列表
     */
    List<FreightTemplateDetail> batchInsert(List<FreightTemplateDetail> freightTemplateDetailList, boolean isRegion);

    /**
     * 删除运费模板明细
     *
     * @param freightTemplateDetailList 待删除的运费模板明细列表
     */
    void batchDelete(List<FreightTemplateDetail> freightTemplateDetailList);

    /**
     * 根据运费模板ID，查询默认运费模板明细
     *
     * @param templateId 运费模板ID
     * @return 运费模板明细列表
     */
    List<FreightTemplateDetail> queryDefaultFreightTemplateDetail(Long templateId);

    /**
     * 根据运费模板ID，查询指定地区运费模板明细
     *
     * @param templateId 运费模板ID
     * @return 地区运费模板明细列表
     */
    List<FreightTemplateDetail> queryRegionFreightTemplateDetail(Long templateId);
}
