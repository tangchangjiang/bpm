package org.o2.business.process.management.app.service;

import org.o2.business.process.management.api.dto.BusinessExportDTO;
import org.o2.business.process.management.api.dto.BusinessProcessQueryDTO;
import org.o2.business.process.management.api.vo.BusinessExportVO;
import org.o2.business.process.management.domain.entity.BusinessProcess;

import java.util.List;


/**
 * 业务流程定义表应用服务
 *
 * @author youlong.peng@hand-china.com
 * @date 2022-08-10 14:23:57
 */
public interface BusinessProcessService {

    /**
     * 批量查询业务流程
     * @param queryDTO
     * @return
     */
    List<BusinessProcess> listBusinessProcess(BusinessProcessQueryDTO queryDTO);
    /**
     * 批量保存业务流程定义表
     *
     * @param businessProcessList 业务流程定义表对象列表
     * @return 业务流程定义表对象列表
     */
    List<BusinessProcess> batchSave(List<BusinessProcess> businessProcessList);


    /**
     * 保存业务流程定义表
     *
     * @param businessProcess 业务流程定义表对象
     * @return 业务流程定义表对象
     */
    BusinessProcess save(BusinessProcess businessProcess);

    /**
     * 导出接口查询
     * @param businessExportDTO
     * @return
     */
    List<BusinessExportVO> businessExport(BusinessExportDTO businessExportDTO);
}
