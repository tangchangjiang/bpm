package org.o2.metadata.console.app.bo;

import lombok.Data;
import org.o2.multi.language.core.annotation.O2RedisMultiLanguage;
import org.o2.multi.language.core.annotation.O2RedisMultiLanguageField;

/**
 * 网店redis
 *
 * @author yipeng.zhu@hand-china.com 20283-05-24
 **/
@Data
@O2RedisMultiLanguage(tlsTable = "o2md_online_shop_tl")
public class OnlineShopMultiRedisBO {

    /**
     * 网点名称
     */
    @O2RedisMultiLanguageField
    private String onlineShopName;

    /**
     * 网点编码
     */
    private String onlineShopCode;
    @O2RedisMultiLanguageField(tableUniqueKey=true)
    private Long  onlineShopId;

}
