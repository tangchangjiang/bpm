package org.o2.metadata.management.client;

import com.fasterxml.jackson.core.type.TypeReference;
import io.choerodon.core.iam.ResourceLevel;
import io.choerodon.swagger.annotation.Permission;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.hzero.core.util.ResponseUtils;
import org.hzero.core.util.Results;
import org.o2.metadata.management.client.domain.co.AddressMappingCO;
import org.o2.metadata.management.client.domain.co.NeighboringRegionCO;
import org.o2.metadata.management.client.domain.dto.AddressMappingQueryInnerDTO;
import org.o2.metadata.management.client.domain.dto.OutAddressMappingInnerDTO;
import org.o2.metadata.management.client.infra.feign.AddressMappingRemoteService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.Map;

/**
 *
 * 地址
 *
 * @author yipeng.zhu@hand-china.com 2021-09-23
 **/
public class AddressClient {
    private final AddressMappingRemoteService addressMappingRemoteService;

    public AddressClient(AddressMappingRemoteService addressMappingRemoteService) {
        this.addressMappingRemoteService = addressMappingRemoteService;
    }

    /**
     * 批量查询地址匹配
     *
     * @param queryInnerDTO 地址匹配
     * @param tenantId   租户ID
     * @return  map key: externalName
     */
    public Map<String, AddressMappingCO> listAddressMappings(AddressMappingQueryInnerDTO queryInnerDTO, Long tenantId) {
        return ResponseUtils.getResponse(addressMappingRemoteService.listAllAddressMappings(queryInnerDTO, tenantId), new TypeReference<Map<String, AddressMappingCO>>() {
        });
    }
    /**
     * 批量查询地址匹配
     *
     * @param queryInnerDTO 地址匹配
     * @param tenantId   租户ID
     * @return  map key: externalName
     */
    public List<AddressMappingCO> listAddressMappingByCode(AddressMappingQueryInnerDTO queryInnerDTO, Long tenantId) {
        return ResponseUtils.getResponse(addressMappingRemoteService.listAddressMappingByCode(queryInnerDTO, tenantId), new TypeReference<List<AddressMappingCO>>() {
        });
    }

    /**
     * 查询临近省
     * @param tenantId 租户ID
     * @return LIST
     */
    public List<NeighboringRegionCO> listNeighboringRegions(Long tenantId) {
        return ResponseUtils.getResponse(addressMappingRemoteService.listNeighboringRegions(tenantId), new TypeReference<List<NeighboringRegionCO>>() {
        });
    }

    /**
     * 通过内部地址编码匹配外部地址编码
     * @param organizationId
     * @param queryInnerDTO
     * @return
     */
    public List<AddressMappingCO> listOutAddress(Long organizationId,List<OutAddressMappingInnerDTO> queryInnerDTO) {
        return ResponseUtils.getResponse(addressMappingRemoteService.listOutAddress(queryInnerDTO, organizationId),new TypeReference<List<AddressMappingCO>>(){});
    }
}
