package org.o2.metadata.console.app.service.impl;

import org.o2.metadata.console.infra.entity.StaticResource;
import org.o2.metadata.console.infra.repository.StaticResourceRepository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.stream.Collectors;
import io.choerodon.mybatis.domain.AuditDomain;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.o2.metadata.console.app.service.StaticResourceService;
import java.util.List;

import org.hzero.mybatis.helper.UniqueHelper;

/**
 * 静态资源文件表应用服务默认实现
 *
 * @author zhanpeng.jiang@hand-china.com 2021-07-30 11:11:38
 */
@Service
public class StaticResourceServiceImpl implements StaticResourceService {
                                                                
    @Autowired
    private StaticResourceRepository staticResourceRepository;


    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<StaticResource> batchSave(List<StaticResource> staticResourceList) {
        Map<AuditDomain.RecordStatus, List<StaticResource>> statusMap = staticResourceList.stream().collect(Collectors.groupingBy(StaticResource::get_status));
        // 删除
        if (statusMap.containsKey(AuditDomain.RecordStatus.delete)) {
            List<StaticResource> deleteList = statusMap.get(AuditDomain.RecordStatus.delete);
            staticResourceRepository.batchDeleteByPrimaryKey(deleteList);
        }
        // 更新
        if (statusMap.containsKey(AuditDomain.RecordStatus.update)) {
            List<StaticResource> updateList = statusMap.get(AuditDomain.RecordStatus.update);
            updateList.forEach(item -> {
                // TODO: 唯一性校验
                UniqueHelper.valid(item,StaticResource.O2MD_STATIC_RESOURCE_U1);
                staticResourceRepository.updateByPrimaryKeySelective(item);
            });
        }
        // 新增
        if (statusMap.containsKey(AuditDomain.RecordStatus.create)) {
            List<StaticResource> createList = statusMap.get(AuditDomain.RecordStatus.create);
            createList.forEach(item -> {
                // TODO: 唯一性校验
                UniqueHelper.valid(item,StaticResource.O2MD_STATIC_RESOURCE_U1);
                staticResourceRepository.insertSelective(item);
            });
        }
        return staticResourceList;
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public StaticResource save(StaticResource staticResource) {
        //保存静态资源文件表
        UniqueHelper.valid(staticResource,StaticResource.O2MD_STATIC_RESOURCE_U1);
        if (staticResource.getResourceId() == null) {
            staticResourceRepository.insertSelective(staticResource);
        } else {
            staticResourceRepository.updateOptional(staticResource,
                    StaticResource.FIELD_RESOURCE_CODE,
                    StaticResource.FIELD_SOURCE_MODULE_CODE,
                    StaticResource.FIELD_RESOURCE_URL,
                    StaticResource.FIELD_DESCRIPTION,
                    StaticResource.FIELD_TENANT_ID
            );
        }

        return staticResource;
    }
}
