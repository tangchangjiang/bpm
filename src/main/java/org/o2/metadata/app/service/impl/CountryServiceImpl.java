package org.o2.metadata.app.service.impl;

import io.choerodon.core.exception.CommonException;
import io.choerodon.mybatis.service.BaseServiceImpl;
import org.hzero.core.base.BaseConstants;
import org.o2.metadata.app.service.CountryService;
import org.o2.metadata.domain.entity.Country;
import org.o2.metadata.domain.repository.CountryRepository;
import org.o2.metadata.infra.constants.BasicDataConstants;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author tingting.wang@hand-china.com 2019-3-25
 */
@Service
public class CountryServiceImpl extends BaseServiceImpl<Country> implements CountryService {
    private final CountryRepository countryRepository;

    public CountryServiceImpl(final CountryRepository countryRepository) {
        this.countryRepository = countryRepository;
    }

    @Override
    public Country createCountry(final Country country) {
        validateCountryCodeRepeat(country);
        validateCountryNameRepeat(country);
        countryRepository.insertSelective(country);
        return country;
    }

    @Override
    public Country updateCountry(final Country country) {
        final Country exists = countryRepository.selectByPrimaryKey(country.getCountryId());
        if (exists == null) {
            throw new CommonException(BaseConstants.ErrorCode.DATA_NOT_EXISTS);
        }
        country.setCountryCode(exists.getCountryCode());
        countryRepository.updateByPrimaryKeySelective(country);
        return country;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<Country> batchDisableCountry(Long organizationId,final List<Country> countryList) {
        for (final Country country : countryList) {
            country.setTenantId(organizationId);
            country.setCountryCode(null);
            country.setCountryName(null);
            country.setEnabledFlag(BaseConstants.Flag.NO);
        }
        return countryRepository.batchUpdateByPrimaryKeySelective(countryList);
    }

    private void validateCountryCodeRepeat(final Country country) {
        // 验证国家Code是否重复
        final List<Country> countryList = countryRepository.select(Country.builder().countryCode(country.getCountryCode()).tenantId(country.getTenantId()).build());
        if (!countryList.isEmpty()) {
            throw new CommonException(BasicDataConstants.ErrorCode.BASIC_DATA_DUPLICATE_CODE, "Country(" + country.getCountryCode() + ")");
        }
    }

    private void validateCountryNameRepeat(final Country country) {
        // 验证国家名称是否重复
        final List<Country> countryList = countryRepository.select(Country.builder().countryName(country.getCountryName()).tenantId(country.getTenantId()).build());
        if (!countryList.isEmpty()) {
            throw new CommonException(BasicDataConstants.ErrorCode.BASIC_DATA_DUPLICATE_NAME, "Country(" + country.getCountryCode() + ")");
        }
    }
}
