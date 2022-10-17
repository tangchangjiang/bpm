package org.o2.rule.engine.management.infra.constants;

/**
 * @author xiang.zhao@hand-chian.com 2022/10/12
 */
public class RuleEngineConstants {

    /**
     * 操作符
     *
     */
    public static class Operator {
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

    /**
     * Condition组件编码
     */
    public static class ComponentCode {
        /**
         * 基础类型
         */
        public static final String BASIC = "BASIC";
        /**
         * 首单类型
         */
        public static final String FIRST_ORDER = "FIRST-ORDER";
        /**
         * spu数量
         */
        public static final String SPU_QTY_LIMIT = "SPU-QTY-LIMIT";
        /**
         * spu价格
         */
        public static final String SPU_PRICE_LIMIT = "SPU-PRICE-LIMIT";
    }

    /**
     * 基本参数值
     */
    public static class BasicParameter {
        /**
         * 规则参数编码
         */
        public static final String PARAMETER_PARAMETER_CODE = "parameterCode";
        /**
         * 规则参数操作符
         */
        public static final String PARAMETER_OPERATOR = "operator";
        /**
         * 规则参数值
         */
        public static final String PARAMETER_VALUE = "value";
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
        public static final String RULE_ENGINE_CODE = "RULE_ENGINE_CODE";
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
        public static final String BASE_SORE = "BASE_SORE";
    }
}
