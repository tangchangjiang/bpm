package org.o2.metadata.console.infra.repository.impl;

import io.choerodon.core.domain.Page;
import io.choerodon.core.exception.CommonException;
import io.choerodon.mybatis.pagehelper.PageHelper;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import lombok.extern.slf4j.Slf4j;
import org.hzero.mybatis.base.impl.BaseRepositoryImpl;
import org.hzero.mybatis.helper.SecurityTokenHelper;
import org.hzero.mybatis.helper.UniqueHelper;
import org.o2.core.response.BatchResponse;
import org.o2.metadata.console.infra.constant.MetadataConstants;
import org.o2.metadata.console.infra.entity.Pos;
import org.o2.metadata.console.infra.entity.Region;
import org.o2.metadata.console.infra.entity.RegionRelPos;
import org.o2.metadata.console.infra.repository.RegionRelPosRepository;
import org.o2.metadata.console.infra.mapper.RegionRelPosMapper;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.TreeSet;
import java.util.stream.Collectors;

/**
 * 默认服务点配置 资源库实现
 *
 * @author wei.cai@hand-china.com 2020-01-09 15:41:36
 */
@Component
@Slf4j
public class RegionRelPosRepositoryImpl extends BaseRepositoryImpl<RegionRelPos> implements RegionRelPosRepository {

    private RegionRelPosMapper regionRelPosMapper;

    public RegionRelPosRepositoryImpl(RegionRelPosMapper regionRelPosMapper) {
        this.regionRelPosMapper = regionRelPosMapper;
    }

    @Override
    public Page<RegionRelPos> listByCondition(PageRequest pageRequest, RegionRelPos regionRelPos) {
        return PageHelper.doPage(pageRequest, () -> regionRelPosMapper.selectByStoreId(regionRelPos));
    }

    @Override
    public List<Region> listUnbindRegion(Long organizationId, Long onlineStoreId) {

        return regionRelPosMapper.selectUnbindRegionByStoreId(organizationId, onlineStoreId);
    }

    @Override
    public Page<Pos> listUnbindPos(PageRequest pageRequest, final RegionRelPos regionRelPos) {
        return PageHelper.doPage(pageRequest, () -> regionRelPosMapper.selectUnbindPosByRegionId(regionRelPos));
    }

    @Override
    public BatchResponse<RegionRelPos> batchCreate(Long organizationId, List<RegionRelPos> regionRelPos) {
        //判断级别是重复
        List<RegionRelPos> regions = regionRelPos.stream().collect(Collectors.collectingAndThen(Collectors.toCollection(()
                -> new TreeSet<>(Comparator.comparing(RegionRelPos::getPriority))), ArrayList::new));
        if (regionRelPos.size() != regions.size()) {
            throw new CommonException(MetadataConstants.ErrorCode.BASIC_DATA_DUPLICATE_CODE, "Priority");
        }
        final BatchResponse<RegionRelPos> batchResponse = new BatchResponse<>();
        for (RegionRelPos relPos : regionRelPos) {
            relPos.setTenantId(organizationId);
            if (relPos.isNew()) {
                //校验是否有重复数据
                if (UniqueHelper.valid(relPos)) {
                    try {
                        this.insertSelective(relPos);
                        batchResponse.addSuccessBody(relPos);
                    } catch (Exception e) {
                        log.error("BatchCreate Error", e);
                        batchResponse.addFailedBody(relPos);
                    }
                } else {
                    batchResponse.addFailedBody(relPos);

                }
            } else {
                try {
                    //仅更新标识符
                    SecurityTokenHelper.validToken(relPos);
                    this.updateOptional(relPos, RegionRelPos.FIELD_ACTIVE_FLAG);
                    batchResponse.addSuccessBody(relPos);
                } catch (Exception e) {
                    log.error("BatchUpdate Error", e);
                    batchResponse.addFailedBody(relPos);
                }
            }
        }
        return batchResponse;
    }

    @Override
    public BatchResponse<RegionRelPos> batchUpdate(Long organizationId, List<RegionRelPos> regionRelPos) {
        final BatchResponse<RegionRelPos> batchResponse = new BatchResponse<>();
        for (RegionRelPos relPos : regionRelPos) {
            relPos.setTenantId(organizationId);
            try {
                //仅更新标识符
                this.updateOptional(relPos, RegionRelPos.FIELD_ACTIVE_FLAG);
                batchResponse.addSuccessBody(relPos);
            } catch (Exception e) {
                batchResponse.addFailedBody(relPos);
            }
        }
        return batchResponse;
    }
}
