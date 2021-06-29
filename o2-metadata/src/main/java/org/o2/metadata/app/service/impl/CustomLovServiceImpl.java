package org.o2.metadata.app.service.impl;

import io.choerodon.core.exception.CommonException;
import lombok.extern.slf4j.Slf4j;
import org.hzero.boot.platform.lov.dto.LovValueDTO;
import org.hzero.boot.platform.lov.feign.LovFeignClient;
import org.hzero.core.util.Results;
import org.o2.metadata.app.service.CustomLovService;
import org.o2.metadata.infra.constants.BasicDataConstants;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * <p>
 * description：集值
 * </p>
 *
 * @author wei.cai@hand-china.com 2019/12/25 12:26
 */
@Service
@Slf4j
public class CustomLovServiceImpl implements CustomLovService {

    private LovFeignClient lovFeignClient;

    public CustomLovServiceImpl(LovFeignClient lovFeignClient) {
        this.lovFeignClient = lovFeignClient;
    }

    @Value("${o2.metadata.public.lov:O2MD.PUBLIC_LOV}")
    private String publicLovMetaData;

    @Override
    public ResponseEntity<List<LovValueDTO>> queryLovInfo(String lovCode, Long tenantId) {
        return Results.success(queryLovValues(lovCode, tenantId));
    }

    @Override
    public ResponseEntity<Map<String, List<LovValueDTO>>> batchQueryLovInfo(Map<String, String> queryMap, Long tenantId) {
        if (queryMap == null) {
            return Results.success(Collections.emptyMap());
        } else {
            queryMap.remove("tenantId");
            queryMap.remove("lang");
            final Map<String, List<LovValueDTO>> result = new HashMap<>(queryMap.size());
            queryMap.forEach((key, value) ->
                result.put(key, this.queryLovValues(value, tenantId))
            );
            return Results.success(result);
        }
    }


    /**
     * 查询值集值
     * @param lovCode 值集编码
     * @param tenantId 租户ID
     * @return 值集值
     */
    private List<LovValueDTO> queryLovValues(String lovCode, Long tenantId) {
        final List<LovValueDTO> lovLists = lovFeignClient.queryLovValue(publicLovMetaData, tenantId);
        if (log.isDebugEnabled()) {
            log.debug("LovList Size For:[{}], SIZE:[{}]", publicLovMetaData, lovLists.size());
        }
        final Optional<LovValueDTO> lovValueDTO = lovLists.stream().filter(e -> lovCode.equals(e.getValue())).findAny();
        if (!lovValueDTO.isPresent()) {
            log.error("Please Check Config : [{}], Lov Code:[{}]", publicLovMetaData, lovCode);
            throw new CommonException(BasicDataConstants.ErrorCode.BASIC_DATA_LOV_PERMISSION_NOT_PASS);
        }
        final List<LovValueDTO> lovValues = lovFeignClient.queryLovValue(lovCode, tenantId);
        if (log.isDebugEnabled()) {
            log.debug("LovCode :[{}],Values:[{}]", lovCode, lovValues);
        }
        return lovValues;
    }
}
