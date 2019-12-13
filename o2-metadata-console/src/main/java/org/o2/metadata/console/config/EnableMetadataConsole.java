package org.o2.metadata.console.config;

import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Component;
import springfox.documentation.service.Tag;
import springfox.documentation.spring.web.plugins.Docket;

/**
 * @author wenjun.deng01@hand-china.com 2019-01-29
 */
@Component
@ComponentScan({
        "org.o2.metadata.console.config",
        "org.o2.metadata.console.api",
        "org.o2.metadata.console.app"
})
@EnableDubbo(scanBasePackages = "org.o2.metadata.console.api.rpc")
public class EnableMetadataConsole {
    public static final String ADDRESS_MAPPING = "Address Mapping";
    public static final String CARRIER = "Carrier";
    public static final String CARRIER_DELIVERY_RANGE = "Carrier Delivery Range";
    public static final String CARRIER_MAPPING = "Carrier Mapping";
    public static final String COUNTRY = "Country";
    public static final String NEIGHBORING_REGION = "Neighboring Region";
    public static final String ONLINE_SHOP = "Online Shop";
    public static final String ONLINE_SHOP_INF_AUTH = "Online Shop API Auth";
    public static final String ONLINE_SHOP_POS_REL = "Online Shop POS Relationship";
    public static final String POS_ADDRESS = "Pos Address";
    public static final String POS = "POS (Point Of Service)";
    public static final String POS_REL_CARRIER = "Pos Rel Carrier";
    public static final String REGION = "Region";
    public static final String SYS_PARAMETER_SETTING = "System Parameter Setting";
    public static final String FREIGHT_TEMPLATE = "Freight Template";
    public static final String FREIGHT_TEMPLATE_DETAIL = "Freight Template Detail";
    public static final String PLATFORM_UOM = "Platform Uom";
    public static final String CATALOG = "Catalog";

    @Autowired
    public EnableMetadataConsole(final Docket docket) {
        docket.tags(new Tag(EnableMetadataConsole.ADDRESS_MAPPING, "地址匹配"))
                .tags(new Tag(EnableMetadataConsole.CARRIER, "承运商管理"))
                .tags(new Tag(EnableMetadataConsole.CARRIER_DELIVERY_RANGE, "承运商送达范围管理"))
                .tags(new Tag(EnableMetadataConsole.CARRIER_MAPPING, "承运商匹配"))
                .tags(new Tag(EnableMetadataConsole.COUNTRY, "国家定义"))
                .tags(new Tag(EnableMetadataConsole.NEIGHBORING_REGION, "临近省管理"))
                .tags(new Tag(EnableMetadataConsole.ONLINE_SHOP, "网店信息管理"))
                .tags(new Tag(EnableMetadataConsole.ONLINE_SHOP_INF_AUTH, "网店接口权限"))
                .tags(new Tag(EnableMetadataConsole.ONLINE_SHOP_POS_REL, "网店关联服务点管理"))
                .tags(new Tag(EnableMetadataConsole.POS_ADDRESS, "服务点地址管理"))
                .tags(new Tag(EnableMetadataConsole.POS, "服务点信息管理"))
                .tags(new Tag(EnableMetadataConsole.POS_REL_CARRIER, "服务点关联承运商管理"))
                .tags(new Tag(EnableMetadataConsole.REGION, "区域定义"))
                .tags(new Tag(EnableMetadataConsole.SYS_PARAMETER_SETTING, "系统参数配置管理"))
                .tags(new Tag(EnableMetadataConsole.FREIGHT_TEMPLATE, "运费模板管理"))
                .tags(new Tag(EnableMetadataConsole.FREIGHT_TEMPLATE_DETAIL, "运费模板明细管理"))
                .tags(new Tag(EnableMetadataConsole.PLATFORM_UOM, "平台值集管理"));
    }
}
