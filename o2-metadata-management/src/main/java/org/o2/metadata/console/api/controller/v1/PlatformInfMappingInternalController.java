package org.o2.metadata.console.api.controller.v1;

import io.choerodon.core.iam.ResourceLevel;
import io.choerodon.swagger.annotation.Permission;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.hzero.boot.platform.lov.annotation.ProcessLovValue;
import org.hzero.core.base.BaseConstants;
import org.hzero.core.base.BaseController;
import org.hzero.core.util.Results;
import org.o2.metadata.console.api.dto.PlatformInfMappingDTO;
import org.o2.metadata.console.api.vo.PlatformInfMappingVO;
import org.o2.metadata.console.config.MetadataManagementAutoConfiguration;
import org.o2.metadata.console.infra.convertor.PlatformInfMappingConvertor;
import org.o2.metadata.console.infra.entity.PlatformInfMapping;
import org.o2.metadata.console.infra.repository.PlatformInfMappingRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * description 平台信息匹配内部调用
 *
 * @author zhilin.ren@hand-china.com 2021/08/02 21:19
 */
@Api(tags= MetadataManagementAutoConfiguration.PLATFORM_INF_MAPPING)
@RestController("platformInfMappingControllerInternal.v1")
@RequestMapping("/v1/{organizationId}/platform-inf-mappings-internal")
@RequiredArgsConstructor
public class PlatformInfMappingInternalController extends BaseController {
    private final PlatformInfMappingRepository platformInfMappingRepository;

    
    @ApiOperation(value = "查询平台信息匹配结果")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PostMapping
    @ProcessLovValue(targetField = BaseConstants.FIELD_BODY)
    public ResponseEntity<List<PlatformInfMappingVO>> getPlatformInfMapping(@PathVariable(value = "organizationId") Long organizationId,
                                                         @RequestBody List<PlatformInfMappingDTO> platformInfMapping) {

        List<PlatformInfMappingVO> mappingVOList = new ArrayList<>();
        for(PlatformInfMappingDTO platformInfMappingDTO : platformInfMapping) {
            platformInfMappingDTO.setTenantId(organizationId);
            validObject(platformInfMappingDTO);
            PlatformInfMapping mappingResult = platformInfMappingRepository.selectOneMapping(platformInfMappingDTO);
            PlatformInfMappingVO mappingVO = PlatformInfMappingConvertor.toPlatformInfMappingVO(mappingResult);
            mappingVOList.add(mappingVO);
        }

        return Results.success(mappingVOList);
    }


}
