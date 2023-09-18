package org.o2.metadata.console.app.service.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import io.choerodon.core.domain.Page;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import org.apache.commons.collections4.CollectionUtils;
import org.hzero.core.base.BaseConstants;
import org.hzero.core.util.ResponseUtils;
import org.o2.metadata.console.api.dto.CurrencyDTO;
import org.o2.metadata.console.api.vo.CurrencyVO;
import org.o2.metadata.console.app.service.CurrencyService;
import org.o2.metadata.console.infra.feign.CurrencyRemoteService;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class CurrencyServiceImpl implements CurrencyService {

    private final CurrencyRemoteService currencyRemoteService;

    public CurrencyServiceImpl(CurrencyRemoteService currencyRemoteService) {
        this.currencyRemoteService = currencyRemoteService;
    }

    /**
     * 分页查询币种信息
     *
     * @param currencyDTO 查询条件
     * @param pageRequest 分页参数
     * @return 币种信息
     */
    @Override
    public Page<CurrencyVO> page(CurrencyDTO currencyDTO, PageRequest pageRequest) {
        Long tenantId = currencyDTO.getTenantId();
        currencyDTO.setPage(pageRequest.getPage());
        currencyDTO.setSize(pageRequest.getSize());
        Page<CurrencyVO> currencyVOPage = ResponseUtils.getResponse(currencyRemoteService.queryCurrency(tenantId,
                currencyDTO.getCurrencyCode(), currencyDTO.getCurrencyName(),
                currencyDTO.getPage(), currencyDTO.getSize()), new TypeReference<Page<CurrencyVO>>() {
        });
        if (BaseConstants.DEFAULT_TENANT_ID.equals(tenantId) && (Objects.isNull(currencyVOPage)
                || (CollectionUtils.isEmpty(currencyVOPage.getContent())) && currencyVOPage.getTotalElements() == 0)) {
            currencyVOPage = ResponseUtils.getResponse(currencyRemoteService.queryCurrency(BaseConstants.DEFAULT_TENANT_ID,
                    currencyDTO.getCurrencyCode(), currencyDTO.getCurrencyName(),
                    currencyDTO.getPage(), currencyDTO.getSize()), new TypeReference<Page<CurrencyVO>>() {
            });
        }
        return currencyVOPage;
    }
}
