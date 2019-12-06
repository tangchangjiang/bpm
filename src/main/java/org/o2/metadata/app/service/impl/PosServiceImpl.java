package org.o2.metadata.app.service.impl;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.hzero.core.base.BaseConstants;
import org.hzero.core.base.BaseConstants.Flag;
import org.o2.context.metadata.api.IPosContext;
import org.o2.metadata.app.service.PosService;
import org.o2.metadata.domain.entity.Pos;
import org.o2.metadata.domain.entity.PosAddress;
import org.o2.metadata.domain.entity.Region;
import org.o2.metadata.domain.repository.PosAddressRepository;
import org.o2.metadata.domain.repository.PosRepository;
import org.o2.metadata.domain.repository.PostTimeRepository;
import org.o2.metadata.domain.repository.RegionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;


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
    private final IPosContext posContext;

    public PosServiceImpl(final PosRepository posRepository,
                          final PostTimeRepository postTimeRepository,
                          final PosAddressRepository posAddressRepository,
                          final RegionRepository regionRepository,
                          final IPosContext posContext) {
        this.posRepository = posRepository;
        this.postTimeRepository = postTimeRepository;
        this.posAddressRepository = posAddressRepository;
        this.regionRepository = regionRepository;
        this.posContext = posContext;
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
                postTimeRepository.insert(postTime);
            });
        }

        // 更新 redis
        syncLimitToRedis(pos);
        return pos;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Pos update(final Pos pos) {
        pos.baseValidate(posRepository);

        Assert.isTrue(pos.exist(posRepository), BaseConstants.ErrorCode.DATA_NOT_EXISTS);

        final PosAddress address;
        if ((address = pos.getAddress()) != null) {
            final Long regionId = address.getRegionId();
            final Region region = regionRepository.selectByPrimaryKey(regionId);
            address.setCountryId(region.getCountryId());
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
                if (postTime.getPostTimeId() != null) {
                    postTimeRepository.updateByPrimaryKey(postTime);
                } else {
                    postTimeRepository.insert(postTime);
                }
            });
        }
        // 更新 redis
        syncLimitToRedis(pos);

        return pos;
    }

    @Override
    public Pos getPosWithPropertiesInRedisByPosId(final Long posId) {
        final Pos pos = posRepository.getPosWithAddressAndPostTimeByPosId(posId);
        if (pos.getExpressedFlag().equals(Flag.YES)) {
            final String expressValue = this.posContext.getExpressLimit(pos.getPosCode(),pos.getTenantId());
            if (StringUtils.isNotBlank(expressValue)) {
                pos.setExpressLimitQuantity(Long.parseLong(expressValue));
            }
        }
        if (pos.getPickedUpFlag().equals(Flag.YES)) {
            final String pickUpValue = this.posContext.getPickUpLimit(pos.getPosCode(),pos.getTenantId());
            if (StringUtils.isNotBlank(pickUpValue)) {
                pos.setPickUpLimitQuantity(Long.parseLong(pickUpValue));
            }
        }
        return pos;
    }

    /**
     * 将最大接单量更新到 redis
     *
     * @param pos Pos
     */
    private void syncLimitToRedis(final Pos pos) {
        final String expressValue = this.posContext.getExpressLimit(pos.getPosCode(),pos.getTenantId());
        final String pickUpValue = this.posContext.getPickUpLimit(pos.getPosCode(),pos.getTenantId());

        final String newExpress = String.valueOf(pos.getExpressLimitQuantity());
        if (pos.getExpressedFlag().equals(Flag.YES) && !newExpress.equals(expressValue)) {
            this.posContext.saveExpressQuantity(pos.getPosCode(), newExpress,pos.getTenantId());
        }
        final String newPickUp = String.valueOf(pos.getPickUpLimitQuantity());
        if (pos.getPickedUpFlag().equals(Flag.YES) && !newPickUp.equals(pickUpValue)) {
            this.posContext.savePickUpQuantity(pos.getPosCode(), newPickUp,pos.getTenantId());
        }
    }
}
