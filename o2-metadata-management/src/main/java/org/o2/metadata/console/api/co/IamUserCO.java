package org.o2.metadata.console.api.co;

import lombok.Data;

/**
 *
 * 用户信息
 *
 * @author yipeng.zhu@hand-china.com 2021-11-01
 **/
@Data
public class IamUserCO {
    /**
     * 用户ID
     */
    private Long id;
    /**
     * 邮箱
     */
    private String email;
    /**
     * 登录名称
     */
    private String loginName;
    /**
     * 真实名称
     */
    private String realName;

    /**
     * 手机号
     */
    private String phone;
}
