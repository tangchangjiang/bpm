package org.o2.metadata.console.infra.lovadapter.repository;

import org.hzero.boot.platform.lov.dto.LovValueDTO;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * description:开放-集值
 * </p>
 *
 * @author wei.cai@hand-china.com 2019/12/25 12:26
 */
public interface PublicLovQueryRepository {

    /**
     * 根据编码以及租户ID查集值
     *
     * @param lovCode  值集编码
     * @param tenantId 租户ID
     * @return 编码值集
     */
    ResponseEntity<List<LovValueDTO>> queryLovInfo(String lovCode, Long tenantId);

    /**
     * 根据编码以及租户ID批量查集值
     *
     * @param queryMap 查询条件
     * @param tenantId 租户ID
     * @return 值集集合
     */
    ResponseEntity<Map<String, List<LovValueDTO>>> batchQueryLovInfo(Map<String, String> queryMap, Long tenantId);
}
