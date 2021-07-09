package org.o2.metadata.console.domain.repository;

import org.hzero.mybatis.base.BaseRepository;
import org.o2.metadata.console.api.vo.FreightTemplateVO;
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
    List<FreightTemplateVO> listFreightTemplates(final FreightTemplate freightTemplate);

    /**
     * 通过运费模板ID，查询运费模板
     *
     * @param templateId 运费模板ID
     * @return 运费模板
     */
    FreightTemplate selectyTemplateId(final Long templateId);

    /**
     * 运费模板是否关联平台产品
     *
     * @param freightTemplate 运费模板
     * @return
     */
    boolean isFreightTemplateRelatePro(final FreightTemplate freightTemplate);
}
