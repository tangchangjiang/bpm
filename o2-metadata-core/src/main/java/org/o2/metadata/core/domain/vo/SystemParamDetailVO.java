package org.o2.metadata.core.domain.vo;

import java.util.Set;

import lombok.Data;

/**
 * @author hongyun.wang01@hand-china.com
 */
@Data
public class SystemParamDetailVO {
    private String paramCode;

    private String kvValue;

    private Set<SystemParamValueVO> setValue;
}
