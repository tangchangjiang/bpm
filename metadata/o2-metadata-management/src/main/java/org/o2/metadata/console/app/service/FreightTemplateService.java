package org.o2.metadata.console.app.service;

import org.o2.metadata.console.api.co.FreightInfoCO;
import org.o2.metadata.console.api.co.FreightTemplateCO;
import org.o2.metadata.console.api.dto.FreightDTO;
import org.o2.metadata.console.api.vo.FreightTemplateManagementVO;
import org.o2.metadata.console.infra.entity.FreightTemplate;

import java.util.List;
import java.util.Map;

/**
 * 运费模板服务
 *
 * @author peng.xu@hand-china.com 2019/5/17
 */
public interface FreightTemplateService {

    /**
     * 根据主键查询运费模板和运费模板明细
     *
     * @param templateId     运费模板ID
     * @param organizationId 租户id
     * @return 运费模板和运费模板明细
     */
    FreightTemplateManagementVO queryTemplateAndDetails(Long templateId, Long organizationId);

    /**
     * 新增运费模板和运费模板明细
     *
     * @param freightTemplate 新增的运费模板和运费模板明细
     * @return 运费模板和运费模板明细
     */
    FreightTemplateManagementVO createTemplateAndDetails(FreightTemplateManagementVO freightTemplate);

    /**
     * 更新运费模板和运费模板明细
     *
     * @param freightTemplate 更新的运费模板和运费模板明细
     * @return 运费模板和运费模板明细
     */
    FreightTemplateManagementVO updateTemplateAndDetails(FreightTemplateManagementVO freightTemplate);

    /**
     * 批量删除运费模板和运费模板明细
     *
     * @param freightTemplateList 待删除的运费模板列表
     * @param tenantId            租户ID
     * @return boolean  是否删除成功
     */
    Boolean removeTemplateAndDetails(List<FreightTemplate> freightTemplateList, Long tenantId);

    /**
     * 新增运费模板
     *
     * @param freightTemplateList 新增的运费模板列表
     * @return 运费模板列表
     */
    List<FreightTemplate> batchInsert(List<FreightTemplate> freightTemplateList);

    /**
     * 更新运费模板
     *
     * @param freightTemplateList 待更新的运费模板列表
     * @return 运费模板列表
     */
    List<FreightTemplate> batchUpdate(List<FreightTemplate> freightTemplateList);

    /**
     * 更新运费模板
     *
     * @param freightTemplateList 待创建或或更新的运费模板列表
     * @return 运费模板列表
     */
    List<FreightTemplate> batchMerge(List<FreightTemplate> freightTemplateList);

    /**
     * 验证运费模板的运费明细
     * 运费模板不包邮时，返回true
     * 运费模板包邮时，验证有且只有一个默认运费行
     *
     * @param templateId 运费模板ID
     * @return
     */
    boolean uniqueDefaultValidate(Long templateId);

    /**
     * 刷新运费模板缓存
     *
     * @param templateId 运费模板ID
     */
    void refreshCache(Long templateId, Long tenantId);

    /**
     * 设置默默认运费模板
     *
     * @param organizationId 租户ID
     * @param templateId     模板ID
     */
    void setDefaultTemp(Long organizationId, Long templateId);

    /**
     * 根据主键查询运费模板和运费模板明细
     *
     * @param organizationId organizationId 租户ID
     * @return 运费模板和运费模板明细
     */
    FreightTemplateManagementVO queryDefaultTemplateDetail(Long organizationId);

    /**
     * 获取计价单位含义
     *
     * @param freightTemplates 模板
     * @param organizationId   租户ID
     */
    void tranLov(List<FreightTemplate> freightTemplates, Long organizationId);

    /**
     * 内部方法 获取运费模版
     *
     * @param freight 运费参数
     * @return 模版信息
     */
    FreightInfoCO getFreightTemplate(FreightDTO freight);

    /**
     * 内部方法 默认模版信息
     *
     * @param organizationId 租户ID
     * @return 默认模版信息
     */
    FreightTemplateCO getDefaultTemplate(Long organizationId);

    /**
     * 内部方法 批量获取运费模版
     *
     * @param templateCodes 运费模板编码
     * @return 模版信息
     */
    Map<String, FreightTemplateCO> listFreightTemplate(Long tenantId, List<String> templateCodes);
}
