package org.o2.metadata.management.client;

import com.fasterxml.jackson.core.type.TypeReference;
import org.hzero.core.util.ResponseUtils;
import org.o2.metadata.management.client.domain.co.AddressMappingCO;
import org.o2.metadata.management.client.domain.co.NeighboringRegionCO;
import org.o2.metadata.management.client.domain.dto.AddressMappingQueryInnerDTO;
import org.o2.metadata.management.client.domain.dto.InsideAddressMappingDTO;
import org.o2.metadata.management.client.domain.dto.OutAddressMappingInnerDTO;
import org.o2.metadata.management.client.infra.feign.AddressMappingRemoteService;

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
    public List<AddressMappingCO> listAddressMappingByCode(InsideAddressMappingDTO queryInnerDTO, Long tenantId) {
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
