package org.o2.metadata.console.infra.repository.impl;

import org.hzero.mybatis.base.impl.BaseRepositoryImpl;
import org.o2.core.helper.JsonHelper;
import org.o2.metadata.console.api.dto.CountryQueryLovDTO;
import org.o2.metadata.console.infra.constant.RegionConstants;
import org.o2.metadata.console.infra.entity.Country;
import org.o2.metadata.console.infra.lovadapter.repository.HzeroLovQueryRepository;
import org.o2.metadata.console.infra.repository.CountryRepository;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author tingting.wang@hand-china.com 2019-3-25
 */
@Component
public class CountryRepositoryImpl extends BaseRepositoryImpl<Country> implements CountryRepository {
    private final HzeroLovQueryRepository hzeroLovQueryRepository;

    public CountryRepositoryImpl(HzeroLovQueryRepository hzeroLovQueryRepository) {
        this.hzeroLovQueryRepository = hzeroLovQueryRepository;
    }


    @Override
    public List<Country> listCountryLov(CountryQueryLovDTO regionQueryLov, Long tenantId) {
        List<Country> countryList = new ArrayList<>();
        Map<String,String> queryParams = new HashMap<>(16);
        queryParams.put(RegionConstants.RegionLov.COUNTRY_CODE.getCode(),regionQueryLov.getCountryCode());
        queryParams.put(RegionConstants.RegionLov.TENANT_ID.getCode(),String.valueOf(regionQueryLov.getTenantId()));
        List<Map<String,Object>> list = hzeroLovQueryRepository.queryLovValueMeaning(tenantId,RegionConstants.RegionLov.REGION_LOV_CODE.getCode(),  regionQueryLov.getPage(), regionQueryLov.getSize() , queryParams);
        if (list.isEmpty()){
            return countryList;
        }
        for (Map<String, Object> map : list) {
            countryList.add(JsonHelper.stringToObject(JsonHelper.objectToString(map), Country.class));
        }
        return countryList;
    }
}

