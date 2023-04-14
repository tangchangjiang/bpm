package org.o2.metadata.console.api.controller.v1;

import io.choerodon.core.domain.Page;
import io.choerodon.core.iam.ResourceLevel;
import io.choerodon.mybatis.pagehelper.PageHelper;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import io.choerodon.swagger.annotation.Permission;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.hzero.core.base.BaseConstants;
import org.hzero.core.base.BaseController;
import org.hzero.core.util.Results;
import org.o2.metadata.console.infra.config.MetadataManagementAutoConfiguration;
import org.o2.metadata.console.infra.entity.OnlineShop;
import org.o2.metadata.console.infra.repository.OnlineShopRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

/**
 * 网店信息 站点级
 *
 * @author chao.yang05@hand-china.com 2023-04-12
 */
@Slf4j
@RestController("onlineShopSiteController.v1")
@RequestMapping("/v1/online-shops")
@Api(tags = MetadataManagementAutoConfiguration.ONLINE_SHOP_SITE)
public class OnlineShopSiteController extends BaseController {

    private final OnlineShopRepository onlineShopRepository;

    public OnlineShopSiteController(OnlineShopRepository onlineShopRepository) {
        this.onlineShopRepository = onlineShopRepository;
    }

    @ApiOperation("查询所有网点列表（站点级）")
    @Permission(level = ResourceLevel.SITE)
    @GetMapping("/all")
    public ResponseEntity<Page<OnlineShop>> listAllShops(final OnlineShop onlineShop, @ApiIgnore PageRequest pageRequest) {
        // 站点级查询
        onlineShop.setSiteFlag(BaseConstants.Flag.YES);
        return Results.success(PageHelper.doPageAndSort(pageRequest, () -> onlineShopRepository.selectShop(onlineShop)));
    }
}
