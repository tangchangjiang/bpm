package org.o2.metadata.console.infra.lovadapter.address;

import org.o2.metadata.console.infra.constant.O2LovConstants;

/**
 * 地址信息
 *
 * @author miao.chen01@hand-china.com 2021-08-16
 */
public interface IAddress {

    /**
     * 设置租户id
     *
     * @param tenantId 租户id
     */
    void setTenantId(Long tenantId);

    /**
     * 获取租户id
     */
    Long getTenantId();

    /**
     * 获取地址类型
     *
     * @return 地址类型（country/countryDetail/region）
     */
    default String getAddressType() {
        return O2LovConstants.AddressType.REGION;
    }

    /**
     * 设置地址类型
     *
     * @param addressType 地址类型（country/countryDetail/region）
     */
    default void setAddressType(String addressType) {

    }

    /**
     * 获取国家编码
     *
     * @return 国家编码
     */
    String getCountryCode();

    /**
     * 设置国家编码
     *
     * @param countryCode 国家编码
     */
    void setCountryCode(String countryCode);

    /**
     * 获取国家名称
     *
     * @return 获取国家名称
     */
    String getCountryName();

    /**
     * 设置国家名称
     *
     * @param countryName 国家名称
     */
    void setCountryName(String countryName);

    /**
     * 获取省编码
     *
     * @return 省编码
     */
    String getRegionCode();

    /**
     * 设置省编码
     *
     * @param regionCode 省编码
     */
    void setRegionCode(String regionCode);

    /**
     * 获取省名称
     *
     * @return 省名称
     */
    String getRegionName();

    /**
     * 设置省名称
     *
     * @param regionName 省名称
     */
    void setRegionName(String regionName);

    /**
     * 获取市编码
     *
     * @return 市编码
     */
    String getCityCode();

    /**
     * 设置市code
     *
     * @param cityCode 市code
     */
    void setCityCode(String cityCode);

    /**
     * 获取市名称
     *
     * @return 市名称
     */
    String getCityName();

    /**
     * 设置市名称
     *
     * @param cityName 市名称
     */
    void setCityName(String cityName);

    /**
     * 区编码
     *
     * @return 区编码
     */
    String getDistrictCode();

    /**
     * 设置区编码
     *
     * @param districtCode 区编码
     */
    void setDistrictCode(String districtCode);

    /**
     * 获取区名称
     *
     * @return 区名
     */
    String getDistrictName();

    /**
     * 设置区名
     *
     * @param districtName 区名
     */
    void setDistrictName(String districtName);
}
