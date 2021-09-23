package org.o2.metadata.console.app.service.impl;

import io.choerodon.mybatis.service.BaseServiceImpl;
import org.o2.metadata.console.api.dto.RegionQueryDTO;
import org.o2.metadata.console.api.dto.RegionQueryLovDTO;
import org.o2.metadata.console.api.vo.AreaRegionVO;
import org.o2.metadata.console.api.vo.RegionVO;
import org.o2.metadata.console.app.bo.RegionBO;
import org.o2.metadata.console.app.service.RegionService;
import org.o2.metadata.console.infra.convertor.RegionConverter;
import org.o2.metadata.console.infra.entity.Region;
import org.o2.metadata.console.infra.repository.RegionRepository;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author tingting.wang@hand-china.com 2019-3-25
 */
@Service
public class RegionServiceImpl extends BaseServiceImpl<Region> implements RegionService {

    private RegionRepository regionRepository;

    public RegionServiceImpl(RegionRepository regionRepository) {
        this.regionRepository = regionRepository;
    }

    @Override
    public List<RegionVO> treeRegionWithParent(final String countryIdOrCode, final String condition, final Integer enabledFlag,final Long tenantId) {
        List<Region> regionList = regionRepository.listRegionWithParent(countryIdOrCode, condition, enabledFlag, tenantId);
        return RegionConverter.poToVoListObjects(regionList);
    }


    @Override
    public List<AreaRegionVO> listAreaRegion(final String countryCode, final Integer enabledFlag, final Long tenantId) {
        //取出所有省份
        RegionQueryDTO queryDTO = new RegionQueryDTO();
        queryDTO.setCountryCode(countryCode);
        queryDTO.setEnabledFlag(enabledFlag);
        queryDTO.setLevelNumber(1);
        List<RegionVO> list = this.listChildren(queryDTO,tenantId);
        List<RegionBO> regionList = RegionConverter.voToBoListObjects(list);

        //市
        queryDTO.setLevelNumber(2);
        List<RegionBO> cityList = RegionConverter.voToBoListObjects(this.listChildren(queryDTO,tenantId));
        Map<String,List<RegionBO>> cityMap = cityList.stream().collect(Collectors.groupingBy(RegionBO::getParentRegionCode));

        for (RegionBO vo : regionList) {
            String code = vo.getRegionCode();
            vo.setChildren(cityMap.get(code));
        }
        AreaRegionVO vo = new AreaRegionVO();
        vo.setAreaCode(countryCode);
        vo.setAreaMeaning(list.get(0).getCountryName());
        vo.setRegionList(regionList);
        return Collections.singletonList(vo);
    }


    @Override
    public List<RegionVO> listChildren(RegionQueryDTO regionQueryDTO, Long organizationId) {
        RegionQueryLovDTO queryLovDTO = new RegionQueryLovDTO();
        queryLovDTO.setParentRegionId(regionQueryDTO.getParentRegionId());
        queryLovDTO.setCountryCode(regionQueryDTO.getCountryCode());
        queryLovDTO.setEnabledFlag(regionQueryDTO.getEnabledFlag());
        queryLovDTO.setTenantId(organizationId);
        queryLovDTO.setParentRegionCode(regionQueryDTO.getParentRegionCode());
        queryLovDTO.setLevelNumber(regionQueryDTO.getLevelNumber());
        List<Region> regionList = regionRepository.listRegionLov(queryLovDTO, organizationId);
        return RegionConverter.poToVoListObjects(regionList);
    }

}

