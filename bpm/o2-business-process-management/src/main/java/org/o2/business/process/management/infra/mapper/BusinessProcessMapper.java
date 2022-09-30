package org.o2.business.process.management.infra.mapper;

import io.choerodon.mybatis.common.BaseMapper;
import org.o2.business.process.management.api.dto.BusinessExportDTO;
import org.o2.business.process.management.api.vo.BusinessExportVO;
import org.o2.business.process.management.domain.entity.BusinessProcess;

import java.util.List;

/**
 * 业务流程定义表Mapper
 *
 * @author youlong.peng@hand-china.com 2022-08-10 14:23:57
 */
public interface BusinessProcessMapper extends BaseMapper<BusinessProcess> {

    /**
     * 业务流程导出
     * @param businessExportDTO
     * @return
     */
    List<BusinessExportVO> listBusinessForExport(BusinessExportDTO businessExportDTO);
}
