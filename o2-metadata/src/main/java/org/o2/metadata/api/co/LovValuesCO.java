package org.o2.metadata.api.co;

import lombok.Data;
import org.hzero.boot.platform.lov.dto.LovValueDTO;

import java.util.List;

/**
 * 值集集合
 * @author peng.xu@hand-china.com 2022/1/21
 */
@Data
public class LovValuesCO {
    /**
     * 值集编码
     */
    private String lovCode;
    /**
     * 值集信息
     */
    private List<LovValueDTO> lovValueList;
}
