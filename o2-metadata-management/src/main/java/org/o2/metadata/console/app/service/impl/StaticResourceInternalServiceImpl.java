package org.o2.metadata.console.app.service.impl;

import org.apache.commons.collections4.CollectionUtils;
import org.hzero.mybatis.domian.Condition;
import org.hzero.mybatis.util.Sqls;
import org.o2.metadata.console.api.dto.StaticResourceQueryDTO;
import org.o2.metadata.console.api.dto.StaticResourceSaveDTO;
import org.o2.metadata.console.app.service.StaticResourceInternalService;
import org.o2.metadata.console.infra.convertor.StaticResourceConverter;
import org.o2.metadata.console.infra.entity.StaticResource;
import org.o2.metadata.console.infra.repository.StaticResourceRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import io.choerodon.core.oauth.DetailsHelper;
import io.choerodon.mybatis.service.BaseServiceImpl;

/**
 * description
 *
 * @author zhanpeng.jiang@hand-china.com
 * @date 2021/07/30 12:00
 */
@Service
public class StaticResourceInternalServiceImpl extends BaseServiceImpl<StaticResource> implements StaticResourceInternalService {

    private final StaticResourceRepository staticResourceRepository;

    public StaticResourceInternalServiceImpl(StaticResourceRepository staticResourceRepository) {
        this.staticResourceRepository = staticResourceRepository;
    }

    @Override
    public Map<String, String> queryResourceCodeUrlMap(StaticResourceQueryDTO staticResourceQueryDTO) {
        if (CollectionUtils.isEmpty(staticResourceQueryDTO.getResourceCodeList())) {
            return new HashMap<>();
        }
        List<String> resourceCodeList = staticResourceQueryDTO.getResourceCodeList();
        List<StaticResource> resourceList = staticResourceRepository.selectByCondition(Condition.builder(StaticResource.class)
                .andWhere(Sqls.custom()
                        .andIn(StaticResource.FIELD_RESOURCE_CODE, resourceCodeList)
                        .andEqualTo(StaticResource.FIELD_TENANT_ID, Optional.ofNullable(staticResourceQueryDTO.getTenantId()).orElse(DetailsHelper.getUserDetails().getTenantId()))
                        .andEqualTo(StaticResource.FIELD_LANG, staticResourceQueryDTO.getLang()))
                .build());
        return resourceList.stream().collect(Collectors.toMap(StaticResource::getResourceCode, StaticResource::getResourceUrl));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveResource(StaticResourceSaveDTO staticResourceSaveDTO) {
        final StaticResource staticResource = StaticResourceConverter.toStaticResource(staticResourceSaveDTO);
        staticResource.setTenantId(DetailsHelper.getUserDetails().getTenantId());

        // 根据resource_code查询是否已存在
        List<StaticResource> staticResources = staticResourceRepository.selectByCondition(Condition.builder(StaticResource.class)
                .andWhere(Sqls.custom()
                        .andEqualTo(StaticResource.FIELD_RESOURCE_CODE, staticResource.getResourceCode())
                        .andEqualTo(StaticResource.FIELD_TENANT_ID, staticResource.getTenantId())
                        .andEqualTo(StaticResource.FIELD_LANG, staticResource.getLang())
                ).build());

        int count = staticResources.size();
        if (count <= 0) {
            staticResourceRepository.insertSelective(staticResource);
        } else {
            StaticResource originResource = staticResources.get(0);
            staticResource.setObjectVersionNumber(originResource.getObjectVersionNumber());
            staticResource.setResourceId(originResource.getResourceId());
            staticResourceRepository.updateByPrimaryKey(staticResource);
        }
    }
}
