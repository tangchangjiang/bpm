package org.o2.metadata.console.api.dto;

import lombok.Data;
import org.hzero.core.base.BaseConstants;

@Data
public class CurrencyDTO {
    private Long tenantId;
    private String currencyCode;
    private String currencyName;
    private int page;
    private int size;
    private Integer enabledFlag = BaseConstants.Flag.YES;
}
