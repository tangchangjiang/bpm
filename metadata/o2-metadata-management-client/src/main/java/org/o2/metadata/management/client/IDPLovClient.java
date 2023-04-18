package org.o2.metadata.management.client;

import com.fasterxml.jackson.core.type.TypeReference;
import org.hzero.core.util.ResponseUtils;
import org.o2.metadata.management.client.domain.co.LovValueCO;
import org.o2.metadata.management.client.domain.dto.LovQueryInnerDTO;
import org.o2.metadata.management.client.infra.feign.LovAdapterRemoteService;

import java.util.List;
import java.util.Map;

/**
 *
 * 独立值集查询
 *
 * @author yipeng.zhu@hand-china.com 2021-09-22
 **/
public class IDPLovClient {
    private LovAdapterRemoteService lovAdapterRemoteService;

    public IDPLovClient(LovAdapterRemoteService lovAdapterRemoteService) {
        this.lovAdapterRemoteService = lovAdapterRemoteService;
    }

    /**
     * 查询值集中指定值的 描述信息（meaning）
     *
     * @param tenantId 租户id
     * @param lovCode  值集code
     * @param lovValue 值集value
     * @return String
     */
    public String queryLovValueMeaning(Long tenantId, String lovCode, String lovValue) {
        return ResponseUtils.getResponse(lovAdapterRemoteService.queryLovValueMeaning(tenantId, lovCode, lovValue), String.class);
    }

    /**
     * 独立值集
     *
     * @param lovCode  值集code
     * @param tenantId 租户id
     * @return List<LovValueDTO>
     */
    public List<LovValueCO> queryLovValue(Long tenantId, String lovCode) {
        return ResponseUtils.getResponse(lovAdapterRemoteService.queryLovValue(tenantId, lovCode), new TypeReference<List<LovValueCO>>() {
        });
    }

    /**
     * 独立值集-多租户
     *
     * @param lovCodeMap 值集code map
     * @return map tenantId:LovValueCO(List)
     */
    public Map<Long, List<LovValueCO>> queryLovValueBatchTenant(Map<Long, String> lovCodeMap) {
        return ResponseUtils.getResponse(lovAdapterRemoteService.queryLovValueBatchTenant(lovCodeMap), new TypeReference<Map<Long, List<LovValueCO>>>() {
        });
    }

    /**
     * 独立查询值集详细信息-批量
     *
     * @param tenantId         租户ID
     * @param lovQueryInnerDTO 值集内部查询DTO
     * @return 值集
     */
    public Map<String, List<LovValueCO>> batchQueryLovValueByLang(Long tenantId, LovQueryInnerDTO lovQueryInnerDTO) {
        return ResponseUtils.getResponse(lovAdapterRemoteService.batchQueryLovValueByLang(tenantId, lovQueryInnerDTO), new TypeReference<Map<String, List<LovValueCO>>>() {
        });
    }
}
