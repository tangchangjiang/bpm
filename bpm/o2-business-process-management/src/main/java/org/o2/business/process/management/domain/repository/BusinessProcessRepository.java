package org.o2.business.process.management.domain.repository;

import org.hzero.mybatis.base.BaseRepository;
import org.o2.business.process.management.api.dto.BusinessExportDTO;
import org.o2.business.process.management.api.dto.BusinessProcessQueryDTO;
import org.o2.business.process.management.api.vo.BusinessExportVO;
import org.o2.business.process.management.domain.entity.BusinessProcess;

import java.util.List;

/**
 * 业务流程定义表资源库
 *
 * @author youlong.peng@hand-china.com 2022-08-10 14:23:57
 */
public interface BusinessProcessRepository extends BaseRepository<BusinessProcess> {


    /**
     * 业务流程导出
     * @param businessExportDTO
     * @return
     */
    List<BusinessExportVO> listBusinessForExport(BusinessExportDTO businessExportDTO);

    /**
     * 条件查询业务流程
     *
     * @param queryDTO 查询条件
     * @return 业务流程
     */
    List<BusinessProcess> listBusinessProcessByCondition(BusinessProcessQueryDTO queryDTO);
}
