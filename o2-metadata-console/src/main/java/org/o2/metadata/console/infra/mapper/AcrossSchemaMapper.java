package org.o2.metadata.console.infra.mapper;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 跨schema查询
 *
 * @author yong.nie@hand-china.com
 * @date 2020/3/30 10:53
 **/

@Component
public interface AcrossSchemaMapper {

    List<String> selectSkuByWarehouse(@Param("warehouseCode") String warehouseCode,
                                      @Param("tenantId") Long tenantId);

}
