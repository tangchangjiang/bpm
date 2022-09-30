package org.o2.metadata.app.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.o2.metadata.api.co.CurrencyCO;
import org.o2.metadata.api.co.LovValuesCO;
import org.o2.metadata.api.co.RoleCO;
import org.o2.metadata.api.dto.RegionQueryLovInnerDTO;
import org.o2.metadata.app.bo.UomBO;
import org.o2.metadata.app.service.LovAdapterService;
import org.o2.metadata.infra.entity.Region;
import org.o2.metadata.infra.lovadapter.repository.BaseLovQueryRepository;
import org.o2.metadata.infra.lovadapter.repository.IdpLovQueryRepository;
import org.o2.metadata.infra.lovadapter.repository.RegionLovQueryRepository;
import org.o2.metadata.infra.lovadapter.repository.SqlLovQueryRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;


/**
 * 值集查询
 *
 * @author yipeng.zhu@hand-china.com 2021-08-30
 **/
@Service
@Slf4j
public class LovAdapterServiceImpl implements LovAdapterService {
    private final IdpLovQueryRepository idpLovQueryRepository;
    private final BaseLovQueryRepository baseLovQueryRepository;
    private final RegionLovQueryRepository regionLovQueryRepository;
    private final SqlLovQueryRepository sqlLovQueryRepository;

    public LovAdapterServiceImpl(IdpLovQueryRepository idpLovQueryRepository,
                                 BaseLovQueryRepository baseLovQueryRepository,
                                 RegionLovQueryRepository regionLovQueryRepository,
                                 SqlLovQueryRepository sqlLovQueryRepository) {
        this.idpLovQueryRepository = idpLovQueryRepository;
        this.baseLovQueryRepository = baseLovQueryRepository;
        this.regionLovQueryRepository = regionLovQueryRepository;
        this.sqlLovQueryRepository = sqlLovQueryRepository;
    }


    @Override
    public String queryLovValueMeaning(Long tenantId, String lovCode, String lovValue) {
        return idpLovQueryRepository.queryLovValueMeaning(tenantId, lovCode, lovValue);
    }

    @Override
    public List<LovValuesCO> queryIdpLov(Long tenantId, List<String> lovCodes) {
        return idpLovQueryRepository.queryIdpLov(tenantId, lovCodes);
    }

    @Override
    public Map<String, CurrencyCO> findCurrencyByCodes(Long tenantId, List<String> currencyCodes) {
        return baseLovQueryRepository.findCurrencyByCodes(tenantId, currencyCodes);
    }

    @Override
    public Map<String, UomBO> findUomByCodes(Long tenantId, List<String> uomCodes) {
        return baseLovQueryRepository.findUomByCodes(tenantId, uomCodes);
    }

    @Override
    public List<Region> queryRegion(Long tenantId, RegionQueryLovInnerDTO innerDTO) {
        return regionLovQueryRepository.queryRegion(tenantId, innerDTO);
    }

    @Override
    public Map<String, RoleCO> findRoleByCodes(Long organizationId, List<String> roleCodes) {
        return sqlLovQueryRepository.findRoleByCodes(organizationId, roleCodes);
    }
}
