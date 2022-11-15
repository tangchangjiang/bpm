package org.o2.metadata.console.infra.redis;

import org.o2.metadata.console.infra.entity.FreightInfo;
import org.o2.metadata.console.infra.entity.FreightTemplate;
import org.o2.metadata.console.infra.entity.FreightTemplateDetail;

import java.util.List;

/**
 * 运费模版信息
 *
 * @author yipeng.zhu@hand-china.com 2021-07-19
 **/
public interface FreightRedis {
    /**
     * 获取运费模版信息
     *
     * @param regionCode   地区ID
     * @param templateCode 模版ID
     * @param tenantId     租户
     * @return 运费信息
     * @date 2021-07-19
     */
    FreightInfo getFreightTemplate(String regionCode, String templateCode, Long tenantId);

    /**
     * 更新
     *
     * @param templateList 模版头
     * @param detailList   模版行
     * @param tenantId     租户ID
     */
    void batchUpdateRedis(List<FreightTemplate> templateList, List<FreightTemplateDetail> detailList, Long tenantId);

    /**
     * 批量获取运费模版信息
     *
     * @param templateCodes 模版ID
     * @param tenantId      租户
     * @return 运费信息
     */
    List<FreightInfo> listFreightTemplate(Long tenantId, List<String> templateCodes);
}
