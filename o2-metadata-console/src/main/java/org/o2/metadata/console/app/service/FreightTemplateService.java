package org.o2.metadata.console.app.service;

import org.o2.metadata.core.domain.entity.FreightTemplate;
import org.o2.metadata.core.domain.vo.FreightTemplateVO;

import java.util.List;

/**
 * 运费模板服务
 *
 * @author peng.xu@hand-china.com 2019/5/17
 */
public interface FreightTemplateService {

    /**
     * 根据主键查询运费模板和运费模板明细
     *
     * @param templateId 运费模板ID
     * @return 运费模板和运费模板明细
     */
    FreightTemplateVO queryTemplateAndDetails(final Long templateId);

    /**
     * 新增运费模板和运费模板明细
     *
     * @param freightTemplate 新增的运费模板和运费模板明细
     * @return 运费模板和运费模板明细
     */
    FreightTemplateVO createTemplateAndDetails(final FreightTemplateVO freightTemplate);

    /**
     * 更新运费模板和运费模板明细
     *
     * @param freightTemplate 更新的运费模板和运费模板明细
     * @return 运费模板和运费模板明细
     */
    FreightTemplateVO updateTemplateAndDetails(final FreightTemplateVO freightTemplate);

    /**
     * 批量删除运费模板和运费模板明细
     *
     * @param freightTemplateList 待删除的运费模板列表
     * @return boolean  是否删除成功
     */
    boolean removeTemplateAndDetails(final List<FreightTemplate> freightTemplateList);

    /**
     * 新增运费模板
     *
     * @param freightTemplateList 新增的运费模板列表
     * @return 运费模板列表
     */
    List<FreightTemplate> batchInsert(final List<FreightTemplate> freightTemplateList);

    /**
     * 更新运费模板
     *
     * @param freightTemplateList 待更新的运费模板列表
     * @return 运费模板列表
     */
    List<FreightTemplate> batchUpdate(final List<FreightTemplate> freightTemplateList);

    /**
     * 更新运费模板
     *
     * @param freightTemplateList 待创建或或更新的运费模板列表
     * @return 运费模板列表
     */
    List<FreightTemplate> batchMerge(final List<FreightTemplate> freightTemplateList);

    /**
     * 验证运费模板的运费明细
     * 运费模板不包邮时，返回true
     * 运费模板包邮时，验证有且只有一个默认运费行
     *
     * @param templateId 运费模板ID
     * @return
     */
    boolean uniqueDefaultValidate(final Long templateId);

    /**
     * 刷新运费模板缓存
     *
     * @param templateId 运费模板ID
     */
    void refreshCache(final Long templateId);


    /**
     * 根据主键查询运费模板和运费模板明细
     *
     * @param  Long organizationId 租户ID
     * @return 运费模板和运费模板明细
     */
    FreightTemplateVO querydefaultTemplate(final Long   organizationId);

}
