package org.o2.metadata.console.infra.repository;

import org.hzero.mybatis.base.BaseRepository;
import org.o2.metadata.console.api.dto.CountryQueryLovDTO;
import org.o2.metadata.console.infra.entity.Country;

import java.util.List;

/**
 * @author tingting.wang@hand-china.com 2019-3-25
 */
public interface CountryRepository extends BaseRepository<Country> {

    /**
     *  查询地区sql值集
     * @param  regionQueryLov 查询条件
     * @param  tenantId 租户ID
     * @return map     */
    List<Country> listCountryLov(CountryQueryLovDTO regionQueryLov, Long tenantId);

}
