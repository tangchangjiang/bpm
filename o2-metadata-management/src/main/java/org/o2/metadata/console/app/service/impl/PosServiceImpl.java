package org.o2.metadata.console.app.service.impl;

import org.apache.commons.collections.CollectionUtils;
import org.hzero.core.base.BaseConstants;
import org.o2.metadata.console.app.service.PosService;
import org.o2.metadata.console.infra.entity.*;
import org.o2.metadata.console.domain.repository.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.util.List;


/**
 * 服务点信息应用服务默认实现
 *
 * @author tingting.wang@hand-china.com 2019-3-25
 */
@Service
public class PosServiceImpl implements PosService {

    private static final Logger LOG = LoggerFactory.getLogger(PosServiceImpl.class);

    private final PosRepository posRepository;
    private final PostTimeRepository postTimeRepository;
    private final PosAddressRepository posAddressRepository;
    private final RegionRepository regionRepository;
    private final PosRelCarrierRepository posRelCarrierRepository;
    private CarrierRepository carrierRepository;


    public PosServiceImpl(PosRepository posRepository, PostTimeRepository postTimeRepository, PosAddressRepository posAddressRepository, RegionRepository regionRepository, PosRelCarrierRepository posRelCarrierRepository, CarrierRepository carrierRepository) {
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
        pos.baseValidate(posRepository);
        pos.validatePosCode(posRepository);

        Assert.isTrue(!pos.exist(posRepository), BaseConstants.ErrorCode.DATA_EXISTS);
        final PosAddress address = pos.getAddress();
        if (address != null && address.getRegionId() != null) {
            final Region region = regionRepository.selectByPrimaryKey(address.getRegionId());
            if (region != null) {
                address.setCountryId(region.getCountryId());
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
            final Long regionId = address.getRegionId();
            final Region region = regionRepository.selectByPrimaryKey(regionId);
            address.setCountryId(region.getCountryId());
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
