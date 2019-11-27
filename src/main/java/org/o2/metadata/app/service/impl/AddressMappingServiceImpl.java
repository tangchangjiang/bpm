package org.o2.metadata.app.service.impl;

import io.choerodon.core.exception.CommonException;
import org.o2.ext.metadata.app.service.AddressMappingService;
import org.o2.ext.metadata.domain.entity.AddressMapping;
import org.o2.ext.metadata.domain.entity.Region;
import org.o2.ext.metadata.domain.vo.RegionTreeChildVO;
import org.o2.ext.metadata.infra.constants.BasicDataConstants;
import org.o2.ext.metadata.infra.mapper.AddressMappingMapper;
import org.o2.ext.metadata.infra.mapper.RegionMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    private static final Logger LOG = LoggerFactory.getLogger(AddressMappingServiceImpl.class);
    private final AddressMappingMapper addressMappingMapper;
    private final RegionMapper regionMapper;

    public AddressMappingServiceImpl(final AddressMappingMapper addressMappingMapper,
                                     final RegionMapper regionMapper) {
        this.addressMappingMapper = addressMappingMapper;
        this.regionMapper = regionMapper;
    }

    /**
     * 地址匹配逆向递归 树状数据结构（根据parent id 分组 减少没必要的递归）
     *
     * @param condition 地址匹配查询条件
     * @return 地址匹配树状结果集
     */
    @Override
    public List<RegionTreeChildVO> findAddressMappingGroupByCondition(final AddressMapping condition, final String countryCode) {
        if (condition.getPlatformTypeCode() == null || "".equals(condition.getPlatformTypeCode())) {
            throw new CommonException("platformType is null");
        }
        if (countryCode == null || "".equals(countryCode)) {
            throw new CommonException("countryCode is null");
        }
        final List<RegionTreeChildVO> regionTreeChildVOList = addressMappingMapper.findAddressMappingByCondition(condition, countryCode);

        //根据parent id 分组
        final Map<Long, List<RegionTreeChildVO>> collect = regionTreeChildVOList.stream().peek(node -> {
            // 将parent id 为null 的替换成-1
            if (node.getParentRegionId() == null) {
                node.setParentRegionId(-1L);
            }
        }).collect(Collectors.groupingBy(RegionTreeChildVO::getParentRegionId));

        final List<RegionTreeChildVO> tree = new ArrayList<>();

        //递归获取树形结构数据
        getParent(collect, tree, condition.getPlatformTypeCode());
        sortList(tree);
        return tree;

        //return processRegionData(regionTreeChildVOList, condition.getPlatformTypeCode());
    }

    // 从获取的list，处理数据，形成树状结构
    private List<RegionTreeChildVO> processRegionData(final List<RegionTreeChildVO> regionTreeChildVOList, final String platformTypeCode) {
        // 省市区三级,用map存储，提高效率，key=regionCode，value=children
        final Map<String, List<RegionTreeChildVO>> map = new HashMap<>();
        final List<RegionTreeChildVO> tree = new ArrayList<>();
        LOG.info("regionTreeChildVOList.size:" + regionTreeChildVOList.size());
        //按照levelPath进行分组，获得省市区
        for (final RegionTreeChildVO regionTreeChildVO : regionTreeChildVOList) {
            final String[] regionPaths = regionTreeChildVO.getLevelPath().split(BasicDataConstants.Constants.ADDRESS_SPLIT_REGEX);
            LOG.info("regionPaths:" + regionPaths);
            LOG.info("regionPaths.length:" + regionPaths.length);
            for (int i = regionPaths.length - 1; i >= 0; i--) {
                LOG.info("i:" + i);
                LOG.info("regionPaths[i]:" + regionPaths[i]);
                // 判断map是否包含
                if (!map.containsKey(regionPaths[i])) {
                    final RegionTreeChildVO regionTree;
                    if (i == regionPaths.length - 1) {
                        regionTree = regionTreeChildVO;
                    } else {
                        regionTree = addressMappingMapper.findAddressMappingByCode(regionPaths[i], platformTypeCode);
                    }
                    LOG.info("regionTree:" + regionTree);
                    // 父类是否包含
                    if (i != 0) {
                        if (map.containsKey(regionPaths[i - 1])) {
                            map.get(regionPaths[i - 1]).add(regionTree);
                        } else {
                            final List<RegionTreeChildVO> temp = new ArrayList<>();
                            temp.add(regionTree);
                            map.put(regionPaths[i - 1], temp);
                        }
                    } else {
                        // 如果是根节点，加入tree
                        tree.add(regionTree);
                    }
                } else {
                    // map.get(regionPaths[i]).add()
                }
            }
        }
        return tree;
    }


    /**
     * 排序返回结果集
     *
     * @param tree 树形返回结果集
     */
    private void sortList(final List<RegionTreeChildVO> tree) {
        //省排序
        Collections.sort(tree);
        //市排序
        for (final RegionTreeChildVO regionTreeChild : tree) {
            if (regionTreeChild.getChildren() != null && regionTreeChild.getChildren().size() > 1) {
                Collections.sort(regionTreeChild.getChildren());
            }
            //区排序
            if (regionTreeChild.getChildren() != null) {
                for (final RegionTreeChildVO cityTreeChild : regionTreeChild.getChildren()) {
                    if (cityTreeChild.getChildren() != null && cityTreeChild.getChildren().size() > 1) {
                        Collections.sort(cityTreeChild.getChildren());
                    }
                }
            }
        }
    }

    /**
     * 获取父节点
     *
     * @param collect 根据条件查询结果集
     * @param tree    树形返回数据
     * @param type    平台类型
     */
    private void getParent(final Map<Long, List<RegionTreeChildVO>> collect, final List<RegionTreeChildVO> tree, final String type) {
        final List<RegionTreeChildVO> result = new ArrayList<>();

        collect.keySet().forEach(key -> {
            //根节点
            if (key == -1L) {
                tree.addAll(collect.get(key));
                return;
            }
            //查询父节点
            final List<RegionTreeChildVO> parents = addressMappingMapper.findAddressMappingById(key, type);
            //处理不同平台同一数据
            RegionTreeChildVO parent = new RegionTreeChildVO();
            if (parents.size() == 1) {
                parent = parents.get(0);
            } else if (parents.size() == 0) {
                final Region region = regionMapper.findRegionByPrimaryKey(key);
                parent.setParentRegionId(region.getParentRegionId());
                parent.setRegionCode(region.getRegionCode());
                parent.setRegionId(region.getRegionId());
                parent.setRegionName(region.getRegionName());
            } else {
                for (final RegionTreeChildVO treeChild : parents) {
                    if (type.equals(treeChild.getPlatformTypeCode())) {
                        parent = treeChild;
                    }
                }
            }
            //如果tree 集合中有了这个父节点，直接赋值tree 集合中父节点的 children字段
            if (tree.contains(parent)) {
                tree.get(tree.indexOf(parent)).setChildren(collect.get(key));
                return;
            }
            parent.setChildren(collect.get(key));
            result.add(parent);
            //return;
        });
        //如果没有父节点 退出递归
        if (result.size() == 0) {
            return;
        }
        //分组下一次数据
        final Map<Long, List<RegionTreeChildVO>> parentMap = result.stream().map(node -> {
            if (node.getParentRegionId() == null) {
                node.setParentRegionId(-1L);
            }
            return node;
        }).collect(Collectors.groupingBy(RegionTreeChildVO::getParentRegionId));
        //递归调用
        getParent(parentMap, tree, type);
    }


}
