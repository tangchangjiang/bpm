package org.o2.business.process.management.infra.repository.impl;

import lombok.RequiredArgsConstructor;
import org.hzero.mybatis.base.impl.BaseRepositoryImpl;
import org.o2.business.process.management.api.dto.BusinessNodeQueryDTO;
import org.o2.business.process.management.api.vo.BusinessNodeExportVO;
import org.o2.business.process.management.api.vo.BusinessNodeVO;
import org.o2.business.process.management.domain.entity.BusinessNode;
import org.o2.business.process.management.domain.repository.BusinessNodeRepository;
import org.o2.business.process.management.infra.mapper.BusinessNodeMapper;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;

/**
 * 业务流程节点表 资源库实现
 *
 * @author zhilin.ren@hand-china.com 2022-08-10 14:31:01
 */
@Component
@RequiredArgsConstructor
public class BusinessNodeRepositoryImpl extends BaseRepositoryImpl<BusinessNode> implements BusinessNodeRepository {

    private final BusinessNodeMapper businessNodeMapper;

    @Override
    public List<BusinessNodeVO> listBusinessNode(BusinessNodeQueryDTO businessNodeQueryDTO) {
        return businessNodeMapper.listBusinessNode(businessNodeQueryDTO);
    }

    @Override
    public List<BusinessNodeExportVO> listNodeForExport(Collection<String> beanIds, Long tenantId) {
        return businessNodeMapper.listNodeForExport(beanIds, tenantId);
    }
}
