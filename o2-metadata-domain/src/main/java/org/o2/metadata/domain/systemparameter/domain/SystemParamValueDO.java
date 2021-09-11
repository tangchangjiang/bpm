package org.o2.metadata.domain.systemparameter.domain;



import java.util.Objects;

/**
 * 系统参数值
 *
 * @author hongyun.wang01@hand-china.com 2020-02-13
 */

public class SystemParamValueDO {
    /**
     * 表ID，主键
     */
    private Long valueId;

    /**
     * 关联参数表，o2ext_system_param.param_id
     */
    private Long paramId;
    /**
     * 值
     */
    private String paramValue;
    /**
     * 拓展字段1
     */
    private String param1;
    /**
     * 拓展字段2
     */
    private String param2;
    /**
     * 拓展字段3
     */
    private String param3;

    /**
     * 租户ID
     */
    private Long tenantId;

    private String paramKey;
    /**
     * 版本号
     */
    private String objectVersionNumber;

    public String getObjectVersionNumber() {
        return objectVersionNumber;
    }

    public void setObjectVersionNumber(String objectVersionNumber) {
        this.objectVersionNumber = objectVersionNumber;
    }

    public String getParamKey() {
        return paramKey;
    }

    public void setParamKey(String paramKey) {
        this.paramKey = paramKey;
    }

    public Long getValueId() {
        return valueId;
    }

    public void setValueId(Long valueId) {
        this.valueId = valueId;
    }

    public Long getParamId() {
        return paramId;
    }

    public void setParamId(Long paramId) {
        this.paramId = paramId;
    }

    public String getParamValue() {
        return paramValue;
    }

    public void setParamValue(String paramValue) {
        this.paramValue = paramValue;
    }

    public String getParam1() {
        return param1;
    }

    public void setParam1(String param1) {
        this.param1 = param1;
    }

    public String getParam2() {
        return param2;
    }

    public void setParam2(String param2) {
        this.param2 = param2;
    }

    public String getParam3() {
        return param3;
    }

    public void setParam3(String param3) {
        this.param3 = param3;
    }

    public Long getTenantId() {
        return tenantId;
    }

    public void setTenantId(Long tenantId) {
        this.tenantId = tenantId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {return true;}
        if (o == null || getClass() != o.getClass()){ return false;}
        SystemParamValueDO valueDO = (SystemParamValueDO) o;
        return Objects.equals(valueId, valueDO.valueId) &&
                Objects.equals(paramId, valueDO.paramId) &&
                Objects.equals(paramValue, valueDO.paramValue) &&
                Objects.equals(param1, valueDO.param1) &&
                Objects.equals(param2, valueDO.param2) &&
                Objects.equals(param3, valueDO.param3) &&
                Objects.equals(paramKey,valueDO.paramKey)&&
                Objects.equals(tenantId, valueDO.tenantId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(valueId, paramId, paramValue, param1, param2, param3, tenantId);
    }

    @Override
    public String toString() {
        return "SystemParamValueDO{" +
                "valueId=" + valueId +
                ", paramId=" + paramId +
                ", paramValue='" + paramValue + '\'' +
                ", param1='" + param1 + '\'' +
                ", param2='" + param2 + '\'' +
                ", param3='" + param3 + '\'' +
                ", tenantId=" + tenantId +
                '}';
    }
}
