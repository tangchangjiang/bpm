package org.o2.feignclient.metadata.infra.feign;

import org.o2.feignclient.metadata.domain.dto.StaticResourceQueryDTO;
import org.o2.feignclient.metadata.domain.dto.StaticResourceSaveDTO;
import org.o2.feignclient.metadata.infra.constants.O2Service;
import org.o2.feignclient.metadata.infra.feign.fallback.StaticResourceRemoteRepositoryImpl;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

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

    /**
     * 查询资源code&url映射
     *
     * @param staticResourceQueryDTO staticResourceQueryDTO
     * @return code&url映射关系
     */
    @PostMapping("/query-resource-url")
    ResponseEntity<String> queryResourceCodeUrlMap(@RequestBody StaticResourceQueryDTO staticResourceQueryDTO);


    /**
     * 保存静态资源文件
     *
     * @param staticResourceSaveDTO staticResourceSaveDTO
     * @return code&url映射关系
     */
    @PostMapping("/save")
    ResponseEntity<String> saveResource(@RequestBody StaticResourceSaveDTO staticResourceSaveDTO);

}

