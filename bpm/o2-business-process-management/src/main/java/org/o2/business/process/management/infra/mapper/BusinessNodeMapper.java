package org.o2.business.process.management.infra.mapper;

import io.choerodon.mybatis.common.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.o2.business.process.management.api.dto.BusinessNodeQueryDTO;
import org.o2.business.process.management.api.vo.BusinessNodeExportVO;
import org.o2.business.process.management.api.vo.BusinessNodeVO;
import org.o2.business.process.management.domain.entity.BusinessNode;

import java.util.Collection;
import java.util.List;

/**
 * 业务流程节点表Mapper
 *
 * @author zhilin.ren@hand-china.com 2022-08-10 14:31:01
 */
public interface BusinessNodeMapper extends BaseMapper<BusinessNode> {


    /**
     * 根据参数，查询业务节点列表
     * @param businessNodeQueryDTO 查询参数
     * @return 结果
     */
    List<BusinessNodeVO> listBusinessNode(BusinessNodeQueryDTO businessNodeQueryDTO);


    /**
     * 查询业务节点与节点参数信息
     * @param beanIds
     * @param tenantId
     * @return
     */
    List<BusinessNodeExportVO> listNodeForExport(@Param("beanIds") Collection<String> beanIds, @Param("tenantId") Long tenantId);
}
