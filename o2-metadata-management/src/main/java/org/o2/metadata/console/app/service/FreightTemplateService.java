package org.o2.metadata.console.app.service;

import org.o2.metadata.console.api.dto.FreightDTO;
import org.o2.metadata.console.api.vo.FreightInfoVO;
import org.o2.metadata.console.api.vo.FreightTemplateManagementVO;
import org.o2.metadata.console.api.vo.FreightTemplateVO;
import org.o2.metadata.console.infra.entity.FreightTemplate;

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
     * @param organizationId 租户id
     * @return 运费模板和运费模板明细
     */
    FreightTemplateManagementVO queryTemplateAndDetails(final Long templateId, Long organizationId);

    /**
     * 新增运费模板和运费模板明细
     *
     * @param freightTemplate 新增的运费模板和运费模板明细
     * @return 运费模板和运费模板明细
     */
    FreightTemplateManagementVO createTemplateAndDetails(final FreightTemplateManagementVO freightTemplate);

    /**
     * 更新运费模板和运费模板明细
     *
     * @param freightTemplate 更新的运费模板和运费模板明细
     * @return 运费模板和运费模板明细
     */
    FreightTemplateManagementVO updateTemplateAndDetails(final FreightTemplateManagementVO freightTemplate);

    /**
     * 批量删除运费模板和运费模板明细
     *
     * @param freightTemplateList 待删除的运费模板列表
     * @param tenantId 租户ID
     * @return boolean  是否删除成功
     */
    Boolean removeTemplateAndDetails(final List<FreightTemplate> freightTemplateList, Long tenantId);

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
     * 设置默默认运费模板
     *
     * @param organizationId
     * @param templateId
     */
    void setDefaultTemp(final Long organizationId, final Long templateId);

    /**
     * 根据主键查询运费模板和运费模板明细
     *
     * @param organizationId organizationId 租户ID
     * @return 运费模板和运费模板明细
     */
    FreightTemplateManagementVO querydefaultTemplate(final Long organizationId);


    /**
     * 手动转换值集视图
     * @param freightTemplates
     * @param organizationId
     */
    void tranLov(List<FreightTemplate> freightTemplates,Long organizationId);

    /**
     * 内部方法 获取运费模版
     * @param  freight  运费参数
     * @return 模版信息
     */
    FreightInfoVO getFreightTemplate(FreightDTO freight);

    /**
     * 默认模版信息
     * @param  organizationId 租户ID
     * @return  默认模版信息
     */
    FreightTemplateVO getDefaultTemplate(Long organizationId);
}
