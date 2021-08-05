package org.o2.metadata.console.infra.repository.impl;

import org.hzero.boot.platform.lov.adapter.LovAdapter;
import org.hzero.mybatis.base.impl.BaseRepositoryImpl;
import org.o2.core.helper.FastJsonHelper;
import org.o2.metadata.console.api.dto.CountryQueryLovDTO;
import org.o2.metadata.console.infra.constant.RegionConstants;
import org.o2.metadata.console.infra.entity.Country;
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
    private final LovAdapter lovAdapter;

    public CountryRepositoryImpl(LovAdapter lovAdapter) {
        this.lovAdapter = lovAdapter;
    }

    @Override
    public List<Country> listCountryLov(CountryQueryLovDTO regionQueryLov, Long tenantId) {
        List<Country> countryList = new ArrayList<>();
        Map<String,String> queryParams = new HashMap<>(16);
        regionQueryLov.setTenantId(tenantId);
        List<Map<String,Object>> list = lovAdapter.queryLovData(RegionConstants.RegionLov.COUNTRY_LOV_CODE.getCode(),tenantId, null,  regionQueryLov.getPage(), regionQueryLov.getSize() , queryParams);
        if (list.isEmpty()){
            return countryList;
        }
        for (Map<String, Object> map : list) {
            countryList.add(FastJsonHelper.stringToObject(FastJsonHelper.objectToString(map), Country.class));
        }
        return countryList;
    }
}

