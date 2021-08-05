package org.o2.metadata.console.infra.convertor;

import org.o2.metadata.console.api.dto.NeighboringRegionDTO;
import org.o2.metadata.console.infra.entity.NeighboringRegion;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * 临近省
 *
 * @author yipeng.zhu@hand-china.com 2021-08-05
 **/
public class NeighboringRegionConverter {
    private NeighboringRegionConverter() {

    }
    private static NeighboringRegion dtoToPoObject(NeighboringRegionDTO neighboringRegionDTO) {

        if (neighboringRegionDTO == null) {
            return null;
        }
        NeighboringRegion neighboringRegion = new NeighboringRegion();
        neighboringRegion.setNeighboringRegionId(neighboringRegionDTO.getNeighboringRegionId());
        neighboringRegion.setPosTypeCode(neighboringRegionDTO.getPosTypeCode());
        neighboringRegion.setSourceCountryCode(neighboringRegionDTO.getSourceCountryCode());
        neighboringRegion.setSourceRegionCode(neighboringRegionDTO.getSourceRegionCode());
        neighboringRegion.setTargetCountryCode(neighboringRegionDTO.getTargetCountryCode());
        neighboringRegion.setTargetRegionCode(neighboringRegionDTO.getTargetRegionCode());
        neighboringRegion.setSourceRegionName(neighboringRegionDTO.getSourceRegionName());
        neighboringRegion.setTargetRegionName(neighboringRegionDTO.getTargetRegionName());
        neighboringRegion.setPosTypeMeaning(neighboringRegionDTO.getPosTypeMeaning());
        neighboringRegion.setSourceCountryName(neighboringRegionDTO.getSourceCountryName());
        neighboringRegion.setTargetCountryName(neighboringRegionDTO.getTargetCountryName());
        neighboringRegion.setTenantId(neighboringRegionDTO.getTenantId());
        neighboringRegion.setObjectVersionNumber(neighboringRegionDTO.getObjectVersionNumber());
        return neighboringRegion;
    }

    public static List<NeighboringRegion> dtoToPoListObjects(List<NeighboringRegionDTO> list) {
        List<NeighboringRegion> neighboringRegions= new ArrayList<>();
        if (list.isEmpty()) {
            return neighboringRegions;
        }
        for (NeighboringRegionDTO dto : list) {
            neighboringRegions.add(dtoToPoObject(dto));
        }
        return neighboringRegions;
    }
}
