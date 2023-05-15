package org.o2.business.process.management.infra.repository.impl;

import org.hzero.mybatis.base.impl.BaseRepositoryImpl;
import org.o2.business.process.management.api.dto.BusinessExportDTO;
import org.o2.business.process.management.api.dto.BusinessProcessQueryDTO;
import org.o2.business.process.management.api.vo.BusinessExportVO;
import org.o2.business.process.management.domain.entity.BusinessProcess;
import org.o2.business.process.management.domain.repository.BusinessProcessRepository;
import org.o2.business.process.management.infra.mapper.BusinessProcessMapper;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 业务流程定义表 资源库实现
 *
 * @author youlong.peng@hand-china.com 2022-08-10 14:23:57
 */
@Component
public class BusinessProcessRepositoryImpl extends BaseRepositoryImpl<BusinessProcess> implements BusinessProcessRepository {

    private final BusinessProcessMapper businessProcessMapper;

    public BusinessProcessRepositoryImpl(BusinessProcessMapper businessProcessMapper) {
        this.businessProcessMapper = businessProcessMapper;
    }

    @Override
    public List<BusinessExportVO> listBusinessForExport(BusinessExportDTO businessExportDTO) {
        return businessProcessMapper.listBusinessForExport(businessExportDTO);
    }

    @Override
    public List<BusinessProcess> listBusinessProcessByCondition(BusinessProcessQueryDTO queryDTO) {
        return businessProcessMapper.listBusinessProcessByCondition(queryDTO);
    }
}
