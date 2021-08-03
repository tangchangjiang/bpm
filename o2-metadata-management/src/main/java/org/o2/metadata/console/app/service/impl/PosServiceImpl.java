package org.o2.metadata.console.app.service.impl;

import com.google.common.collect.Maps;
import org.apache.commons.collections.CollectionUtils;
import org.hzero.boot.platform.lov.adapter.LovAdapter;
import org.hzero.core.base.BaseConstants;
import org.o2.metadata.console.api.dto.PosAddressDTO;
import org.o2.metadata.console.api.vo.PosAddressVO;
import org.o2.metadata.console.app.service.PosService;
import org.o2.metadata.console.app.service.RegionService;
import org.o2.metadata.console.infra.convertor.PosAddressConvertor;
import org.o2.metadata.console.infra.entity.*;
import org.o2.metadata.console.infra.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


/**
 * 服务点信息应用服务默认实现
 *
 * @author tingting.wang@hand-china.com 2019-3-25
 */
@Service
public class PosServiceImpl implements PosService {
    private final PosRepository posRepository;
    private final PostTimeRepository postTimeRepository;
    private final PosAddressRepository posAddressRepository;
    private final RegionService regionService;
    private final PosRelCarrierRepository posRelCarrierRepository;
    private CarrierRepository carrierRepository;
    private final LovAdapter lovAdapter;


    public PosServiceImpl(PosRepository posRepository,
                          PostTimeRepository postTimeRepository,
                          PosAddressRepository posAddressRepository,
                          RegionService regionService,
                          PosRelCarrierRepository posRelCarrierRepository,
                          CarrierRepository carrierRepository,
                          LovAdapter lovAdapter) {
        this.posRepository = posRepository;
        this.postTimeRepository = postTimeRepository;
        this.posAddressRepository = posAddressRepository;
        this.regionService = regionService;
        this.posRelCarrierRepository = posRelCarrierRepository;
        this.carrierRepository = carrierRepository;
        this.lovAdapter = lovAdapter;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Pos create(final Pos pos) {
        pos.baseValidate(posRepository);
        pos.validatePosCode(posRepository);

        Assert.isTrue(!pos.exist(posRepository), BaseConstants.ErrorCode.DATA_EXISTS);
        final PosAddress address = pos.getAddress();
        if (address != null && address.getRegionCode() != null) {
            List<String> regionCodes = new ArrayList<>();
            regionCodes.add(address.getRegionCode());
            List<Region> regionList = regionService.listRegionLov(regionCodes,pos.getTenantId());
            if (!regionList.isEmpty()) {
                address.setCountryCode(regionList.get(0).getCountryCode());
            }
            address.setTenantId(pos.getTenantId());
            posAddressRepository.insertSelective(address);
            pos.setAddressId(address.getPosAddressId());
        }

        posRepository.insertSelective(pos);

        if (CollectionUtils.isNotEmpty(pos.getPostTimes())) {
            pos.getPostTimes().forEach(postTime -> {
                // 过滤无意义数据
                if (!postTime.initTimeRange()) {
                    return;
                }
                postTime.setPosId(pos.getPosId());
                postTime.setTenantId(pos.getTenantId());
                postTimeRepository.insert(postTime);
            });
        }
        updateCarryIsDefault(pos);
        return pos;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Pos update(final Pos pos) {
        pos.baseValidate(posRepository);

        final PosAddress address;
        if ((address = pos.getAddress()) != null) {
            List<String> regionCodes = new ArrayList<>();
            regionCodes.add(address.getRegionCode());
            List<Region> regionList = regionService.listRegionLov(regionCodes,pos.getTenantId());
            if (!regionList.isEmpty()) {
                address.setCountryCode(regionList.get(0).getCountryCode());
            }
            address.setTenantId(pos.getTenantId());
            posAddressRepository.updateByPrimaryKey(address);
        }

        posRepository.updateByPrimaryKey(pos);

        if (CollectionUtils.isNotEmpty(pos.getPostTimes())) {
            pos.getPostTimes().forEach(postTime -> {
                // 过滤无意义数据
                if (!postTime.initTimeRange()) {
                    return;
                }
                postTime.setPosId(pos.getPosId());
                postTime.setTenantId(pos.getTenantId());
                if (postTime.getPostTimeId() != null) {
                    postTimeRepository.updateByPrimaryKey(postTime);
                } else {
                    postTimeRepository.insert(postTime);
                }
            });
        }
        updateCarryIsDefault(pos);
        return pos;
    }

    @Override
    public Pos getPosWithPropertiesInRedisByPosId(final Long organizationId,final Long posId) {
        final Pos pos = posRepository.getPosWithAddressAndPostTimeByPosId(organizationId,posId);
        List<PosRelCarrier> posRelCarriers = pos.posRelCarrier(posRelCarrierRepository,1);
        if (CollectionUtils.isNotEmpty(posRelCarriers)) {
            final Carrier carrier = carrierRepository.selectByPrimaryKey(posRelCarriers.get(0).getCarrierId());
            pos.setCarrierId(carrier.getCarrierId());
            pos.setCarrierName(carrier.getCarrierName());
        }
        return pos;
    }

    @Override
    public List<PosAddressVO> listPosAddress(PosAddressDTO posAddressDTO, Long tenantId) {
        List<PosAddress> addresses = posAddressRepository.listPosAddress(posAddressDTO,tenantId);
        List<String> regionCodes = new ArrayList<>();
        for (PosAddress address : addresses) {
            regionCodes.add(address.getCityCode());
            regionCodes.add(address.getDistrictCode());
            regionCodes.add(address.getRegionCode());
        }
        List<Region> regionList =  regionService.listRegionLov(regionCodes,tenantId);
        Map<String,Region> regionMap =  Maps.newHashMapWithExpectedSize(regionList.size());
        for (Region region : regionList) {
            regionMap.put(region.getRegionCode(),region);
        }
        for (PosAddress address : addresses) {
            //市
            String cityCode = address.getCityCode();
            Region city = regionMap.get(cityCode);
            if (null != city) {
                address.setCity(city.getRegionName());
                address.setCountry(city.getCountryName());
                address.setCountryCode(city.getCountryCode());
            }
            //区
            String districtCode = address.getDistrictCode();
            Region district = regionMap.get(districtCode);
            if (null != district) {
                address.setDistrict(district.getRegionName());
            }
            //省
            String regionCode = address.getRegionCode();
            Region region = regionMap.get(regionCode);
            if (null != region) {
                address.setRegion(region.getRegionName());
            }
        }
        return PosAddressConvertor.poToVoListObjects(addresses);
    }


    /**
     * 更新默认承运商
     * @param pos pos
     */
    public void updateCarryIsDefault (Pos pos) {
        List<PosRelCarrier> posRelCarriers = pos.posRelCarrier(posRelCarrierRepository,null);
        posRelCarriers.stream()
                .filter( c -> c.getDefaultFlag() == 1)
                .forEach(c -> {
                    c.setDefaultFlag(0);
                    posRelCarrierRepository.updateByPrimaryKeySelective(c);
                } );

        if (null != pos.getCarrierId()) {
            posRelCarriers.stream()
                    .filter( c -> c.getCarrierId().equals(pos.getCarrierId()))
                    .forEach(c -> posRelCarrierRepository.updateIsDefault(c.getPosRelCarrierId(), c.getPosId(),1));
        }
    }


}
