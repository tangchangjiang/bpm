package org.o2.metadata.console.app.service.impl;

import com.google.common.base.Preconditions;
import io.choerodon.core.exception.CommonException;
import org.apache.commons.lang3.StringUtils;
import org.hzero.core.base.BaseConstants;
import org.o2.metadata.console.api.dto.AddressMappingQueryDTO;
import org.o2.metadata.console.api.dto.AddressMappingQueryInnerDTO;
import org.o2.metadata.console.api.dto.RegionQueryLovDTO;
import org.o2.metadata.console.api.vo.AddressMappingVO;
import org.o2.metadata.console.api.vo.RegionTreeChildVO;
import org.o2.metadata.console.app.service.AddressMappingService;
import org.o2.metadata.console.infra.constant.MetadataConstants;
import org.o2.metadata.console.infra.constant.RegionConstants;
import org.o2.metadata.console.infra.convertor.AddressMappingConverter;
import org.o2.metadata.console.infra.convertor.RegionConverter;
import org.o2.metadata.console.infra.entity.*;
import org.o2.metadata.console.infra.mapper.AddressMappingMapper;
import org.o2.metadata.console.infra.repository.AddressMappingRepository;
import org.o2.metadata.console.infra.repository.CatalogRepository;
import org.o2.metadata.console.infra.repository.PlatformRepository;
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
    private final PlatformRepository platformRepository;

    public AddressMappingServiceImpl(final AddressMappingMapper addressMappingMapper,
                                     RegionRepository regionRepository,
                                     AddressMappingRepository addressMappingRepository,
                                     CatalogRepository catalogRepository, PlatformRepository platformRepository) {
        this.addressMappingMapper = addressMappingMapper;
        this.regionRepository = regionRepository;
        this.addressMappingRepository = addressMappingRepository;
        this.catalogRepository = catalogRepository;
        this.platformRepository = platformRepository;
    }

    /**
     * 地址匹配逆向递归 树状数据结构（根据parent id 分组 减少没必要的递归）
     *
     * @param addressMappingQueryDTO 地址匹配查询条件
     * @return 地址匹配树状结果集
     */
    @Override
    public List<RegionTreeChildVO> findAddressMappingGroupByCondition(final AddressMappingQueryDTO addressMappingQueryDTO, final String countryCode) {
        if (addressMappingQueryDTO.getPlatformCode() == null || "".equals(addressMappingQueryDTO.getPlatformCode())) {
            throw new CommonException(MetadataConstants.ErrorCode.BASIC_DATA_PLATFORM_CODE_IS_NULL);
        }
        if (countryCode == null || "".equals(countryCode)) {
            throw new CommonException("countryCode is null");
        }
        if (null == addressMappingQueryDTO.getTenantId()) {
            throw new CommonException(MetadataConstants.ErrorCode.BASIC_DATA_TENANT_ID_IS_NULL);
        }
        List<RegionTreeChild> regionTreeChildList = addressMappingMapper.findAddressMappingByCondition(addressMappingQueryDTO, countryCode);
        List<String> regionCodes = new ArrayList<>();
        for (RegionTreeChild regionTreeChild : regionTreeChildList) {
            if (StringUtils.isNotEmpty(regionTreeChild.getRegionCode())) {
                regionCodes.add(regionTreeChild.getRegionCode());
            }
        }
        //获取地区
        RegionQueryLovDTO dto = new RegionQueryLovDTO();
        dto.setTenantId(addressMappingQueryDTO.getTenantId());
        dto.setRegionCodes(regionCodes);
        dto.setCountryCode(countryCode);
        List<Region> regionList = regionRepository.listRegionLov(dto, addressMappingQueryDTO.getTenantId());
        if (regionList.isEmpty()) {
            return new ArrayList<>();
        }
        Map<String,Region> regionMap = regionList.stream().collect(Collectors.toMap(Region::getRegionCode, region -> region));
        for (RegionTreeChild regionTreeChild : regionTreeChildList) {
            Region region = regionMap.get(regionTreeChild.getRegionCode());
            if (null == region) {
                continue;
            }
            regionTreeChild.setRegionName(region.getRegionName());
            regionTreeChild.setLevelPath(region.getLevelPath());
            regionTreeChild.setParentRegionCode(region.getParentRegionCode());
            regionTreeChild.setRegionId(region.getRegionId());
        }
        //根据parent id 分组
        final Map<String, List<RegionTreeChild>> collect = new HashMap<>();
        for (RegionTreeChild node : regionTreeChildList) {
            // 将parent id 为null 的替换成-1
            if (node.getParentRegionCode() == null) {
                node.setParentRegionCode(String.valueOf(-1));
            }
            collect.computeIfAbsent(node.getParentRegionCode(), k -> new ArrayList<>()).add(node);
        }

        final List<RegionTreeChild> tree = new ArrayList<>();

        //递归获取树形结构数据
        getParent(collect, tree, addressMappingQueryDTO.getPlatformCode(), addressMappingQueryDTO.getTenantId());
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
        Platform platform = new Platform();
        platform.setTenantId(tenantId);
        platform.setPlatformCode(addressMapping.getPlatformCode());
        final Platform query = platformRepository.selectOne(platform);
        addressMapping.setPlatformCode(query.getPlatformName());
        if (!regionList.isEmpty()) {
            Region region = regionList.get(0);
            addressMapping.setRegionName(region.getRegionName());
            addressMapping.getRegionPathIds().add(region.getCountryId());
            addressMapping.getRegionPathCodes().add(region.getCountryCode());
            addressMapping.getRegionPathNames().add(region.getCountryName());
        }
        return AddressMappingConverter.poToVoObject(addressMapping);
    }

    @Override
    public List<AddressMappingVO> listAddressMappings(List<AddressMappingQueryInnerDTO> addressMappingQueryInts, Long tenantId) {
        List<String> externalCodes = new ArrayList<>();
        List<String> addressTypeCodes = new ArrayList<>();
        List<String> externalNames = new ArrayList<>();
        for (AddressMappingQueryInnerDTO addressMappingQueryInnerDTO : addressMappingQueryInts) {
            String externalCode = addressMappingQueryInnerDTO.getExternalCode();
            if(StringUtils.isNotEmpty(externalCode)){
                externalCodes.add(externalCode);
            }

            String  externalName = addressMappingQueryInnerDTO.getExternalName();
            if (StringUtils.isNotEmpty(externalName)) {
                externalNames.add(externalName);
            }

            String addressTypeCode = addressMappingQueryInnerDTO.getAddressTypeCode();
            if (StringUtils.isNotEmpty(addressTypeCode)) {
                addressTypeCodes.add(addressTypeCode);
            }

        }
        return AddressMappingConverter.poToVoListObjects(addressMappingRepository.listAddressMappings(externalCodes, addressTypeCodes, externalNames, tenantId));
    }

    @Override
    public void createAddressMapping(AddressMapping addressMapping) {
        if (addressMapping.exist(addressMappingRepository)) {
            throw new CommonException(BaseConstants.ErrorCode.DATA_EXISTS);
        }
        Preconditions.checkArgument(null != addressMapping.getPlatformCode(), MetadataConstants.ErrorCode.BASIC_DATA_PLATFORM_CODE_IS_NULL);
        addressMappingRepository.insertSelective(addressMapping);
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
    private Map<String,List<RegionTreeChild>> getParentAddressMapping(List<Region> parentRegions,
                                                             String type,
                                                             Long tenantId) {

        List<String> regionCodes = new ArrayList<>();
        List<RegionTreeChild> result = new ArrayList<>(16);
        for (Region parentRegion : parentRegions) {
            regionCodes.add(parentRegion.getRegionCode());

            RegionTreeChild regionTreeChild = new RegionTreeChild();
            regionTreeChild.setRegionCode(parentRegion.getRegionCode());
            regionTreeChild.setParentRegionCode(parentRegion.getParentRegionCode());
            regionTreeChild.setRegionName(parentRegion.getRegionName());
            regionTreeChild.setRegionId(parentRegion.getRegionId());
            regionTreeChild.setLevelPath(parentRegion.getLevelPath());
            regionTreeChild.setRegionId(parentRegion.getRegionId());
            result.add(regionTreeChild);
        }
        List<RegionTreeChild> queryList = addressMappingMapper.listAddressMapping(regionCodes,type,tenantId);
        if (queryList.isEmpty()) {
            return  result.stream().collect(Collectors.groupingBy(RegionTreeChild::getRegionCode));
        }

        Map<String,RegionTreeChild> queryMap = queryList.stream().collect(Collectors.toMap(RegionTreeChild::getRegionCode, regionTreeChild -> regionTreeChild));
        for (RegionTreeChild regionTreeChild : result) {
            String regionCode = regionTreeChild.getRegionCode();
            RegionTreeChild queryAddressMapping = queryMap.get(regionCode);
            if (null == queryAddressMapping) {
                continue;
            }
            regionTreeChild.setActiveFlag(queryAddressMapping.getActiveFlag());
            regionTreeChild.setExternalCode(queryAddressMapping.getExternalCode());
            regionTreeChild.setPlatformCode(queryAddressMapping.getPlatformCode());
            regionTreeChild.setExternalName(queryAddressMapping.getExternalName());
            regionTreeChild.setAddressMappingId(queryAddressMapping.getAddressMappingId());
            regionTreeChild.set_token(queryAddressMapping.get_token());
        }
        return result.stream().collect(Collectors.groupingBy(RegionTreeChild::getRegionCode));
    }
    /**
     * 获取父级地区
     * @param map 分组数据
     * @param tenantId 租户ID
     * @return list
     */
    private List<Region> parentRegion(Map<String, List<RegionTreeChild>> map,Long tenantId) {
        List<String> parentRegionCodes = new ArrayList<>();
        map.forEach((k,v)->{
            if (!RegionConstants.RegionLov.DEFAULT_CODE.getCode().equals(k)) {
                parentRegionCodes.add(k);
            }
        });
        RegionQueryLovDTO dto = new RegionQueryLovDTO();
        dto.setTenantId(tenantId);
        if (parentRegionCodes.isEmpty()) {
            return  new ArrayList<>();
        }
        dto.setParentRegionCodes(parentRegionCodes);
        return regionRepository.listRegionLov(dto,tenantId);
    }
    /**
     * 获取父节点
     *
     * @param collect 根据条件查询结果集
     * @param tree    树形返回数据
     * @param type    平台类型
     */
    private void getParent(final Map<String, List<RegionTreeChild>> collect,
                           final List<RegionTreeChild> tree,
                           final String type,
                           final Long tenantId) {

        List<Region> parentRegions = parentRegion(collect,tenantId);
        Map<String,Region> parentRegionMap = parentRegions.stream().collect(Collectors.toMap(Region::getRegionCode, region -> region));
        Map<String,List<RegionTreeChild>> map = getParentAddressMapping(parentRegions,type,tenantId);

        final List<RegionTreeChild> result = new ArrayList<>();
        for (Map.Entry<String,List<RegionTreeChild>> entry : collect.entrySet()) {
            String key = entry.getKey();
            //根节点
            if (RegionConstants.RegionLov.DEFAULT_CODE.getCode().equals(key)) {
                if (!tree.containsAll(collect.get(key))) {
                    tree.addAll(collect.get(key));
                }
                continue;
            }
            //查询父节点
            final List<RegionTreeChild> parents = map.get(key);
            //处理不同平台同一数据
            RegionTreeChild parent = handleParents(parents, parentRegionMap, key, type);
            //如果tree 集合中有了这个RegionTreeChild，直接赋值tree 集合中父节点的 children字段
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
        final Map<String, List<RegionTreeChild>> parentMap = result.stream().map(node -> {
            if (node.getParentRegionCode() == null) {
                node.setParentRegionCode(RegionConstants.RegionLov.DEFAULT_CODE.getCode());
            }
            return node;
        }).collect(Collectors.groupingBy(RegionTreeChild::getParentRegionCode));
        //递归调用
        getParent(parentMap, tree, type,tenantId);
    }
    /**
     *
     * @date 2021-08-09
     * @param  parents 父节点 地区匹配数据
     * @param  parentRegionMap 父节点 地区数据
     * @param  key  地区编码
     * @param  type  目录编码
     * @return  RegionTreeChild
     */
    private RegionTreeChild handleParents(List<RegionTreeChild> parents,Map<String,Region> parentRegionMap,String key,String type) {
        //处理不同平台同一数据
        RegionTreeChild parent = new RegionTreeChild();
        if (null == parents || parents.isEmpty()) {
            final Region region = parentRegionMap.get(key);
            parent.setParentRegionCode(region.getParentRegionCode());
            parent.setRegionCode(region.getRegionCode());
            parent.setRegionId(region.getRegionId());
            parent.setRegionName(region.getRegionName());
        }else if (parents.size() == 1) {
            parent = parents.get(0);
        } else {
            for (final RegionTreeChild treeChild : parents) {
                if (type.equals(treeChild.getPlatformCode())) {
                    parent = treeChild;
                }
            }
        }
        return parent;
    }
}
