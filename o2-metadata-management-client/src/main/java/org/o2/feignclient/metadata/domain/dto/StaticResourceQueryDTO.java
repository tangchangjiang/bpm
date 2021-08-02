package org.o2.feignclient.metadata.domain.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


import java.util.List;

/**
 * 静态资源文件查询DTO
 *
 * @author zhanpeng.jiang@hand-china.com
 * @date 2021/07/30 11:57
 */
@Data
public class StaticResourceQueryDTO {

    @ApiModelProperty(value = "资源编码列表", required = true)
    private List<String> resourceCodeList;

}
