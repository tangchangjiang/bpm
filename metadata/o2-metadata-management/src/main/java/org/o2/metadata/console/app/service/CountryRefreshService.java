package org.o2.metadata.console.app.service;

import org.o2.metadata.console.api.dto.CountryRefreshDTO;

public interface CountryRefreshService {

    /**
     * 刷新国家信息文件
     * @param countryRefreshDTO 刷新DTO
     */
    void refreshCountryInfoFile(CountryRefreshDTO countryRefreshDTO);
}
