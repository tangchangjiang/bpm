package org.o2.rule.engine.management.infra.constants;

/**
 * 规则引擎常量
 *
 * @author xiang.zhao@hand-chian.com 2022/10/12
 */
public class RuleEngineConstants {
    /**
     * 规则状态
     */
    public static class RuleStatus {
        /**
         * 启用
         */
        public static final String ENABLE = "ENABLE";
        /**
         * 未启用
         */
        public static final String DISABLE = "DISABLE";
        /**
         * 已失效
         */
        public static final String INVALID = "INVALID";
        /**
         * 修改待发布
         */
        public static final String MODIFIED = "MODIFIED";
        /**
         * 新建
         */
        public static final String NEW = "NEW";
        /**
         * 值集编码
         */
        public static final String CODE = "O2RE.RULE_STATUS";
    }

    /**
     * 操作符
     *
     */
    public static class CollectionOperator {
        /**
         * 全部包含
         */
        public static final String ALL_IN = "allIn";
        /**
         * 任一包含
         */
        public static final String ANY_IN = "anyIn";
        /**
         * 都不包含
         */
        public static final String NOT_IN = "notIn";
    }

    public static class StringOperator {
        /**
         * 不为空
         */
        public static final String NOT_BLANK = "NotBlank";
    }

    /**
     * 比较操作符
     *
     */
    public static class Operator {
        /**
         * 大于
         */
        public static final String GREATER = ">";
        public static final String GREATER_CODE = "Greater";
        /**
         * 小于
         */
        public static final String LESS = "<";
        public static final String LESS_CODE = "Less";
        /**
         * 等于
         */
        public static final String EQUAL = "==";
        public static final String EQUAL_CODE = "Equal";
        /**
         * 大于等于
         */
        public static final String GREATER_OR_EQUAL = ">=";
        public static final String GREATER_OR_EQUAL_CODE = "GreaterOrEqual";
        /**
         * 小于等于
         */
        public static final String LESS_OR_EQUAL = "<=";
        public static final String LESS_OR_EQUAL_CODE = "LessOrEqual";
    }

    /**
     * Condition组件编码
     */
    public static class ComponentCode {
        /**
         * 基础类型
         */
        public static final String BASIC = "BASIC";
        /**
         * 集合类型
         */
        public static final String COLLECTION = "COLLECTION";

        public static final String STRING = "STRING";
    }

    /**
     * 基本参数值
     */
    public static class BasicParameter {
        /**
         * 规则参数操作符
         */
        public static final String PARAMETER_OPERATOR = "operator";
        /**
         * 规则参数操作符
         */
        public static final String PARAMETER_C_OPERATOR = "collectionOperator";
        /**
         * 规则参数值
         */
        public static final String PARAMETER_VALUE = "value";
        /**
         * 规则参数属性
         */
        public static final String PARAMETER_PROPERTY = "property";
    }

    /**
     * 基本条件
     */
    public static class BasicCondition {
        public static final String TOTAL_PRICE = "order.totalPrice";
        public static final String TOTAL_QUANTITY = "order.totalQuantity";
        public static final String PRODUCT_SPU_CODES = "order.productSpuCodes";
        public static final String USER_GROUPS = "order.userGroups";
    }

    public static class SpuConditionParameter {
        public static final String PARAMETER_SPU_OPERATOR = "spuOperator";
        public static final String PARAMETER_SPU_VALUE = "spuValue";
        public static final String PARAMETER_VALUE_OPERATOR = "valueOperator";
        public static final String PARAMETER_VALUE = "value";
    }

    /**
     * 关联Entity类型
     */
    public static class RelEntityType {
        public static final String PLATFORM_PRODUCT_SPU = "PRODUCT_SPU";
        public static final String USER_GROUP = "USER_GROUP";
    }

    /**
     * 关联Entity类型
     */
    public static class GenerateTypeCode {
        public static final String RULE_CODE = "O2RE.RULE_CODE";
    }

    /**
     * 关联Entity类型
     */
    public static class ParameterType {
        public static final String BIG_DECIMAL = "BIG_DECIMAL";
        public static final String INTEGER = "INTEGER";
    }

    /**
     * 条件过滤编码
     */
    public static class FilterCode {
        public static final String BASE_STORE = "BASE_STORE";
    }

    /**
     * 错误编码
     */
    public static class ErrorCode {
        public static final String PARAM_VALID_ERROR = "o2rule.error.param_valid_error";
    }
}
