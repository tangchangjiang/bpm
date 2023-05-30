package org.o2.metadata.console.infra.repository.impl;

import org.apache.commons.collections4.CollectionUtils;
import org.hzero.core.base.BaseConstants;
import org.o2.metadata.console.infra.convertor.FreightConverter;
import org.o2.metadata.console.infra.entity.FreightInfo;
import org.o2.metadata.console.infra.redis.FreightRedis;
import org.o2.metadata.domain.freight.domain.FreightInfoDO;
import org.o2.metadata.domain.freight.domain.FreightTemplateDO;
import org.o2.metadata.domain.freight.repository.FreightTemplateDomainRepository;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 获取运费模版信息
 *
 * @author yipeng.zhu@hand-china.com 2021-07-19
 **/
@Component
public class FreightTemplateDomainRepositoryImpl implements FreightTemplateDomainRepository {
    private final FreightRedis freightRedis;

    public FreightTemplateDomainRepositoryImpl(FreightRedis freightRedis) {
        this.freightRedis = freightRedis;
    }

    @Override
    public FreightInfoDO getFreightTemplate(String regionCode, String templateCode, Long tenantId) {
        if (null == tenantId) {
            tenantId = BaseConstants.DEFAULT_TENANT_ID;
        }
        FreightInfo freightTemplate = freightRedis.getFreightTemplate(regionCode, templateCode, tenantId);
        // 如果当前租户没有运费模板数据，则查询0租户作为兜底
        if (null == freightTemplate.getHeadTemplate() && null == freightTemplate.getRegionTemplate() && !BaseConstants.DEFAULT_TENANT_ID.equals(tenantId)) {
            tenantId = BaseConstants.DEFAULT_TENANT_ID;
            freightTemplate = freightRedis.getFreightTemplate(regionCode, templateCode, tenantId);
        }
        return FreightConverter.poToDoObject(freightTemplate);
    }

    @Override
    public List<FreightTemplateDO> listFreightTemplate(Long tenantId, List<String> templateCodes) {
        List<FreightInfo> freightInfos = freightRedis.listFreightTemplate(tenantId, templateCodes);
        // 筛选未查询到运费数据的模板编码，查询0租户作为兜底数据
        if (freightInfos.size() < templateCodes.size()) {
            List<String> selectedTemplateCodes = freightInfos.stream().map(FreightInfo::getFreightTemplateCode).collect(Collectors.toList());
            templateCodes.removeIf(selectedTemplateCodes::contains);
            if (CollectionUtils.isNotEmpty(templateCodes)) {
                freightInfos.addAll(freightRedis.listFreightTemplate(BaseConstants.DEFAULT_TENANT_ID, templateCodes));
            }
        }
        List<FreightTemplateDO> freightTemplateDOs = new ArrayList<>();
        for (FreightInfo freightInfo : freightInfos) {
            FreightTemplateDO freightTemplateDO = FreightConverter.toFreightTemplateDO(freightInfo.getHeadTemplate());
            freightTemplateDOs.add(freightTemplateDO);
        }
        return freightTemplateDOs;
    }

}
