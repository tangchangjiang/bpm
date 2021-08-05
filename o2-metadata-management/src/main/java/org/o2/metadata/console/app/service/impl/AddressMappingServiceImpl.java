package org.o2.metadata.console.app.service.impl;

import io.choerodon.core.exception.CommonException;
import org.apache.commons.lang3.StringUtils;
import org.o2.metadata.console.api.dto.AddressMappingDTO;
import org.o2.metadata.console.api.dto.RegionQueryLovDTO;
import org.o2.metadata.console.api.vo.AddressMappingVO;
import org.o2.metadata.console.app.service.AddressMappingService;
import org.o2.metadata.console.infra.constant.MetadataConstants;
import org.o2.metadata.console.infra.convertor.AddressMappingConverter;
import org.o2.metadata.console.infra.convertor.RegionConverter;
import org.o2.metadata.console.infra.entity.*;
import org.o2.metadata.console.api.vo.RegionTreeChildVO;
import org.o2.metadata.console.infra.mapper.AddressMappingMapper;
import org.o2.metadata.console.infra.repository.AddressMappingRepository;
import org.o2.metadata.console.infra.repository.CatalogRepository;
import org.o2.metadata.console.infra.repository.RegionRepository;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 地址匹配应用服务默认实现
 *
 * @author tingting.wang@hand-china.com 2019-3-25
 */
@Service
public class AddressMappingServiceImpl implements AddressMappingService {
    private final AddressMappingMapper addressMappingMapper;
    private final RegionRepository regionRepository;
    private final AddressMappingRepository addressMappingRepository;
    private final CatalogRepository catalogRepository;

    public AddressMappingServiceImpl(final AddressMappingMapper addressMappingMapper,
                                     RegionRepository regionRepository,
                                     AddressMappingRepository addressMappingRepository,
                                     CatalogRepository catalogRepository) {
        this.addressMappingMapper = addressMappingMapper;
        this.regionRepository = regionRepository;
        this.addressMappingRepository = addressMappingRepository;
        this.catalogRepository = catalogRepository;
    }

    /**
     * 地址匹配逆向递归 树状数据结构（根据parent id 分组 减少没必要的递归）
     *
     * @param addressMappingDTO 地址匹配查询条件
     * @return 地址匹配树状结果集
     */
    @Override
    public List<RegionTreeChildVO> findAddressMappingGroupByCondition(final AddressMappingDTO addressMappingDTO, final String countryCode) {
        if (addressMappingDTO.getCatalogCode() == null || "".equals(addressMappingDTO.getCatalogCode())) {
            throw new CommonException(MetadataConstants.ErrorCode.BASIC_DATA_CATALOG_CODE_IS_NULL);
        }
        if (countryCode == null || "".equals(countryCode)) {
            throw new CommonException("countryCode is null");
        }
        if (null == addressMappingDTO.getTenantId()) {
            throw new CommonException(MetadataConstants.ErrorCode.BASIC_DATA_TENANT_ID_IS_NULL);
        }
        List<RegionTreeChild> regionTreeChildList = addressMappingMapper.findAddressMappingByCondition(addressMappingDTO, countryCode);
        List<String> regionCodes = new ArrayList<>();
        for (RegionTreeChild regionTreeChild : regionTreeChildList) {
            if (StringUtils.isNotEmpty(regionTreeChild.getRegionCode())) {
                regionCodes.add(regionTreeChild.getRegionCode());
            }
        }
        //获取地区
        RegionQueryLovDTO dto = new RegionQueryLovDTO();
        dto.setTenantId(addressMappingDTO.getTenantId());
        dto.setRegionCodes(regionCodes);
        dto.setCountryCode(countryCode);
        List<Region> regionList = regionRepository.listRegionLov(dto,addressMappingDTO.getTenantId());
        Map<String,Region> regionMap = regionList.stream().collect(Collectors.toMap(Region::getRegionCode, region -> region));
        for (RegionTreeChild regionTreeChild : regionTreeChildList) {
            Region region = regionMap.get(regionTreeChild.getRegionCode());
            if (null == region) {
                continue;
            }
            regionTreeChild.setRegionName(region.getRegionName());
            regionTreeChild.setLevelPath(region.getLevelPath());
            regionTreeChild.setParentRegionId(region.getParentRegionId());
        }
        //根据parent id 分组
        final Map<Long, List<RegionTreeChild>> collect = regionTreeChildList.stream().peek(node -> {
            // 将parent id 为null 的替换成-1
            if (node.getParentRegionId() == null) {
                node.setParentRegionId(-1L);
            }
        }).collect(Collectors.groupingBy(RegionTreeChild::getParentRegionId));

        final List<RegionTreeChild> tree = new ArrayList<>();

        //递归获取树形结构数据
        getParent(collect, tree, addressMappingDTO.getCatalogCode(),addressMappingDTO.getTenantId());
        sortList(tree);
        return RegionConverter.poToVoChildObjects(tree);
    }

    @Override
    public AddressMappingVO addressMappingDetail(Long addressMappingId, String countryCode, Long tenantId) {
        final AddressMapping addressMapping = addressMappingRepository.selectByPrimaryKey(addressMappingId);
        RegionQueryLovDTO queryLovDTO = new RegionQueryLovDTO();
        queryLovDTO.setCountryCode(countryCode);
        queryLovDTO.setRegionCode(addressMapping.getRegionCode());
        queryLovDTO.setTenantId(tenantId);
        List<Region> regionList = regionRepository.listRegionLov(queryLovDTO,tenantId);
        final Catalog catalog = catalogRepository.selectOne(Catalog.builder().catalogCode(addressMapping.getCatalogCode()).tenantId(addressMapping.getTenantId()).build());
        addressMapping.setCatalogCode(catalog.getCatalogCode());
        addressMapping.setCatalogName(catalog.getCatalogName());
        if (!regionList.isEmpty()) {
            Region region = regionList.get(0);
            addressMapping.setRegionName(region.getRegionName());
            addressMapping.getRegionPathIds().add(region.getCountryId());
            addressMapping.getRegionPathCodes().add(region.getCountryCode());
            addressMapping.getRegionPathNames().add(region.getCountryName());
        }
        return AddressMappingConverter.poToVoObject(addressMapping);
    }

    /**
     * 排序返回结果集
     *
     * @param tree 树形返回结果集
     */
    private void sortList(final List<RegionTreeChild> tree) {
        //省排序
        Collections.sort(tree);
        //市排序
        for (final RegionTreeChild regionTreeChild : tree) {
            if (regionTreeChild.getChildren() != null && regionTreeChild.getChildren().size() > 1) {
                Collections.sort(regionTreeChild.getChildren());
            }
            //区排序
            if (regionTreeChild.getChildren() != null) {
                for (final RegionTreeChild cityTreeChild : regionTreeChild.getChildren()) {
                    if (cityTreeChild.getChildren() != null && cityTreeChild.getChildren().size() > 1) {
                        Collections.sort(cityTreeChild.getChildren());
                    }
                }
            }
        }
    }
    /**
     * 父级的地址匹配数据
     * @param parentRegions 父地区
     * @param type 类型
     * @param tenantId 租户ID
     * @return map
     */
    private Map<Long,List<RegionTreeChild>> getParentAddress(List<Region> parentRegions,
                                                             String type,
                                                             Long tenantId) {

        Map<String,Region> maps = parentRegions.stream().collect(Collectors.toMap(Region::getRegionCode, region -> region));
        List<String> regionCodes = new ArrayList<>();
        for (Region parentRegion : parentRegions) {
            regionCodes.add(parentRegion.getRegionCode());
        }
        List<RegionTreeChild> queryList = addressMappingMapper.listAddressMapping(regionCodes,type,tenantId);
        for (RegionTreeChild regionTreeChild : queryList) {
            Region region = maps.get(regionTreeChild.getRegionCode());
            if (null == region) {
                continue;
            }
            regionTreeChild.setRegionName(region.getRegionName());
            regionTreeChild.setLevelPath(region.getLevelPath());
            regionTreeChild.setParentRegionId(region.getParentRegionId());
            regionTreeChild.setRegionId(region.getRegionId());
        }
        return queryList.stream().collect(Collectors.groupingBy(RegionTreeChild::getRegionId));
    }
    /**
     * 获取父级地区
     * @param map 分组数据
     * @param tenantId 租户ID
     * @return
     */
    private List<Region> parentRegion(Map<Long, List<RegionTreeChild>> map,Long tenantId) {
        List<Long> parentRegionIds = new ArrayList<>();
        map.forEach((k,v)->{
            if (k != -1) {
                parentRegionIds.add(k);
            }
        });
        //fu
        RegionQueryLovDTO dto = new RegionQueryLovDTO();
        dto.setTenantId(tenantId);
        dto.setRegionIds(parentRegionIds);
        return regionRepository.listRegionLov(dto,tenantId);
    }
    /**
     * 获取父节点
     *
     * @param collect 根据条件查询结果集
     * @param tree    树形返回数据
     * @param type    平台类型
     */
    private void getParent(final Map<Long, List<RegionTreeChild>> collect,
                           final List<RegionTreeChild> tree,
                           final String type,
                           final Long tenantId) {

        List<Region> parentRegions = parentRegion(collect,tenantId);
        Map<Long,Region> parentRegionMap = parentRegions.stream().collect(Collectors.toMap(Region::getRegionId, region -> region));
        Map<Long,List<RegionTreeChild>> map = getParentAddress(parentRegions,type,tenantId);

        final List<RegionTreeChild> result = new ArrayList<>();
        for (Long key : collect.keySet()) {
            //根节点
            if (key == -1L) {
                tree.addAll(collect.get(key));
                continue;
            }
            //查询父节点
            final List<RegionTreeChild> parents = map.get(key);
            //处理不同平台同一数据
            RegionTreeChild parent = new RegionTreeChild();
            if (null == parents || parents.isEmpty()) {
                final Region region = parentRegionMap.get(key);
                parent.setParentRegionId(region.getParentRegionId());
                parent.setRegionCode(region.getRegionCode());
                parent.setRegionId(region.getRegionId());
                parent.setRegionName(region.getRegionName());
                continue;
            }
            if (parents.size() == 1) {
                parent = parents.get(0);
            } else {
                for (final RegionTreeChild treeChild : parents) {
                    if (type.equals(treeChild.getCatalogCode())) {
                        parent = treeChild;
                    }
                }
            }
            //如果tree 集合中有了这个父节点，直接赋值tree 集合中父节点的 children字段
            if (tree.contains(parent)) {
                tree.get(tree.indexOf(parent)).setChildren(collect.get(key));
                continue;
            }
            parent.setChildren(collect.get(key));
            result.add(parent);
        }
        //如果没有父节点 退出递归
        if (result.isEmpty()) {
            return;
        }
        //分组下一次数据
        final Map<Long, List<RegionTreeChild>> parentMap = new HashMap<>();
        for (RegionTreeChild node : result) {
            if (node.getParentRegionId() == null) {
                node.setParentRegionId(-1L);
            }
            parentMap.computeIfAbsent(node.getParentRegionId(), k -> new ArrayList<>()).add(node);
        }
        //递归调用
        getParent(parentMap, tree, type,tenantId);
    }


}
