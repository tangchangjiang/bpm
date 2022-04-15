package org.o2.metadata.console.infra.repository.impl;

import com.google.common.base.Preconditions;
import org.apache.commons.lang3.StringUtils;
import org.hzero.mybatis.base.impl.BaseRepositoryImpl;
import org.o2.metadata.console.api.dto.PosDTO;
import org.o2.metadata.console.api.dto.RegionQueryLovInnerDTO;
import org.o2.metadata.console.infra.entity.*;
import org.o2.metadata.console.infra.mapper.PosAddressMapper;
import org.o2.metadata.console.infra.mapper.PosMapper;
import org.o2.metadata.console.infra.mapper.PostTimeMapper;
import org.o2.metadata.console.infra.repository.PosRepository;
import org.o2.metadata.console.infra.repository.RegionRepository;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 服务点信息 资源库实现
 *
 * @author tingting.wang@hand-china.com 2019-3-25
 */
@Component
public class PosRepositoryImpl extends BaseRepositoryImpl<Pos> implements PosRepository {
    private final PosMapper posMapper;
    private final PosAddressMapper posAddressMapper;
    private final PostTimeMapper postTimeMapper;
    private final RegionRepository regionRepository;

    public PosRepositoryImpl(final PosMapper posMapper,
                             final PosAddressMapper posAddressMapper,
                             final PostTimeMapper postTimeMapper, RegionRepository regionRepository) {
        this.posMapper = posMapper;
        this.posAddressMapper = posAddressMapper;
        this.postTimeMapper = postTimeMapper;
        this.regionRepository = regionRepository;
    }

    @Override
    public List<Pos> listPosWithAddressByCondition(PosDTO pos) {
        Preconditions.checkArgument(null != pos.getTenantId(), "pos must contains tenantId");
        List<Pos> posList =  posMapper.listPosWithAddressByCondition(pos);
        List<String> regionCodes = new ArrayList<>();
        if (posList.isEmpty()){
            return posList;
        }
        posList.forEach(bean->{
            if (StringUtils.isNotEmpty(bean.getRegionCode())){
                regionCodes.add(bean.getRegionCode());
            }
            if (StringUtils.isNotEmpty(bean.getCityCode())){
                regionCodes.add(bean.getCityCode());
            }
            if (StringUtils.isNotEmpty(bean.getDistrictCode())){
                regionCodes.add(bean.getDistrictCode());
            }
        });
        RegionQueryLovInnerDTO dto = new RegionQueryLovInnerDTO();
        dto.setTenantId(pos.getTenantId());
        dto.setRegionCodes(regionCodes);
        List<Region> regionList = regionRepository.listRegionLov(dto,pos.getTenantId());
        Map<String,String> map = new HashMap<>();
        for (Region region : regionList) {
            map.put(region.getRegionCode(), region.getRegionName());
        }
        posList.forEach(bean -> {
            String cityCode = bean.getCityCode();
            bean.setCityName(map.get(cityCode));
            String regionCode = bean.getRegionCode();
            bean.setRegionName(map.get(regionCode));
            String districtCode = bean.getDistrictCode();
            bean.setDistrictName(map.get(districtCode));
        });
        return posList;
    }

    @Override
    public Pos getPosWithAddressAndPostTimeByPosId(final Long tenantId,final Long posId) {
        final Pos pos = posMapper.getPosWithCarrierNameById(tenantId,posId);

        if (pos.getAddressId() != null) {
            PosAddress posAddress = posAddressMapper.selectByPrimaryKey(pos.getAddressId());
            pos.setAddress(posAddress);
            List<String> regionCodes = new ArrayList<>(3);
            String cityCode = posAddress.getRegionCode();
            if (StringUtils.isNotEmpty(cityCode)){
                regionCodes.add(cityCode);
            }
            String districtCode = posAddress.getDistrictCode();
            if (StringUtils.isNotEmpty(districtCode)){
                regionCodes.add(districtCode);
            }
            String regionCode =   posAddress.getRegionCode();
            if (StringUtils.isNotEmpty(regionCode)){
                regionCodes.add(regionCode);
            }
            RegionQueryLovInnerDTO dto = new RegionQueryLovInnerDTO();
            dto.setRegionCodes(regionCodes);
            dto.setTenantId(tenantId);
            Map<String,String> map = new HashMap<>();
            List<Region> regionList = regionRepository.listRegionLov(dto,tenantId);
            if (!regionList.isEmpty()) {
                for (Region region : regionList) {
                    map.put(region.getRegionCode(), region.getRegionName());
                }
                posAddress.setCityName(map.get(cityCode));
                posAddress.setDistrictName(map.get(districtCode));
                posAddress.setRegionName(map.get(regionCode));
                posAddress.setCountryName(regionList.get(0).getCountryName());
            }
        }

        // 接派单时间
        final PostTime postTime = new PostTime();
        postTime.setPosId(posId);
        final List<PostTime> postTimes = postTimeMapper.select(postTime);
        pos.setPostTimes(postTimes);
        return pos;
    }

    @Override
    public List<Pos> listUnbindPosList(final Long shopId, final String posCode, final String posName, final Long tenantId) {
        return posMapper.listUnbindPosList(shopId, posCode, posName, tenantId);
    }

    @Override
    public Pos getPosByCode(Long tenantId, final String posCode) {
        final Pos pos = new Pos();
        pos.setPosCode(posCode);
        pos.setTenantId(tenantId);
        return posMapper.selectOne(pos);
    }

    @Override
    public List<Pos> listPosByCondition(Pos pos) {
        return posMapper.listPosByCondition(pos);
    }

    @Override
    public List<PosInfo> listPosInfoByCode(List<Long> posIds, List<String> posCodes, String posTypeCode, Long tenantId) {
        return posMapper.listPosInfoByCode(posIds, posCodes, posTypeCode, tenantId);
    }
}
