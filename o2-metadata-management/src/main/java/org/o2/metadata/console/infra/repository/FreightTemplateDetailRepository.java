package org.o2.metadata.console.infra.repository;

import org.apache.ibatis.annotations.Param;
import org.hzero.mybatis.base.BaseRepository;
import org.o2.metadata.console.infra.entity.FreightTemplateDetail;

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
     *  通过租户ID查询所有
     * @param tenantId 租户ID
     * @return list
     */
    List<FreightTemplateDetail> selectAllByTenantId(@Param("tenantId") Long tenantId);
}
