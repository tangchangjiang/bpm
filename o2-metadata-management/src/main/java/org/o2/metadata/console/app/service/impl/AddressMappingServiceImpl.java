package org.o2.metadata.console.app.service.impl;

import com.google.common.base.Preconditions;
import io.choerodon.core.exception.CommonException;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.hzero.core.base.BaseConstants;
import org.o2.metadata.console.api.co.AddressMappingCO;
import org.o2.metadata.console.api.dto.AddressMappingInnerDTO;
import org.o2.metadata.console.api.dto.AddressMappingQueryDTO;
import org.o2.metadata.console.api.dto.AddressMappingQueryInnerDTO;
import org.o2.metadata.console.api.dto.RegionQueryLovInnerDTO;
import org.o2.metadata.console.api.vo.AddressMappingVO;
import org.o2.metadata.console.api.vo.RegionTreeChildVO;
import org.o2.metadata.console.app.bo.RegionNameMatchBO;
import org.o2.metadata.console.app.service.AddressMappingService;
import org.o2.metadata.console.infra.constant.AddressConstants;
import org.o2.metadata.console.infra.constant.MetadataConstants;
import org.o2.metadata.console.infra.constant.O2LovConstants;
import org.o2.metadata.console.infra.convertor.AddressMappingConverter;
import org.o2.metadata.console.infra.convertor.RegionConverter;
import org.o2.metadata.console.infra.entity.AddressMapping;
import org.o2.metadata.console.infra.entity.Platform;
import org.o2.metadata.console.infra.entity.Region;
import org.o2.metadata.console.infra.entity.RegionTreeChild;
import org.o2.metadata.console.infra.lovadapter.repository.RegionLovQueryRepository;
import org.o2.metadata.console.infra.mapper.AddressMappingMapper;
import org.o2.metadata.console.infra.repository.AddressMappingRepository;
import org.o2.metadata.console.infra.repository.PlatformRepository;
import org.o2.metadata.console.infra.repository.RegionRepository;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.collectingAndThen;
import static java.util.stream.Collectors.toCollection;

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
    private final PlatformRepository platformRepository;
    private final RegionLovQueryRepository regionLovQueryRepository;

    public AddressMappingServiceImpl(final AddressMappingMapper addressMappingMapper,
                                     RegionRepository regionRepository,
                                     AddressMappingRepository addressMappingRepository,
                                     PlatformRepository platformRepository, RegionLovQueryRepository regionLovQueryRepository) {
        this.addressMappingMapper = addressMappingMapper;
        this.regionRepository = regionRepository;
        this.addressMappingRepository = addressMappingRepository;
        this.platformRepository = platformRepository;
        this.regionLovQueryRepository = regionLovQueryRepository;
    }

    /**
     * 地址匹配逆向递归 树状数据结构（根据parent id 分组 减少没必要的递归）
     *
     * @param addressMappingQueryDTO 地址匹配查询条件
     * @return 地址匹配树状结果集
     */
    @Override
    public List<RegionTreeChildVO> findAddressMappingGroupByCondition(final AddressMappingQueryDTO addressMappingQueryDTO, final String countryCode) {
        checkData(addressMappingQueryDTO,countryCode);
        // 更据名称查询地区
        if (StringUtils.isNotEmpty(addressMappingQueryDTO.getRegionName())) {
            RegionQueryLovInnerDTO name = new RegionQueryLovInnerDTO();
            name.setRegionName(addressMappingQueryDTO.getRegionName());
            name.setTenantId(addressMappingQueryDTO.getTenantId());
            List<Region> nameList = regionRepository.listRegionLov(name, addressMappingQueryDTO.getTenantId());
            List<String> nameCodes = new ArrayList<>();
            for (Region region : nameList) {
                nameCodes.add(region.getRegionCode());
            }
            addressMappingQueryDTO.setRegionCodes(nameCodes);
        }

        List<RegionTreeChild> regionTreeChildList = addressMappingMapper.findAddressMappingByCondition(addressMappingQueryDTO, countryCode);
        if (null == regionTreeChildList || regionTreeChildList.isEmpty()) {
            return new ArrayList<>();
        }
        List<String> regionCodes = new ArrayList<>();
        for (RegionTreeChild regionTreeChild : regionTreeChildList) {
            if (StringUtils.isNotEmpty(regionTreeChild.getRegionCode())) {
                regionCodes.add(regionTreeChild.getRegionCode());
            }
        }
        //获取地区
        RegionQueryLovInnerDTO dto = new RegionQueryLovInnerDTO();
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
        return RegionConverter.poToVoChildObjects(tree);
    }
    /**
     * 校验数据
     * @param countryCode 国际编码
     * @param addressMappingQueryDTO 入参
     */
    private void checkData(AddressMappingQueryDTO addressMappingQueryDTO,String countryCode ) {
        if (addressMappingQueryDTO.getPlatformCode() == null || "".equals(addressMappingQueryDTO.getPlatformCode())) {
            throw new CommonException(MetadataConstants.ErrorCode.BASIC_DATA_PLATFORM_CODE_IS_NULL);
        }
        if (countryCode == null || "".equals(countryCode)) {
            throw new CommonException("countryCode is null");
        }
        if (null == addressMappingQueryDTO.getTenantId()) {
            throw new CommonException(MetadataConstants.ErrorCode.BASIC_DATA_TENANT_ID_IS_NULL);
        }
    }

    @Override
    public AddressMappingVO addressMappingDetail(Long addressMappingId, String countryCode, Long tenantId) {
        final AddressMapping addressMapping = addressMappingRepository.selectByPrimaryKey(addressMappingId);
        RegionQueryLovInnerDTO queryLovDTO = new RegionQueryLovInnerDTO();
        queryLovDTO.setCountryCode(countryCode);
        queryLovDTO.setRegionCode(addressMapping.getRegionCode());
        queryLovDTO.setTenantId(tenantId);
        List<Region> regionList = regionRepository.listRegionLov(queryLovDTO,tenantId);
        Platform platform = new Platform();
        platform.setTenantId(tenantId);
        platform.setPlatformCode(addressMapping.getPlatformCode());
        final Platform query = platformRepository.selectOne(platform);
        addressMapping.setPlatformName(query.getPlatformName());
        if (!regionList.isEmpty()) {
            //查询地区上级
            Region bean = regionList.get(0);
            String levelPath = bean.getLevelPath();
            String[] paths = levelPath.split(MetadataConstants.Constants.ADDRESS_SPLIT_REGEX);
            List<String> regionCodes = new ArrayList<>();
            Collections.addAll(regionCodes, paths);
            RegionQueryLovInnerDTO queryLov = new RegionQueryLovInnerDTO();
            queryLov.setCountryCode(countryCode);
            queryLov.setRegionCodes(regionCodes);
            queryLov.setTenantId(tenantId);
            List<Region> parent = regionRepository.listRegionLov(queryLov,tenantId);
            if (!parent.isEmpty()) {
                for (Region region : parent){
                    addressMapping.setRegionName(region.getRegionName());
                    addressMapping.getRegionPathNames().add(region.getRegionName());
                }
            }
        }
        return AddressMappingConverter.poToVoObject(addressMapping);
    }

    @Override
    public Map<String, AddressMappingCO> listAddressMappings(AddressMappingQueryInnerDTO addressMappingQueryInts, Long tenantId) {
        List<AddressMappingCO> list = new ArrayList<>(16);
        if (CollectionUtils.isNotEmpty(addressMappingQueryInts.getAddressMappingInnerList())) {
            list = regionBaseMapping(tenantId,addressMappingQueryInts);
            List<AddressMappingInnerDTO> addressMappingInnerList = addressMappingQueryInts.getAddressMappingInnerList();
            if (addressMappingInnerList.size() == list.size()) {
                return buildAddressMappingTree(tenantId, list);
            }
        }
        // 查询地址匹配的数据
        List<AddressMappingCO> listMapping = AddressMappingConverter.poToCoListObjects(addressMappingRepository.listAddressMappings(addressMappingQueryInts, tenantId));
        list.addAll(listMapping);
        ArrayList<AddressMappingCO> collect = list.stream().collect(
                collectingAndThen(
                        toCollection(() -> new TreeSet<>(Comparator.comparing(AddressMappingCO::getRegionCode))), ArrayList::new)
        );
        return buildAddressMappingTree(tenantId, collect);
    }
     /**
      * 批量hzero基础数据
      * @param tenantId 租户ID
      * @param addressMappingQueryInts 查询条件
      * @return  list
      */
    private List<AddressMappingCO> regionBaseMapping(Long tenantId, AddressMappingQueryInnerDTO addressMappingQueryInts) {
        List<AddressMappingCO> list = new ArrayList<>(16);
        // 匹配hzero地区数据
        List<Region> regionList = regionLovQueryRepository.fuzzyMatching(tenantId,null,null,getQuery(addressMappingQueryInts));
        if (CollectionUtils.isNotEmpty(regionList)) {
            for (Region region : regionList) {
                AddressMappingCO  co = new AddressMappingCO();
                co.setExternalName(region.getRegionName());
                co.setRegionCode(region.getRegionCode());
                co.setExternalName(region.getRegionName());
                co.setExternalCode(region.getExternalCode());
                if (AddressConstants.AddressMapping.LEVEL_NUMBER_1.equals(region.getLevelNumber())) {
                    co.setAddressTypeCode(AddressConstants.AddressMapping.REGION);
                }
                if (AddressConstants.AddressMapping.LEVEL_NUMBER_2.equals(region.getLevelNumber())) {
                    co.setAddressTypeCode(AddressConstants.AddressMapping.CITY);
                }
                if (AddressConstants.AddressMapping.LEVEL_NUMBER_3.equals(region.getLevelNumber())) {
                    co.setAddressTypeCode(AddressConstants.AddressMapping.DISTRICT);
                }
                co.setParentRegionCode(region.getParentRegionCode());
                list.add(co);
            }
        }
        return list;
    }

   /**
    * 构造查询条件
    * @param addressMappingQueryInts 查询条件
    * @return  list
    */
    private  List<RegionNameMatchBO> getQuery(AddressMappingQueryInnerDTO addressMappingQueryInts) {
        List<RegionNameMatchBO> query = new ArrayList<>();
        List<AddressMappingInnerDTO> addressMappingInnerList = addressMappingQueryInts.getAddressMappingInnerList();
        for (AddressMappingInnerDTO dto : addressMappingInnerList) {
            RegionNameMatchBO bo = new RegionNameMatchBO();
            bo.setRegionName(dto.getExternalName());
            bo.setExternalCode(dto.getExternalCode());
            bo.setExternalName(dto.getExternalName());
            if (AddressConstants.AddressMapping.REGION.equals(dto.getAddressTypeCode())) {
                bo.setLevelNumber(AddressConstants.AddressMapping.LEVEL_NUMBER_1);
            }
            if (AddressConstants.AddressMapping.CITY.equals(dto.getAddressTypeCode())) {
                bo.setLevelNumber(AddressConstants.AddressMapping.LEVEL_NUMBER_2);
            }
            if (AddressConstants.AddressMapping.DISTRICT.equals(dto.getAddressTypeCode())) {
                bo.setLevelNumber(AddressConstants.AddressMapping.LEVEL_NUMBER_3);
            }
            query.add(bo);
        }
        return query;
    }

    /**
     * 返回地址对象集合
     * @param tenantId 租户ID
     * @param list 地区匹配
     * @return map
     */
    private Map<String, AddressMappingCO> buildAddressMappingTree(Long tenantId, List<AddressMappingCO> list) {
        List<String> codes = new ArrayList<>(list.size());
        for (AddressMappingCO co : list) {
            codes.add(co.getRegionCode());
        }
        RegionQueryLovInnerDTO queryLovDTO = new RegionQueryLovInnerDTO();
        queryLovDTO.setTenantId(tenantId);
        queryLovDTO.setRegionCodes(codes);
        List<Region> regionList = regionRepository.listRegionLov(queryLovDTO, tenantId);
        Map<String, Region> regionMap = regionList.stream().collect(Collectors.toMap(Region::getRegionCode, v -> v));
        for (AddressMappingCO co : list) {
            Region region = regionMap.get(co.getRegionCode());
            if (region != null) {
                co.setParentRegionCode(region.getParentRegionCode());
                co.setRegionName(region.getRegionName());
            }
        }
        // 通过组装树形结构数据 过滤通过名称查出的脏数据
        List<AddressMappingCO> addressMappingTree = new ArrayList<>();
        for (AddressMappingCO co : list) {
            if (StringUtils.isEmpty(co.getParentRegionCode())) {
                co.setChildren(getChildren(co, list));
                addressMappingTree.add(co);
            }
        }
        Map<String, AddressMappingCO> result = new HashMap<>(16);
        for (AddressMappingCO co : addressMappingTree) {
            result.put(co.getExternalName(), co);
        }
        return result;
    }
    /**
     * 递归查找指定分类的所有子分类
     * @param co 一级分类
     * @param entities 地区匹配数据
     * @return list
     */
    private List<AddressMappingCO> getChildren(AddressMappingCO co, List<AddressMappingCO> entities) {
        //找到子分类
        List<AddressMappingCO> children = new ArrayList<>();
        for (AddressMappingCO entry : entities) {
            if (co.getRegionCode().equals(entry.getParentRegionCode())) {
                entry.setChildren(getChildren(entry, entities));
                children.add(entry);
            }
        }
        return children;
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
            if (!O2LovConstants.RegionLov.DEFAULT_CODE.equals(k)) {
                parentRegionCodes.add(k);
            }
        });
        RegionQueryLovInnerDTO dto = new RegionQueryLovInnerDTO();
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
            if (O2LovConstants.RegionLov.DEFAULT_CODE.equals(key)) {
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
        final Map<String, List<RegionTreeChild>> parentMap = new HashMap<>();
        for (RegionTreeChild node : result) {
            if (node.getParentRegionCode() == null) {
                node.setParentRegionCode(O2LovConstants.RegionLov.DEFAULT_CODE);
            }
            parentMap.computeIfAbsent(node.getParentRegionCode(), k -> new ArrayList<>()).add(node);
        }
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
