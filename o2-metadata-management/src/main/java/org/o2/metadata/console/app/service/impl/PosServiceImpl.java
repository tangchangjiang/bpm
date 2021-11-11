package org.o2.metadata.console.app.service.impl;

import com.google.common.collect.Maps;
import org.apache.commons.collections.CollectionUtils;
import org.o2.core.exception.O2CommonException;
import org.o2.metadata.console.api.co.PosAddressCO;
import org.o2.metadata.console.api.dto.PosAddressQueryInnerDTO;
import org.o2.metadata.console.api.dto.RegionQueryLovInnerDTO;
import org.o2.metadata.console.api.vo.PosVO;
import org.o2.metadata.console.app.service.PosService;
import org.o2.metadata.console.infra.constant.PosConstants;
import org.o2.metadata.console.infra.convertor.PosAddressConverter;
import org.o2.metadata.console.infra.convertor.PosConverter;
import org.o2.metadata.console.infra.entity.*;
import org.o2.metadata.console.infra.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


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
    private final RegionRepository regionRepository;
    private final PosRelCarrierRepository posRelCarrierRepository;
    private CarrierRepository carrierRepository;


    public PosServiceImpl(PosRepository posRepository,
                          PostTimeRepository postTimeRepository,
                          PosAddressRepository posAddressRepository,
                          RegionRepository regionRepository,
                          PosRelCarrierRepository posRelCarrierRepository,
                          CarrierRepository carrierRepository) {
        this.posRepository = posRepository;
        this.postTimeRepository = postTimeRepository;
        this.posAddressRepository = posAddressRepository;
        this.regionRepository = regionRepository;
        this.posRelCarrierRepository = posRelCarrierRepository;
        this.carrierRepository = carrierRepository;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Pos create(final Pos pos) {
        // 名称校验
        validPosNameUnique(pos);
        validatePosCode(pos);
        PosAddress address = pos.getAddress();
        if (address != null && address.getRegionCode() != null) {
            updatePosAddress(address,pos.getTenantId());
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
        validPosNameUnique(pos);
        validatePosCode(pos);
        final PosAddress address;
        if ((address = pos.getAddress()) != null) {
            updatePosAddress(address,pos.getTenantId());
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

    /**
     * 服务点是否存在
     * @param pos 服务点调用
     */
    private void validatePosCode(Pos pos) {
        if (null != pos.getPosId()) {
            Pos original = posRepository.selectByPrimaryKey(pos);
            if (!original.getPosCode().equals(pos.getPosCode())) {
                throw new O2CommonException(null,PosConstants.ErrorCode.ERROR_POS_CODE_NOT_UPDATE, PosConstants.ErrorCode.ERROR_POS_CODE_NOT_UPDATE);
            }
            return;
        }
        Pos query = new Pos();
        query.setTenantId(pos.getTenantId());
        query.setPosCode(pos.getPosCode());
        final List<Pos> mayEmpty = posRepository.select(query);
        if (CollectionUtils.isNotEmpty(mayEmpty)) {
            throw new O2CommonException(null,PosConstants.ErrorCode.ERROR_POS_CODE_DUPLICATE, PosConstants.ErrorCode.ERROR_POS_CODE_DUPLICATE);
        }
    }

    /**
     * 名称唯一性校验
     * @param pos 服务点
     */
    private void validPosNameUnique(Pos pos) {
        if (null != pos.getPosId()) {
            Pos original = posRepository.selectByPrimaryKey(pos);
            if (original.getPosName().equals(pos.getPosName())) {
                return;
            }
        }
        Pos query = new Pos();
        query.setPosName(pos.getPosName());
        query.setTenantId(pos.getTenantId());
       List<Pos> list = posRepository.select(query);
       if (!list.isEmpty()) {
           throw new O2CommonException(null, PosConstants.ErrorCode.ERROR_POS_NAME_DUPLICATE,PosConstants.ErrorCode.ERROR_POS_NAME_DUPLICATE);
       }
    }
    /**
     * 更新服务点地址
     * @param address 服务点地址
     * @param tenantId 租户ID
     */
    private void updatePosAddress(PosAddress address,Long tenantId) {
        List<String> regionCodes = new ArrayList<>();
        regionCodes.add(address.getRegionCode());
        RegionQueryLovInnerDTO dto = new RegionQueryLovInnerDTO();
        dto.setRegionCodes(regionCodes);
        List<Region> regionList = regionRepository.listRegionLov(dto,tenantId);
        if (!regionList.isEmpty()) {
            address.setCountryCode(regionList.get(0).getCountryCode());
        }
    }

    @Override
    public PosVO getPosWithPropertiesInRedisByPosId(final Long organizationId, final Long posId) {
        final Pos pos = posRepository.getPosWithAddressAndPostTimeByPosId(organizationId,posId);
        List<PosRelCarrier> posRelCarriers = pos.posRelCarrier(posRelCarrierRepository,1);
        if (CollectionUtils.isNotEmpty(posRelCarriers)) {
            final Carrier carrier = carrierRepository.selectByPrimaryKey(posRelCarriers.get(0).getCarrierId());
            pos.setCarrierId(carrier.getCarrierId());
            pos.setCarrierName(carrier.getCarrierName());
        }
        return PosConverter.poToVoObject(pos);
    }

    @Override
    public List<PosAddressCO> listPosAddress(PosAddressQueryInnerDTO posAddressQueryInnerDTO, Long tenantId) {
        List<PosAddress> addresses = posAddressRepository.listPosAddress(posAddressQueryInnerDTO,tenantId);
        List<String> regionCodes = new ArrayList<>();
        for (PosAddress address : addresses) {
            regionCodes.add(address.getCityCode());
            regionCodes.add(address.getDistrictCode());
            regionCodes.add(address.getRegionCode());
        }
        RegionQueryLovInnerDTO dto = new RegionQueryLovInnerDTO();
        dto.setRegionCodes(regionCodes);
        List<Region> regionList =  regionRepository.listRegionLov(dto,tenantId);
        Map<String,Region> regionMap =  Maps.newHashMapWithExpectedSize(regionList.size());
        for (Region region : regionList) {
            regionMap.put(region.getRegionCode(),region);
        }
        for (PosAddress address : addresses) {
            //市
            String cityCode = address.getCityCode();
            Region city = regionMap.get(cityCode);
            if (null != city) {
                address.setCityName(city.getRegionName());
                address.setCountryName(city.getCountryName());
                address.setCountryCode(city.getCountryCode());
            }
            //区
            String districtCode = address.getDistrictCode();
            Region district = regionMap.get(districtCode);
            if (null != district) {
                address.setDistrictName(district.getRegionName());
            }
            //省
            String regionCode = address.getRegionCode();
            Region region = regionMap.get(regionCode);
            if (null != region) {
                address.setRegionName(region.getRegionName());
            }
        }
        return PosAddressConverter.poToCoListObjects(addresses);
    }


    /**
     * 更新默认承运商
     * @param pos pos
     */
    private void updateCarryIsDefault (Pos pos) {
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
