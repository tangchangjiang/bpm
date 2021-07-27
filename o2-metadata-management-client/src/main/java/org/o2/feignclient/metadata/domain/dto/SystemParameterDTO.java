package org.o2.feignclient.metadata.domain.dto;

import lombok.Data;

import java.util.Map;

/**
 *
 * 系统参数更新
 *
 * @author yipeng.zhu@hand-china.com 2021-07-27
 **/
@Data
public class SystemParameterDTO {
    /**
     * 参数编码
     */
    private String paramCode;
    /**
     * hashMap
     */
    private Map<String,String> hashMap;
}
