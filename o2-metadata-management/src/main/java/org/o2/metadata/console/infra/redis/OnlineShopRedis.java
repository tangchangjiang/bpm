package org.o2.metadata.console.infra.redis;

import org.o2.metadata.console.api.dto.OnlineShopQueryInnerDTO;
import org.o2.metadata.console.infra.entity.OnlineShop;
import org.o2.metadata.console.infra.entity.OnlineShopRelWarehouse;

import java.util.List;

/**
 *
 * 网店
 *
 * @author yipeng.zhu@hand-china.com 2021-08-06
 **/
public interface OnlineShopRedis {
     /**
      * 更新网店
      * @param onlineShopCode 网店编码
      * @param tenantId 租户ID
      */
     void updateRedis(String onlineShopCode,Long tenantId);

     /**
      * 更新网店关联仓库
      * @param list 网店关联仓库数据
      * @param tenantId 租户ID
      * @param handleType 操作类型
      */
     void batchUpdateShopRelWh(List<OnlineShopRelWarehouse> list,Long tenantId,String handleType);

     /**
      * 批量更新
      * @param list 网店
      * @param tenantId 租户ID
      */
     void batchUpdateRedis(List<OnlineShop> list, Long tenantId);
     
     /**
      * 查询网店
      * @param innerDTO 查询条件
      * @param tenantId 租户ID
      * @return 网店数据
      */
     List<OnlineShop> select(OnlineShopQueryInnerDTO innerDTO,Long tenantId);
}
