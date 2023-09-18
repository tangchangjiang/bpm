package org.o2.metadata.console.app.service;

import io.choerodon.core.domain.Page;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import org.o2.metadata.console.api.dto.CurrencyDTO;
import org.o2.metadata.console.api.vo.CurrencyVO;

public interface CurrencyService {

    /**
     * 分页查询币种信息
     *
     * @param currencyDTO 查询条件
     * @param pageRequest 分页参数
     * @return 币种信息
     */
    Page<CurrencyVO> page(CurrencyDTO currencyDTO, PageRequest pageRequest);
}
