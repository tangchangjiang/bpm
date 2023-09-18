package org.o2.metadata.console.api.dto;

import lombok.Data;

@Data
public class CurrencyDTO {
    private Long tenantId;
    private String currencyCode;
    private String currencyName;
    private int page;
    private int size;
}
