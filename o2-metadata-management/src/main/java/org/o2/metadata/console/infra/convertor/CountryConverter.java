package org.o2.metadata.console.infra.convertor;

import org.o2.metadata.console.api.vo.CountryVO;
import org.o2.metadata.console.infra.entity.Country;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * 国家
 *
 * @author yipeng.zhu@hand-china.com 2021-08-04
 **/
public class CountryConverter {
    /**
     * po -> vo
     * @param country 国家
     * @return vo
     */
    public static CountryVO poToVoObject(Country country){

        if (country == null) {
            return null;
        }
        CountryVO countryVO = new CountryVO();
        countryVO.setCountryCode(country.getCountryCode());
        countryVO.setCountryName(country.getCountryName());
        return countryVO;
    }

    /**
     * PO 转 VO
     * @param countryList 国家
     * @return  list
     */
    public static List<CountryVO> poToVoListObjects(List<Country> countryList) {
        List<CountryVO> countryVOList = new ArrayList<>();
        if (countryList == null) {
            return countryVOList;
        }
        for (Country country : countryList) {
            countryVOList.add(poToVoObject(country));
        }
        return countryVOList;
    }
}
