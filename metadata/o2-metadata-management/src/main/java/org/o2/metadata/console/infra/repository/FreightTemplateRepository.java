package org.o2.metadata.console.infra.repository;

import org.hzero.mybatis.base.BaseRepository;
import org.o2.metadata.console.api.vo.FreightTemplateManagementVO;
import org.o2.metadata.console.infra.entity.FreightTemplate;

import java.util.List;

/**
 * 运费模板资源库
 *
 * @author peng.xu@hand-china.com 2019/5/15
 */
public interface FreightTemplateRepository extends BaseRepository<FreightTemplate> {

    /**
     * 运费模板查询
     *
     * @param freightTemplate 运费模板查询条件
     * @return 运费模板列表
     */
    List<FreightTemplateManagementVO> listFreightTemplates(FreightTemplate freightTemplate);

    /**
     * 通过运费模板ID，查询运费模板
     *
     * @param templateId 运费模板ID
     * @return 运费模板
     */
    FreightTemplate selectTemplateId(Long templateId);

    /**
     * 默认模版信息
     *
     * @param organizationId 租户ID
     * @return 默认模版信息
     */
    FreightTemplate getDefaultTemplate(Long organizationId);

    /**
     * 通过租户ID查询所有
     *
     * @param tenantId 租户ID
     * @return list
     */
    List<FreightTemplate> selectAllByTenantId(Long tenantId);

    List<FreightTemplate> selectTemplateForRefresh(Long tenantId);
}
