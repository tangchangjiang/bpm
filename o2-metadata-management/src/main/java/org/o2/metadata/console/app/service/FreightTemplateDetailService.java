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
    List<FreightTemplateDetail> batchInsert(final List<FreightTemplateDetail> freightTemplateDetailList, final boolean isRegion);

    /**
     * 更新运费模板明细
     *
     * @param freightTemplateDetailList 更新的运费模板明细列表
     * @param isRegion                  是否属于指定区域的运费模板明细列表
     * @return 更新成功的运费模板明细列表
     */
    List<FreightTemplateDetail> batchUpdate(final List<FreightTemplateDetail> freightTemplateDetailList, final boolean isRegion);

    /**
     * 删除运费模板明细
     *
     * @param freightTemplateDetailList 待删除的运费模板明细列表
     */
    void batchDelete(final List<FreightTemplateDetail> freightTemplateDetailList);
}
