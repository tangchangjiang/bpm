package org.o2.metadata.console.job;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.hzero.boot.platform.lov.adapter.LovAdapter;
import org.hzero.boot.platform.lov.dto.LovValueDTO;
import org.hzero.boot.scheduler.infra.annotation.JobHandler;
import org.hzero.boot.scheduler.infra.enums.ReturnT;
import org.hzero.boot.scheduler.infra.handler.IJobHandler;
import org.hzero.boot.scheduler.infra.tool.SchedulerTool;
import org.hzero.core.base.BaseConstants;
import org.o2.metadata.core.domain.entity.Region;
import org.o2.metadata.core.domain.entity.RegionArea;
import org.o2.metadata.core.domain.repository.RegionAreaRepository;
import org.o2.metadata.core.domain.repository.RegionRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Map;

/**
 * 地区大区
 *
 * @author houlin.cheng@hand-china.com
 */
@Slf4j
@JobHandler(value = "regionAreaInitialJob")
public class RegionAreaInitialJob implements IJobHandler {
    private static final String AREA_CODE = "O2MD.AREA_CODE";
    private static final String TENANT_ID = "tenantId";

    @Autowired
    private LovAdapter lovAdapter;
    @Autowired
    private RegionRepository regionRepository;
    @Autowired
    private RegionAreaRepository regionAreaRepository;

    @Override
    public ReturnT execute(Map<String, String> map, SchedulerTool tool) {
        final String organizationId = map.get(TENANT_ID);
        if (StringUtils.isBlank(organizationId)) {
            tool.error("Parameter [tenantId] can't be null. Please check job configuration.");
            return ReturnT.FAILURE;
        }

//        {
//            "tenantId":"2",
//            "HB":"10000,20000,30000",
//            "HD":"10000,20000,30000",
//            "HN":"10000,20000,30000"
//        }

        final Long tenantId = Long.parseLong(organizationId);

        List<LovValueDTO> lovValues = lovAdapter.queryLovValue(AREA_CODE, tenantId);
        if (CollectionUtils.isNotEmpty(lovValues)) {
            lovValues.forEach(item -> {
                String areaCode = item.getValue();

                String regionCodes = map.get(areaCode);
                if (null != regionCodes) {
                    String[] regionCodeArr = regionCodes.split(BaseConstants.Symbol.COMMA);
                    for (String regionCode : regionCodeArr) {
                        // 校验regionCode合法性
                        final Region param = new Region();
                        param.setTenantId(tenantId);
                        param.setRegionCode(regionCode);
                        List<Region> exists = regionRepository.select(param);
                        if (CollectionUtils.isEmpty(exists)) {
                            continue;
                        }

                        // 判断是否已存在大区数据
                        final RegionArea areaParam = new RegionArea();
                        areaParam.setTenantId(tenantId);
                        areaParam.setRegionCode(regionCode);
                        areaParam.setAreaCode(areaCode);
                        List<RegionArea> areaExists = regionAreaRepository.select(areaParam);
                        if (CollectionUtils.isEmpty(areaExists)) {
                            // 新建大区定义
                            RegionArea regionArea = new RegionArea();
                            regionArea.setAreaCode(areaCode);
                            regionArea.setEnabledFlag(BaseConstants.Flag.YES);
                            regionArea.setTenantId(tenantId);
                            regionArea.setRegionId(exists.get(0).getRegionId());
                            regionArea.setRegionCode(exists.get(0).getRegionCode());
                            regionArea.setRegionName(exists.get(0).getRegionName());
                            regionAreaRepository.insertSelective(regionArea);
                        }
                    }
                }
            });
        }

        return ReturnT.SUCCESS;
    }
}
