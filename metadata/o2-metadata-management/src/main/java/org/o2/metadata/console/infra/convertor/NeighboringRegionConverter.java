package org.o2.metadata.console.infra.convertor;

import org.o2.metadata.console.api.co.NeighboringRegionCO;
import org.o2.metadata.console.api.dto.NeighboringRegionQueryDTO;
import org.o2.metadata.console.infra.entity.NeighboringRegion;

import java.util.ArrayList;
import java.util.List;

/**
 * 临近省
 *
 * @author yipeng.zhu@hand-china.com 2021-08-05
 **/
public class NeighboringRegionConverter {
    private NeighboringRegionConverter() {

    }

    private static NeighboringRegion dtoToPoObject(NeighboringRegionQueryDTO neighboringRegionDTO) {

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

    public static List<NeighboringRegion> dtoToPoListObjects(List<NeighboringRegionQueryDTO> list) {
        List<NeighboringRegion> neighboringRegions = new ArrayList<>();
        if (list.isEmpty()) {
            return neighboringRegions;
        }
        for (NeighboringRegionQueryDTO dto : list) {
            neighboringRegions.add(dtoToPoObject(dto));
        }
        return neighboringRegions;
    }

    public static List<NeighboringRegionCO> poToCoListObjects(List<NeighboringRegion> list) {
        List<NeighboringRegionCO> neighboringRegions = new ArrayList<>();
        if (list.isEmpty()) {
            return neighboringRegions;
        }
        for (NeighboringRegion po : list) {
            neighboringRegions.add(poToCoObject(po));
        }
        return neighboringRegions;
    }

    /**
     * po-co
     *
     * @param neighboringRegion 临近省
     * @return lsit
     */
    private static NeighboringRegionCO poToCoObject(NeighboringRegion neighboringRegion) {

        if (neighboringRegion == null) {
            return null;
        }
        NeighboringRegionCO co = new NeighboringRegionCO();
        co.setPosTypeCode(neighboringRegion.getPosTypeCode());
        co.setSourceCountryCode(neighboringRegion.getSourceCountryCode());
        co.setSourceRegionCode(neighboringRegion.getSourceRegionCode());
        co.setTargetCountryCode(neighboringRegion.getTargetCountryCode());
        co.setTargetRegionCode(neighboringRegion.getTargetRegionCode());
        co.setSourceRegionName(neighboringRegion.getSourceRegionName());
        co.setTargetRegionName(neighboringRegion.getTargetRegionName());
        co.setSourceCountryName(neighboringRegion.getSourceCountryName());
        co.setTargetCountryName(neighboringRegion.getTargetCountryName());
        co.setTenantId(neighboringRegion.getTenantId());
        return co;
    }
}
