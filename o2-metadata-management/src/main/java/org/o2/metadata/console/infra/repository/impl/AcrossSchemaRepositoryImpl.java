package org.o2.metadata.console.infra.repository.impl;

import org.o2.metadata.console.infra.mapper.AcrossSchemaMapper;
import org.o2.metadata.console.infra.repository.AcrossSchemaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author yong.nie@hand-china.com
 * @date 2020/5/8 15:42
 **/

@Repository
public class AcrossSchemaRepositoryImpl implements AcrossSchemaRepository {

    private AcrossSchemaMapper acrossSchemaMapper;

    @Autowired
    public AcrossSchemaRepositoryImpl(AcrossSchemaMapper acrossSchemaMapper) {
        this.acrossSchemaMapper = acrossSchemaMapper;
    }

    @Override
    public List<String> selectSkuByWarehouse(String warehouseCode, Long tenantId) {
        return acrossSchemaMapper.selectSkuByWarehouse(warehouseCode, tenantId);
    }

}
