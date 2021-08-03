package org.o2.metadata.console.app.service.impl;

import io.choerodon.core.exception.CommonException;
import lombok.RequiredArgsConstructor;
import org.hzero.mybatis.domian.Condition;
import org.hzero.mybatis.util.Sqls;
import org.o2.metadata.console.infra.constant.MetadataConstants;
import org.o2.metadata.console.infra.entity.Platform;
import org.o2.metadata.console.infra.repository.PlatformRepository;
import org.springframework.transaction.annotation.Transactional;
import java.util.stream.Collectors;
import io.choerodon.mybatis.domain.AuditDomain;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.o2.metadata.console.app.service.PlatformService;
import java.util.List;


/**
 * 平台定义表应用服务默认实现
 *
 * @author zhilin.ren@hand-china.com 2021-08-02 11:11:28
 */
@Service
@RequiredArgsConstructor
public class PlatformServiceImpl implements PlatformService {

    private final PlatformRepository platformRepository;

    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<Platform> batchSave(List<Platform> platformList) {
        Map<AuditDomain.RecordStatus, List<Platform>> statusMap = platformList.stream().collect(Collectors.groupingBy(Platform::get_status));
        // 删除
        if (statusMap.containsKey(AuditDomain.RecordStatus.delete)) {
            List<Platform> deleteList = statusMap.get(AuditDomain.RecordStatus.delete);
            platformRepository.batchDeleteByPrimaryKey(deleteList);
        }
        // 更新
        if (statusMap.containsKey(AuditDomain.RecordStatus.update)) {
            List<Platform> updateList = statusMap.get(AuditDomain.RecordStatus.update);
            updateList.forEach(item -> {
                // TODO: 唯一性校验
                platformRepository.updateByPrimaryKeySelective(item);
            });
        }
        // 新增
        if (statusMap.containsKey(AuditDomain.RecordStatus.create)) {
            List<Platform> createList = statusMap.get(AuditDomain.RecordStatus.create);
            createList.forEach(item -> {
                // TODO: 唯一性校验
                platformRepository.insertSelective(item);
            });
        }
        return platformList;
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public Platform save(Platform platform) {
        // 唯一性校验
        Condition condition = Condition.builder(Platform.class).andWhere(Sqls.custom().andEqualTo(Platform.FIELD_PLATFORM_CODE,platform.getPlatformCode())
                .andEqualTo(Platform.FIELD_PLATFORM_STATUS_CODE,platform.getPlatformStatusCode())).build();
        int count = platformRepository.selectCountByCondition(condition);
        if (count > 0) {
            throw new CommonException(MetadataConstants.ErrorCode.O2MD_ERROR_CHECK_FAILED);
        }
        //保存平台定义表
        if (platform.getPlatformId() == null) {
            platformRepository.insertSelective(platform);
        } else {
            platformRepository.updateOptional(platform,
                    Platform.FIELD_PLATFORM_CODE,
                    Platform.FIELD_PLATFORM_NAME,
                    Platform.FIELD_PLATFORM_STATUS_CODE,
                    Platform.FIELD_TENANT_ID
            );
        }

        return platform;
    }
}
