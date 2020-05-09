package org.o2.metadata.console.config;

import org.o2.context.inventory.api.IInventoryContext;
import org.o2.context.inventory.config.InventoryContextConsumer;
import org.o2.context.metadata.api.ISysParameterContext;
import org.o2.context.metadata.api.IWarehouseContext;
import org.o2.context.metadata.config.MetadataContextConsumer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Component;
import springfox.documentation.service.Tag;
import springfox.documentation.spring.web.plugins.Docket;

/**
 * @author wenjun.deng01@hand-china.com 2019-01-29
 */
@Component
@ComponentScan({
        "org.o2.metadata.console.api",
        "org.o2.metadata.console.app",
        "org.o2.metadata.console.job"
})
public class EnableMetadataConsole {
    public static final String ADDRESS_MAPPING = "Address Mapping";
    public static final String CARRIER = "Carrier";
    public static final String CARRIER_DELIVERY_RANGE = "Carrier Delivery Range";
    public static final String CARRIER_MAPPING = "Carrier Mapping";
    public static final String COUNTRY = "Country";
    public static final String NEIGHBORING_REGION = "Neighboring Region";
    public static final String ONLINE_SHOP = "Online Shop";
    public static final String ONLINE_SHOP_INF_AUTH = "Online Shop API Auth";
    public static final String ONLINE_SHOP_WAREHOUSE_REL = "Online Shop Warehouse Relationship";
    public static final String WAREHOUSE = "Warehouse Management";
    public static final String POS_ADDRESS = "Pos Address";
    public static final String POS = "POS (Point Of Service)";
    public static final String POS_REL_CARRIER = "Pos Rel Carrier";
    public static final String REGION = "Region";
    public static final String SYS_PARAMETER_SETTING = "System Parameter Setting";
    public static final String FREIGHT_TEMPLATE = "Freight Template";
    public static final String FREIGHT_TEMPLATE_DETAIL = "Freight Template Detail";
    public static final String PLATFORM_UOM = "Platform Uom";
    public static final String CATALOG = "Catalog";
    public static final String REGION_REL_POS = "Region POS Relationship";

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
                .tags(new Tag(EnableMetadataConsole.ONLINE_SHOP_WAREHOUSE_REL, "网店关联仓库管理"))
                .tags(new Tag(EnableMetadataConsole.WAREHOUSE, "仓库管理"))
                .tags(new Tag(EnableMetadataConsole.POS_ADDRESS, "服务点地址管理"))
                .tags(new Tag(EnableMetadataConsole.POS, "服务点信息管理"))
                .tags(new Tag(EnableMetadataConsole.POS_REL_CARRIER, "服务点关联承运商管理"))
                .tags(new Tag(EnableMetadataConsole.REGION, "区域定义"))
                .tags(new Tag(EnableMetadataConsole.SYS_PARAMETER_SETTING, "系统参数配置管理"))
                .tags(new Tag(EnableMetadataConsole.FREIGHT_TEMPLATE, "运费模板管理"))
                .tags(new Tag(EnableMetadataConsole.FREIGHT_TEMPLATE_DETAIL, "运费模板明细管理"))
                .tags(new Tag(EnableMetadataConsole.PLATFORM_UOM, "平台值集管理"))
                .tags(new Tag(EnableMetadataConsole.REGION_REL_POS, "区域关联服务点配置"));
    }

    @Bean
    public ISysParameterContext sysParameterContext(final MetadataContextConsumer metadataContextConsumer) {
        metadataContextConsumer.sysParameterContextConsumer().init();
        return metadataContextConsumer.sysParameterContextConsumer().get();
    }

    @Bean
    public IWarehouseContext warehouseContext(final MetadataContextConsumer metadataContextConsumer) {
        metadataContextConsumer.warehouseContextConsumer().init();
        return metadataContextConsumer.warehouseContextConsumer().get();
    }

    @Bean
    public IInventoryContext inventoryContext(InventoryContextConsumer inventoryContextConsumer) {
        inventoryContextConsumer.inventoryContextConsumer().init();
        return inventoryContextConsumer.inventoryContextConsumer().get();
    }
}
