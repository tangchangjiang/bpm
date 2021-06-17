package org.o2.metadata.core.infra.repository.impl;

import org.o2.metadata.core.domain.entity.Country;
import org.hzero.mybatis.base.impl.BaseRepositoryImpl;
import org.o2.metadata.core.domain.repository.CountryRepository;
import org.springframework.stereotype.Component;

/**
 * @author tingting.wang@hand-china.com 2019-3-25
 */
@Component
public class CountryRepositoryImpl extends BaseRepositoryImpl<Country> implements CountryRepository {

}
