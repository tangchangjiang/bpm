package org.o2.metadata.console.app.service;


import org.o2.metadata.console.app.bo.FreightBO;
import org.o2.metadata.console.app.bo.FreightDetailBO;
import org.o2.metadata.console.app.bo.FreightPriceBO;
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
     * 清除运费模板明细redis缓存
     *
     * @param freightDetailList 运费模板明细列表
     */
    void deleteFreightDetails(List<FreightDetailBO> freightDetailList);

    /**
     * 获取运费模板价格行缓存信息
     *
     * @param templateCode 运费模板编码
     * @param carrierCode  承运商编码
     * @param regionCode   地区编码
     * @return 运费模板价格行缓存信息
     */
    FreightPriceBO getFreightPrice(String templateCode,  String regionCode);

    /**
     * 获取运费模板明细缓存信息
     *
     * @param templateCode 运费模板编码
     * @param detailId  运费模板明细ID
     * @return  运费模板明细缓存信息
     */
    FreightDetailBO getFreightDetail(String templateCode, Long detailId);

    /**
     * 获取运费模板缓存信息(不包含运费模板明细)
     *
     * @param templateCode 运费模板编码
     * @return 运费模板缓存信息
     */
    FreightBO getFreight(String templateCode);

    /**
     * 获取默认的运费模板明细缓存信息
     *
     * @param templateCode 运费模板编码
     * @return 默认的运费模板明细缓存信息
     */
    FreightDetailBO getDefaultFreightDetail(String templateCode);
}
