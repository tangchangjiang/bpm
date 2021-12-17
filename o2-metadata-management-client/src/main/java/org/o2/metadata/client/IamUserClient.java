package org.o2.metadata.client;

import com.fasterxml.jackson.core.type.TypeReference;
import org.hzero.core.util.ResponseUtils;
import org.o2.metadata.client.domain.co.IamUserCO;
import org.o2.metadata.client.domain.dto.IamUserQueryInnerDTO;
import org.o2.metadata.client.infra.feign.IamUserRemoteService;

import java.util.List;

/**
 *
 * 用户信息客户端
 *
 * @author yipeng.zhu@hand-china.com 2021-11-01
 **/
public class IamUserClient {

    private final IamUserRemoteService iamUserRemoteService;

    public IamUserClient(IamUserRemoteService iamUserRemoteService) {
        this.iamUserRemoteService = iamUserRemoteService;
    }

    /**
     * 查询用户信息
     * @param tenantId 租户ID
     * @param queryInner 查询条件
     * @return list
     */
    public List<IamUserCO> listIamUser(Long tenantId, IamUserQueryInnerDTO queryInner) {
        return ResponseUtils.getResponse(iamUserRemoteService.listIamUser(tenantId, queryInner), new TypeReference<List<IamUserCO>>() {
        });
    }
}
