package org.o2.feignclient.metadata.infra.feign;

import io.swagger.annotations.ApiOperation;
import org.o2.feignclient.metadata.domain.dto.StaticResourceQueryDTO;
import org.o2.feignclient.metadata.domain.dto.StaticResourceSaveDTO;
import org.o2.feignclient.metadata.infra.constants.O2Service;
import org.o2.feignclient.metadata.infra.feign.fallback.StaticResourceRemoteRepositoryImpl;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


import io.choerodon.core.iam.ResourceLevel;
import io.choerodon.swagger.annotation.Permission;

/**
 * 静态资源表 -远程调用资源库接口
 *
 * @author zhanpeng.jiang@hand-china.com
 * @date 2021/07/30 15:33
 */
@FeignClient(
        value = O2Service.Metadata.NAME,
        path = "/v1",
        fallback = StaticResourceRemoteRepositoryImpl.class
)
public interface StaticResourceRemoteRepository {

    @PostMapping("/query-resource-url")
    ResponseEntity<String> queryResourceCodeUrlMap(@RequestBody StaticResourceQueryDTO staticResourceQueryDTO);

    @ApiOperation(value = "查询静态资源文件code&url映射")
    @Permission(level = ResourceLevel.ORGANIZATION, permissionWithin = true)
    @PostMapping("/save")
    ResponseEntity<String> saveResource(@RequestBody StaticResourceSaveDTO staticResourceSaveDTO);

}

