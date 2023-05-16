package org.o2.metadata.management.client;

import com.fasterxml.jackson.core.type.TypeReference;
import io.swagger.annotations.ApiOperation;
import org.hzero.core.util.ResponseUtils;
import org.o2.metadata.management.client.domain.co.IamUserCO;
import org.o2.metadata.management.client.domain.dto.IamUserQueryInnerDTO;
import org.o2.metadata.management.client.infra.feign.IamUserRemoteService;

import java.util.List;

/**
 * 用户信息客户端(平台层)
 *
 * @author chao.yang05@hand-china.com 2023-05-16
 */
public class IamUserSiteClient {

    private final IamUserRemoteService iamUserRemoteService;

    public IamUserSiteClient(IamUserRemoteService iamUserRemoteService) {
        this.iamUserRemoteService = iamUserRemoteService;
    }

    /**
     * 查询用户信息
     *
     * @param queryInner 查询条件
     * @return 用户信息list
     */
    @ApiOperation("查询用户信息（平台层，默认0租户）")
    public List<IamUserCO> listIamUserInfoOfSite(IamUserQueryInnerDTO queryInner) {
        return ResponseUtils.getResponse(iamUserRemoteService.listIamUserInfoOfSite(queryInner), new TypeReference<List<IamUserCO>>() {
        });
    }
}
