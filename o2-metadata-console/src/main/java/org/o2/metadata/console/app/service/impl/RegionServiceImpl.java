package org.o2.metadata.console.app.service.impl;

import io.choerodon.core.exception.CommonException;
import io.choerodon.mybatis.service.BaseServiceImpl;
import org.hzero.core.base.BaseConstants;
import org.o2.metadata.console.api.dto.AreaRegionDTO;
import org.o2.metadata.console.api.dto.RegionDTO;
import org.o2.metadata.console.app.service.RegionService;
import org.o2.metadata.core.domain.entity.Region;
import org.o2.metadata.core.domain.entity.RegionArea;
import org.o2.metadata.core.domain.repository.RegionAreaRepository;
import org.o2.metadata.core.domain.repository.RegionRelPosRepository;
import org.o2.metadata.core.domain.repository.RegionRepository;
import org.o2.metadata.core.domain.vo.RegionVO;
import org.o2.metadata.core.infra.constants.BasicDataConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

import static org.hzero.core.base.BaseConstants.ErrorCode.DATA_NOT_EXISTS;

/**
 * @author tingting.wang@hand-china.com 2019-3-25
 */
@Service
public class RegionServiceImpl extends BaseServiceImpl<Region> implements RegionService {
    private static final long ROOT_ID = -1L;

    private RegionRepository regionRepository;
    private RegionAreaRepository regionAreaRepository;
    private RegionRelPosRepository regionRelPosRepository;

    public RegionServiceImpl(RegionRepository regionRepository,RegionAreaRepository regionAreaRepository, RegionRelPosRepository regionRelPosRepository) {
        this.regionRepository = regionRepository;
        this.regionAreaRepository = regionAreaRepository;
        this.regionRelPosRepository = regionRelPosRepository;
    }

    @Override
    public List<Region> treeRegionWithParent(final String countryIdOrCode, final String condition, final Integer enabledFlag,final Long tenantId) {
        return recursiveBuildRegionTree(
                regionRepository.listRegionWithParent(countryIdOrCode, condition, enabledFlag,tenantId).stream().collect(Collectors.groupingBy(
                        item -> item.getParentRegionId() == null ? ROOT_ID : item.getParentRegionId())),
                ROOT_ID, new ArrayList<>());
    }

    @Override
    public List<Region> treeOnlineStoreUnbindRegion(Long organizationId, Long onlineStoreId) {
        return recursiveBuildRegionTree(
                regionRelPosRepository.listUnbindRegion(organizationId, onlineStoreId).stream().collect(Collectors.groupingBy(
                        item -> item.getParentRegionId() == null ? ROOT_ID : item.getParentRegionId())),
                ROOT_ID, new ArrayList<>());
    }

    @Override
    public List<AreaRegionDTO> listAreaRegion(final String countryIdOrCode, final Integer enabledFlag, final Long tenantId) {
        //取出所有省份
        final List<RegionVO> regionList = regionRepository.listChildren(countryIdOrCode, null, enabledFlag,tenantId);
        final Map<String, List<RegionDTO>> regionMap = new HashMap<>(16);
        String key;
        List<RegionDTO> list;
        for (final RegionVO regionVO : regionList) {
            key = regionVO.getAreaCode() == null ? "$OTHER_AREA$" : regionVO.getAreaCode();
            if (regionMap.containsKey(key)) {
                list = regionMap.get(key);
            } else {
                list = new ArrayList<>();
                regionMap.put(key, list);
            }
            list.add(new RegionDTO(regionVO.getRegionId(), regionVO.getRegionCode(), regionVO.getRegionName()));
        }
        final List<AreaRegionDTO> areaRegionList = new ArrayList<>(regionMap.size());
        for (final String areaCode : regionMap.keySet()) {
            final AreaRegionDTO areaRegionDTO = new AreaRegionDTO();
            areaRegionDTO.setAreaCode(areaCode);
            areaRegionDTO.setRegionList(regionMap.get(areaCode));
            areaRegionList.add(areaRegionDTO);
        }
        // 排序
        areaRegionList.sort(null);
        return areaRegionList;
    }

    @Override
    public Region createRegion(final Region region) {
        if (region.getParentRegionId() != null) {
            final Region parentRegion = regionRepository.selectByPrimaryKey(region.getParentRegionId());
            if (parentRegion == null) {
                throw new CommonException(DATA_NOT_EXISTS);
            }
            region.setLevelPath(parentRegion.getLevelPath() + BasicDataConstants.Constants.ADDRESS_SPLIT + region.getRegionCode());
        } else {
            region.setLevelPath(region.getRegionCode());
        }
        if (regionRepository.insert(region) != 1) {
            throw new CommonException(BaseConstants.ErrorCode.ERROR);
        }
        return region;
    }

    @Override
    public Region updateRegion(final Region region) {
        final Region exists = regionRepository.selectByPrimaryKey(region.getRegionId());
        if (exists == null) {
            throw new CommonException(DATA_NOT_EXISTS);
        }
//        region.setRegionCode(exists.getRegionCode());
//        region.setLevelPath(exists.getLevelPath());
//        if (this.updateByPrimaryKey(region) != 1) {
//            throw new CommonException(BaseConstants.ErrorCode.ERROR);
//        }
        // 更新大区定义
        RegionArea regionArea = new RegionArea();
        regionArea.setAreaCode(region.getAreaCode());
        regionArea.setEnabledFlag(region.getEnabledFlag());
        regionArea.setTenantId(region.getTenantId());
        regionArea.setRegionId(exists.getRegionId());
        regionArea.setRegionCode(exists.getRegionCode());
        regionArea.setRegionName(exists.getRegionName());
        regionAreaRepository.insertSelective(regionArea);
        return region;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<Region> disableOrEnable(final Region region) {
        // 默认启用
        final int enableFlag = region.getEnabledFlag() == null ? BaseConstants.Flag.NO : region.getEnabledFlag();
        final Region exists = regionRepository.selectByPrimaryKey(region.getRegionId());
        if (exists == null) {
            throw new CommonException(BaseConstants.ErrorCode.DATA_NOT_EXISTS);
        }
        region.setLevelPath(exists.getLevelPath());
        // 默认父级未启用禁止启用下级
        if (enableFlag == BaseConstants.Flag.YES && region.getLevelPath().contains(BasicDataConstants.Constants.ADDRESS_SPLIT)) {
            // 验证父级是否开启
            final List<Region> parentRegionList =
                    regionRepository.listRegionByLevelPath(resolveParentLevelPath(region.getLevelPath()),region.getTenantId());
            for (final Region parent : parentRegionList) {
                if (Objects.equals(BaseConstants.Flag.NO, parent.getEnabledFlag())) {
                    throw new CommonException(BasicDataConstants.ErrorCode.BASIC_DATA_PARENT_NOT_ENABLED);
                }
            }
        }
        final List<Region> regionList = regionRepository.listRegionChildrenByLevelPath(region.getLevelPath(),region.getTenantId());
        for (final Region item : regionList) {
            item.setCountryId(null);
            item.setRegionCode(null);
            item.setRegionName(null);
            item.setParentRegionId(null);
            item.setEnabledFlag(enableFlag);
        }
        return regionRepository.batchUpdateByPrimaryKeySelective(regionList);
    }

    @Override
    public Region getRegionByCode(final String regionCode) {
        final Region region = new Region();
        region.setRegionCode(regionCode);
        return regionRepository.selectOne(region);
    }

    /**
     * 递归构建地区定义树
     *
     * @param regionMap  地区定义Map
     * @param parentId   父地区定义ID
     * @param regionList 地区定义List
     * @return 地区定义树
     */
    private List<Region> recursiveBuildRegionTree(final Map<Long, List<Region>> regionMap, final long parentId,
                                                  final List<Region> regionList) {
        if (regionMap.containsKey(parentId)) {
            for (final Region region : regionMap.get(parentId)) {
                if (regionMap.containsKey(region.getRegionId())) {
                    region.setChildren(recursiveBuildRegionTree(regionMap, region.getRegionId(), new ArrayList<>()));
                    regionList.add(region);
                } else {
                    regionList.add(region);
                }
            }
        }
        return regionList;
    }

    /**
     * 获取所有父级路径
     *
     * @param levelPath 当前等级路径
     * @return 所有父级路径
     */
    private List<String> resolveParentLevelPath(String levelPath) {
        final List<String> levelPathList = new ArrayList<>();
        while (levelPath.contains(BasicDataConstants.Constants.ADDRESS_SPLIT)) {
            levelPath = levelPath.substring(0, levelPath.lastIndexOf(BasicDataConstants.Constants.ADDRESS_SPLIT));
            levelPathList.add(levelPath);
        }
        return levelPathList;
    }
}

