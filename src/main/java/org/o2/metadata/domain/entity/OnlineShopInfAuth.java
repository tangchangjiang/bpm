package org.o2.metadata.domain.entity;

import io.choerodon.mybatis.annotation.ModifyAudit;
import io.choerodon.mybatis.annotation.VersionAudit;
import io.choerodon.mybatis.domain.AuditDomain;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 网店接口表
 *
 * @author tingting.wang@hand-china.com 2019-3-25
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel("网店接口表")
@VersionAudit
@ModifyAudit
@Table(name = "o2md_online_shop_inf_auth")
public class OnlineShopInfAuth extends AuditDomain {

    public static final String FIELD_ONLINE_SHOP_INF_AUTH_ID = "onlineShopInfAuthId";
    public static final String FIELD_ONLINE_SHOP_ID = "onlineShopId";
    public static final String FIELD_ONLINE_SHOP_CODE = "onlineShopCode";
    public static final String FIELD_APP_URL = "appUrl";
    public static final String FIELD_APP_KEY = "appKey";
    public static final String FIELD_APP_SECRET = "appSecret";
    public static final String FIELD_SESSION_KEY = "sessionKey";
    public static final String FIELD_ACCESS_TOKEN = "accessToken";
    public static final String FIELD_APP_ACCOUNT = "appAccount";

    //
    // 业务方法(按public protected private顺序排列)
    // ------------------------------------------------------------------------------

    //
    // 数据库字段
    // ------------------------------------------------------------------------------

    @ApiModelProperty("表ID，主键")
    @Id
    @GeneratedValue
    private Long onlineShopInfAuthId;

    @ApiModelProperty(value = "网店主键")
    private Long onlineShopId;

    @ApiModelProperty(value = "网店编码")
    private String onlineShopCode;

    @ApiModelProperty(value = "接口地址")
    private String appUrl;

    @ApiModelProperty(value = "app_key")
    private String appKey;

    @ApiModelProperty(value = "app_secret")
    private String appSecret;

    @ApiModelProperty(value = "session_key")
    private String sessionKey;

    @ApiModelProperty(value = "access_token")
    private String accessToken;

    @ApiModelProperty(value = "商家编码")
    private String appAccount;

    @ApiModelProperty(value = "租户ID")
    private Long tenantId;

    //
    // 非数据库字段
    // ------------------------------------------------------------------------------

}
