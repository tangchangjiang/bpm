package org.o2.metadata.app.service;

import org.o2.ext.metadata.domain.entity.Country;

import java.util.List;

/**
 * @author tingting.wang@hand-china.com 2019-3-25
 */
public interface CountryService {
    /**
     * 创建国家定义
     *
     * @param country 国家信息
     * @return 创建完成之后的国家
     */
    Country createCountry(Country country);

    /**
     * 更新国家定义
     *
     * @param country 国家信息
     * @return 更新完成之后的国家
     */
    Country updateCountry(Country country);

    /**
     * 批量禁用国家定义
     *
     * @param countryList 国家定义信息列表
     * @return 禁用后国家定义信息列表
     */
    List<Country> batchDisableCountry(List<Country> countryList);

}
