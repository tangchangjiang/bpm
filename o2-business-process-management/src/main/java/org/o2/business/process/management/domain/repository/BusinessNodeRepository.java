package org.o2.business.process.management.domain.repository;

import org.hzero.mybatis.base.BaseRepository;
import org.o2.business.process.management.api.dto.BusinessNodeQueryDTO;
import org.o2.business.process.management.api.vo.BusinessNodeVO;
import org.o2.business.process.management.domain.entity.BusinessNode;

import java.util.List;

/**
 * 业务流程节点表资源库
 *
 * @author zhilin.ren@hand-china.com 2022-08-10 14:31:01
 */
public interface BusinessNodeRepository extends BaseRepository<BusinessNode> {


    /**
     * 根据参数，查询业务节点列表
     * @param businessNodeQueryDTO 查询参数
     * @return 结果
     */
    List<BusinessNodeVO> listBusinessNode(BusinessNodeQueryDTO businessNodeQueryDTO);
}
