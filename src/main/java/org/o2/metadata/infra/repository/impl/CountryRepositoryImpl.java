package org.o2.metadata.infra.repository.impl;

import org.hzero.mybatis.base.impl.BaseRepositoryImpl;
import org.o2.ext.metadata.domain.entity.Country;
import org.o2.ext.metadata.domain.repository.CountryRepository;
import org.springframework.stereotype.Component;

/**
 * @author tingting.wang@hand-china.com 2019-3-25
 */
@Component
public class CountryRepositoryImpl extends BaseRepositoryImpl<Country> implements CountryRepository {

}

