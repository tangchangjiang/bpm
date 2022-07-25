package org.o2.metadata.console.app.service.impl;

import com.google.common.collect.Maps;
import org.apache.commons.collections.CollectionUtils;
import org.o2.core.exception.O2CommonException;
import org.o2.core.helper.JsonHelper;
import org.o2.core.helper.TransactionalHelper;
import org.o2.data.redis.client.RedisCacheClient;
import org.o2.metadata.console.api.co.PosAddressCO;
import org.o2.metadata.console.api.dto.PosAddressQueryInnerDTO;
import org.o2.metadata.console.api.dto.PosQueryInnerDTO;
import org.o2.metadata.console.api.dto.RegionQueryLovInnerDTO;
import org.o2.metadata.console.api.vo.PosVO;
import org.o2.metadata.console.app.service.PosService;
import org.o2.metadata.console.app.service.SourcingCacheUpdateService;
import org.o2.metadata.console.infra.constant.MetadataConstants;
import org.o2.metadata.console.infra.constant.PosConstants;
import org.o2.metadata.console.infra.convertor.PosAddressConverter;
import org.o2.metadata.console.infra.convertor.PosConverter;
import org.o2.metadata.console.infra.entity.*;
import org.o2.metadata.console.infra.redis.PosRedis;
import org.o2.metadata.console.infra.repository.*;
import org.o2.metadata.management.client.domain.co.PosCO;
import org.o2.metadata.management.client.domain.dto.PosDTO;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.StringRedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.ArrayList;
import java.util.HashMap;
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
    private final PosRedis posRedis;
    private final RedisCacheClient redisCacheClient;
    private final TransactionalHelper transactionalHelper;
    private final SourcingCacheUpdateService sourcingCacheService;


    public PosServiceImpl(PosRepository posRepository,
                          PostTimeRepository postTimeRepository,
                          PosAddressRepository posAddressRepository,
                          RegionRepository regionRepository,
                          PosRelCarrierRepository posRelCarrierRepository,
                          CarrierRepository carrierRepository,
                          PosRedis posRedis,
                          RedisCacheClient redisCacheClient, TransactionalHelper transactionalHelper, SourcingCacheUpdateService sourcingCacheService) {
        this.posRepository = posRepository;
        this.postTimeRepository = postTimeRepository;
        this.posAddressRepository = posAddressRepository;
        this.regionRepository = regionRepository;
        this.posRelCarrierRepository = posRelCarrierRepository;
        this.carrierRepository = carrierRepository;
        this.posRedis = posRedis;
        this.redisCacheClient = redisCacheClient;
        this.transactionalHelper = transactionalHelper;
        this.sourcingCacheService = sourcingCacheService;
    }

    @Override
    public Pos create(final Pos pos) {
        // 名称校验
        validPosNameUnique(pos);
        validatePosCode(pos);
        PosAddress address = pos.getAddress();
        transactionalHelper.transactionOperation(() -> {
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
            List<String> posCodes = new ArrayList<>();
            posCodes.add(pos.getPosCode());
            // 同步Redis
            posRedis.syncPosToRedis(posCodes, pos.getTenantId());
        });
        sourcingCacheService.refreshSourcingCache(pos.getTenantId(), this.getClass().getSimpleName());
        return pos;
    }

    @Override
    public Pos update(final Pos pos) {
        validPosNameUnique(pos);
        validatePosCode(pos);
        transactionalHelper.transactionOperation(() ->{
            PosAddress oldAddress = null;
            final PosAddress address;
            if ((address = pos.getAddress()) != null) {
                // 查询旧地址信息
                PosAddressQueryInnerDTO posAddressQueryInnerDTO = new PosAddressQueryInnerDTO();
                List<String> posCodes = new ArrayList<>();
                posCodes.add(pos.getPosCode());
                posAddressQueryInnerDTO.setPosCodes(posCodes);
                List<PosAddress> oldAddressList = posAddressRepository.listPosAddress(posAddressQueryInnerDTO, pos.getTenantId());
                if (CollectionUtils.isNotEmpty(oldAddressList)) {
                    oldAddress = oldAddressList.get(0);
                }

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
            // 更新同步服务点Redis
            updatePosToRedis(oldAddress, pos.getPosCode(), pos.getTenantId());
        });
        sourcingCacheService.refreshSourcingCache(pos.getTenantId(), this.getClass().getSimpleName());
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

    @Override
    public List<Pos> selectByCondition(Pos query) {
        return posRepository.listPosByCondition(query);
    }

    @Override
    public Map<String, String> listPosName(Long tenantId, PosQueryInnerDTO posQueryInnerDTO) {
        Map<String, String> resultMap = new HashMap<>();
        List<Pos> posList = posRepository.listPosByCode(tenantId, posQueryInnerDTO);
        for(Pos pos : posList){
            resultMap.put(pos.getPosCode(), pos.getPosName());
        }
        return resultMap;
    }

    @Override
    public PosCO savePos(PosDTO posDTO) {
        Pos pos = PosConverter.dtoToPoObject(posDTO);
        Pos posQuery = new Pos();
        posQuery.setPosCode(pos.getPosCode());
        posQuery.setTenantId(pos.getTenantId());
        Pos posResult = posRepository.selectOne(posQuery);
        if (ObjectUtils.isEmpty(posResult)) {
            Pos query = new Pos();
            query.setPosName(pos.getPosName());
            query.setTenantId(pos.getTenantId());
            Pos result = posRepository.selectOne(query);
            if (ObjectUtils.isEmpty(result)) {
                posResult = this.create(pos);
            } else {
                return PosConverter.poToCoObject(result);
            }

        } else {
            posResult.setPosName(pos.getPosName());
            PosAddress posAddress = posAddressRepository.selectByPrimaryKey(posResult.getAddressId());
            posAddress.setCityCode(pos.getAddress().getCityCode());
            posAddress.setCountryCode(pos.getAddress().getCountryCode());
            posAddress.setDistrictCode(pos.getAddress().getDistrictCode());
            posAddress.setLatitude(pos.getAddress().getLatitude());
            posAddress.setLongitude(pos.getAddress().getLongitude());
            posAddress.setStreetName(pos.getAddress().getStreetName());
            posAddress.setPhoneNumber(pos.getAddress().getPhoneNumber());
            posAddress.setRegionCode(pos.getAddress().getRegionCode());
            posAddress.setRegionName(pos.getAddress().getRegionName());
            posAddress.setCityName(pos.getAddress().getCityName());
            posAddress.setDistrictName(pos.getAddress().getDistrictName());
            posAddress.setTenantId(pos.getTenantId());
            posAddress.setPostcode(pos.getAddress().getPostcode());

            posResult.setAddress(posAddress);
            posResult = this.update(posResult);
        }
        return PosConverter.poToCoObject(posResult);
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
                    .forEach(c -> posRelCarrierRepository.updateIsDefault(c.getPosRelCarrierId(), c.getPosId(),1,c.getTenantId()));
        }
    }

    /**
     * 更新门店信息
     *
     * @param oldPosAddress 门店旧地址
     * @param posCode 门店编码
     * @param tenantId 租户Id
     */
    private void updatePosToRedis(PosAddress oldPosAddress, String posCode, Long tenantId) {
        List<String> posCodes = new ArrayList<>();
        posCodes.add(posCode);
        List<PosInfo> posInfos = posRepository.listPosInfoByCode(null, posCodes, MetadataConstants.PosType.STORE, tenantId);
        if (CollectionUtils.isEmpty(posInfos)) {
            return;
        }
        PosInfo posInfo = posInfos.get(0);
        // 设置服务点地址名称
        setPosAddress(posInfo, tenantId);
        // 更新服务点门店信息
        redisCacheClient.executePipelined(new RedisCallback<Object>() {

            @Override
            public Object doInRedis(RedisConnection connection) throws DataAccessException {
                StringRedisConnection stringConnection = (StringRedisConnection) connection;

                String oldCityStoreKey = PosConstants.RedisKey.getPosCityStoreKey(tenantId, oldPosAddress.getRegionCode(), oldPosAddress.getCityCode());
                String oldDistrictStoreKey = PosConstants.RedisKey.getPosDistrictStoreKey(tenantId, oldPosAddress.getRegionCode(), oldPosAddress.getCityCode(), oldPosAddress.getDistrictCode());
                String newCityStoreKey = PosConstants.RedisKey.getPosCityStoreKey(tenantId, posInfo.getRegionCode(), posInfo.getCityCode());
                String newDistrictStoreKey = PosConstants.RedisKey.getPosDistrictStoreKey(tenantId, posInfo.getRegionCode(), posInfo.getCityCode(), posInfo.getDistrictCode());
                String posDetailKey = PosConstants.RedisKey.getPosDetailKey(tenantId);

                // 删除旧的门店地址信息
                stringConnection.sRem(oldCityStoreKey, posCode);
                stringConnection.sRem(oldDistrictStoreKey, posCode);
                // 添加新的门店地址信息
                stringConnection.sAdd(newCityStoreKey, posCode);
                stringConnection.sAdd(newDistrictStoreKey, posCode);
                stringConnection.hSet(posDetailKey, posCode, JsonHelper.objectToString(posInfo));
                return null;
            }
        });
    }

    /**
     * 设置服务点地址名称
     *
     * @param posInfo 服务点
     * @param tenantId 租户Id
     */
    private void setPosAddress(PosInfo posInfo, Long tenantId) {
        List<String> regionCodes = new ArrayList<>();
        regionCodes.add(posInfo.getCityCode());
        regionCodes.add(posInfo.getDistrictCode());
        regionCodes.add(posInfo.getRegionCode());
        RegionQueryLovInnerDTO dto = new RegionQueryLovInnerDTO();
        dto.setRegionCodes(regionCodes);
        List<Region> regionList = regionRepository.listRegionLov(dto,tenantId);
        Map<String,Region> regionMap = Maps.newHashMapWithExpectedSize(regionList.size());
        for (Region region : regionList) {
            regionMap.put(region.getRegionCode(),region);
        }
        //市
        String cityCode = posInfo.getCityCode();
        Region city = regionMap.get(cityCode);
        if (null != city) {
            posInfo.setCityName(city.getRegionName());
            posInfo.setCountryName(city.getCountryName());
            posInfo.setCountryCode(city.getCountryCode());
        }
        //区
        String districtCode = posInfo.getDistrictCode();
        Region district = regionMap.get(districtCode);
        if (null != district) {
            posInfo.setDistrictName(district.getRegionName());
        }
        //省
        String regionCode = posInfo.getRegionCode();
        Region region = regionMap.get(regionCode);
        if (null != region) {
            posInfo.setRegionName(region.getRegionName());
        }
    }

}
