package org.o2.metadata.app.bo;

import lombok.Data;

import java.io.Serializable;

/**
 * 系统参数业务对象
 *
 * @author mark.bao@hand-china.com 2019-05-30
 */
@Data
public class SysParameterBO implements Serializable {
    private String parameterCode;
    private String parameterValue;
    private Integer isActive;
}
