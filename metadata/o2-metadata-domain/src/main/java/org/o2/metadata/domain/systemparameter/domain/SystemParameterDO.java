package org.o2.metadata.domain.systemparameter.domain;

import java.util.Set;

/**
 *
 * 系统参数DO
 *
 * @author yipeng.zhu@hand-china.com 2021-07-09
 **/
public class SystemParameterDO {
    /**
     * 表ID，主键
     */
    private Long paramId;
    /**
     * 编码
     */
    private String paramCode;
    /**
     * 参数名称
     */
    private String paramName;
    /**
     * 值集，KV（key-value） LIST(重复) SET（不重复
     */
    private String paramTypeCode;

    /**
     * 是否启用
     */
    private Integer activeFlag;
    /**
     * 备注说明
     */
    private String remark;
    /**
     * 默认值
     */
    private String defaultValue;
    /**
     * 租户ID
     */
    private Long tenantId;

    private Set<SystemParamValueDO> setSystemParamValue;

    public Long getParamId() {
        return paramId;
    }

    public void setParamId(Long paramId) {
        this.paramId = paramId;
    }

    public String getParamCode() {
        return paramCode;
    }

    public void setParamCode(String paramCode) {
        this.paramCode = paramCode;
    }

    public String getParamName() {
        return paramName;
    }

    public void setParamName(String paramName) {
        this.paramName = paramName;
    }

    public String getParamTypeCode() {
        return paramTypeCode;
    }

    public void setParamTypeCode(String paramTypeCode) {
        this.paramTypeCode = paramTypeCode;
    }

    public Integer getActiveFlag() {
        return activeFlag;
    }

    public void setActiveFlag(Integer activeFlag) {
        this.activeFlag = activeFlag;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getDefaultValue() {
        return defaultValue;
    }

    public void setDefaultValue(String defaultValue) {
        this.defaultValue = defaultValue;
    }

    public Long getTenantId() {
        return tenantId;
    }

    public void setTenantId(Long tenantId) {
        this.tenantId = tenantId;
    }

    public Set<SystemParamValueDO> getSetSystemParamValue() {
        return setSystemParamValue;
    }

    public void setSetSystemParamValue(Set<SystemParamValueDO> setSystemParamValue) {
        this.setSystemParamValue = setSystemParamValue;
    }

    @Override
    public String toString() {
        return "SystemParameterDO{" +
                "paramId=" + paramId +
                ", paramCode='" + paramCode + '\'' +
                ", paramName='" + paramName + '\'' +
                ", paramTypeCode='" + paramTypeCode + '\'' +
                ", activeFlag=" + activeFlag +
                ", remark='" + remark + '\'' +
                ", defaultValue='" + defaultValue + '\'' +
                ", tenantId=" + tenantId +
                '}';
    }
}
