package org.o2.metadata.console.app.service.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.hzero.core.base.BaseConstants;
import org.o2.core.helper.JsonHelper;
import org.o2.metadata.console.api.co.IamUserCO;
import org.o2.metadata.console.api.dto.IamUserQueryInnerDTO;
import org.o2.metadata.console.app.service.IamUserService;
import org.o2.metadata.console.infra.constant.O2LovConstants;
import org.o2.metadata.console.infra.lovadapter.repository.HzeroLovQueryRepository;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * 用户信息
 *
 * @author yipeng.zhu@hand-china.com 2021-11-01
 **/
@Service
@Slf4j
public class IamUserServiceImpl implements IamUserService {
   private final HzeroLovQueryRepository hzeroLovQueryRepository;
   private final ObjectMapper objectMapper;

    public IamUserServiceImpl(HzeroLovQueryRepository hzeroLovQueryRepository, ObjectMapper objectMapper) {
        this.hzeroLovQueryRepository = hzeroLovQueryRepository;
        this.objectMapper = objectMapper;
    }

    @Override
    public List<IamUserCO> listIamUser(IamUserQueryInnerDTO queryInner) {
        Map<String, String> queryLovValueMap = new HashMap<>(4);
        queryLovValueMap.put(O2LovConstants.IamUserLov.idList, StringUtils.join(queryInner.getIdList(), BaseConstants.Symbol.COMMA));
        queryLovValueMap.put(O2LovConstants.IamUserLov.REAL_NAME, queryInner.getRealName());
        queryLovValueMap.put(O2LovConstants.IamUserLov.TENANT_ID, String.valueOf(queryInner.getTenantId()));
        queryLovValueMap.put(O2LovConstants.IamUserLov.OPENID_LIST, StringUtils.join(queryInner.getOpenIdList(), BaseConstants.Symbol.COMMA));
        return this.getUserInfo(queryLovValueMap, queryInner.getTenantId());
    }

    @Override
    public List<IamUserCO> listIamUserInfo(IamUserQueryInnerDTO queryInner) {
        Map<String, String> queryLovValueMap = new HashMap<>(4);
        queryLovValueMap.put(O2LovConstants.IamUserLov.idList, StringUtils.join(queryInner.getIds(), BaseConstants.Symbol.COMMA));
        queryLovValueMap.put(O2LovConstants.IamUserLov.REAL_NAME, queryInner.getRealName());
        queryLovValueMap.put(O2LovConstants.IamUserLov.OPENID_LIST, StringUtils.join(queryInner.getOpenIdList(), BaseConstants.Symbol.COMMA));
        if (!BaseConstants.Flag.YES.equals(queryInner.getSiteFlag())) {
            queryLovValueMap.put(O2LovConstants.IamUserLov.TENANT_ID, String.valueOf(queryInner.getTenantId()));
            return this.getUserInfoForSite(queryLovValueMap, BaseConstants.DEFAULT_TENANT_ID);
        }
        return this.getUserInfo(queryLovValueMap, queryInner.getTenantId());
    }

    private List<IamUserCO> getUserInfo(Map<String, String> queryLovValueMap, Long tenantId) {
        List<Map<String, Object>> result = hzeroLovQueryRepository.queryLovValueMeaning(tenantId, O2LovConstants.IamUserLov.IAM_USER_LOV_CODE, queryLovValueMap);
        List<IamUserCO> list = null;
        try {
            list = this.objectMapper.readValue(JsonHelper.objectToString(result), new TypeReference<List<IamUserCO>>() {
            });
        } catch (Exception e) {
            log.error("user translation data error.");
        }
        return list;
    }

    private List<IamUserCO> getUserInfoForSite(Map<String, String> queryLovValueMap, Long tenantId) {
        List<Map<String, Object>> result = hzeroLovQueryRepository.queryLovValueMeaning(tenantId, O2LovConstants.IamUserLov.IAM_USER_SITE_LOV_CODE, queryLovValueMap);
        List<IamUserCO> list = null;
        try {
            list = this.objectMapper.readValue(JsonHelper.objectToString(result), new TypeReference<List<IamUserCO>>() {
            });
        } catch (Exception e) {
            log.error("user translation data error.");
        }
        return list;
    }
}
