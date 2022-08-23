package org.o2.metadata.console.app.service;


import org.o2.metadata.console.app.bo.FreightDetailBO;
import org.o2.metadata.console.app.bo.FreightTemplateBO;

import java.util.List;


/**
 * 运费模板缓存服务
 *
 * @author peng.xu@hand-china.com 2019-06-17
 */
public interface FreightCacheService {

    /**
     * 更新运费模板redis缓存
     *
     * @param freightTemplate 运费模板传输对象(包含运费模板和明细)
     */
    void saveFreight(FreightTemplateBO freightTemplate);

    /**
     * 更新运费模板明细redis缓存
     *
     * @param freightDetailList 运费模板明细列表
     */
    void saveFreightDetails(List<FreightDetailBO> freightDetailList);

    /**
     * 清除运费模板redis缓存
     *
     * @param freightTemplate  运费模板传输对象(包含运费模板和明细)
     */
    void deleteFreight(FreightTemplateBO freightTemplate);

    /**
     * 批量清除运费模板redis缓存
     *
     * @param freightTemplates  运费模板传输对象(包含运费模板和明细)
     */
    void deleteFreight(List<FreightTemplateBO> freightTemplates);

    /**
     * 清除运费模板明细redis缓存
     *
     * @param freightDetailList 运费模板明细列表
     */
    void deleteFreightDetails(List<FreightDetailBO> freightDetailList);

}
