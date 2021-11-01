package org.o2.metadata.console.app.service;

import org.o2.metadata.console.api.co.IamUserCO;
import org.o2.metadata.console.api.dto.IamUserQueryInnerDTO;

import java.util.List;

/**
 *
 * 用户信息
 *
 * @author yipeng.zhu@hand-china.com 2021-11-01
 **/
public interface IamUserService {


    /**
     * 批量查询用户信息
     * @param queryInner 查询条件
     * @return  list
     */
    List<IamUserCO> listIamUser(IamUserQueryInnerDTO queryInner);
}
