package org.o2.metadata.console.infra.lovadapter.address;

import com.google.common.base.Joiner;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.hzero.core.base.BaseConstants;
import org.o2.metadata.console.infra.constant.O2LovConstants;
import org.o2.metadata.console.infra.lovadapter.repository.HzeroLovQueryRepository;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 省市区 国家翻译工具类
 *
 * @author miao.chen01@hand-china.com 2021-08-16
 */
@Component(value = "MdAddressTranslationHelper")
@Slf4j
public class AddressTranslationHelper {

    private final HzeroLovQueryRepository hzeroLovQueryRepository;

    public AddressTranslationHelper(HzeroLovQueryRepository hzeroLovQueryRepository) {
        this.hzeroLovQueryRepository = hzeroLovQueryRepository;
    }

    /**
     * 地址翻译
     *
     * @param address 地址信息
     */
    public void translate(IAddress address) {
        Map<String, String> queryRegionMap = Maps.newHashMapWithExpectedSize(BaseConstants.Digital.TWO);
        queryRegionMap.put(O2LovConstants.RegionInfo.ADDRESS_TYPE, address.getAddressType());
        queryRegionMap.put(O2LovConstants.RegionInfo.PARAM_REGION_CODES,
                Joiner.on(BaseConstants.Symbol.COMMA).skipNulls().join(address.getRegionCode(), address.getCityCode(), address.getDistrictCode()));
        queryRegionMap.put(O2LovConstants.RegionInfo.COUNTRY_CODE, address.getCountryCode());

        List<Map<String, Object>> regionMeaningList = new ArrayList<>();
        try {
            regionMeaningList = hzeroLovQueryRepository.queryLovValueMeaning(address.getTenantId(), O2LovConstants.RegionInfo.LOV_CODE, queryRegionMap);
        } catch (Exception e) {
            log.error("AddressTranslationHelper.translate region fail {}", e);
        }

        if (CollectionUtils.isEmpty(regionMeaningList)) {
            return;
        }

        Map<String, String> regionMap = Maps.newHashMapWithExpectedSize(BaseConstants.Digital.FOUR);
        for (Map<String, Object> map : regionMeaningList) {
            regionMap.put(String.valueOf(map.get(O2LovConstants.RegionInfo.REGION_CODE)), String.valueOf(map.get(O2LovConstants.RegionInfo.REGION_NAME)));
            regionMap.put(String.valueOf(map.get(O2LovConstants.RegionInfo.COUNTRY_CODE)), String.valueOf(map.get(O2LovConstants.RegionInfo.COUNTRY_NAME)));
        }
        address.setCountryName(regionMap.getOrDefault(address.getCountryCode(), address.getCountryCode()));
        address.setRegionName(regionMap.getOrDefault(address.getRegionCode(), address.getRegionCode()));
        address.setCityName(regionMap.getOrDefault(address.getCityCode(), address.getCityCode()));
        address.setDistrictName(regionMap.getOrDefault(address.getDistrictCode(), address.getDistrictCode()));
    }
}
