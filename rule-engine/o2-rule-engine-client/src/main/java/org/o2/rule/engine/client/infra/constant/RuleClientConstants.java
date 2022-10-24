package org.o2.rule.engine.client.infra.constant;

/**
 * 规则客户端常量
 *
 * @author wei.cai@hand-china.com
 * @date 2022/10/13
 */
public class RuleClientConstants {

    private RuleClientConstants() {

    }

    /**
     * 错误信息
     */
    public static class ErrorMessage {
        /**
         * Context Is Null
         */
        public static final String RULE_CONTEXT_IS_NULL = "o2re.client.context_is_null";
        /**
         * Condition Is Null
         */
        public static final String RULE_CONDITION_IS_NULL = "o2re.client.condition_is_null";
        /**
         * Rule Entity Is Null
         */
        public static final String RULE_ENTITY_IS_NULL = "o2re.client.entity_is_null";
        /**
         * Rule Execute Exception
         */
        public static final String RULE_EXECUTE_EXCEPTION = "o2re.client.execute_exception";
        /**
         * Rule Param Error
         */
        public static final String RULE_PARAM_ERROR = "o2.rule.param_error";

        private ErrorMessage() {

        }

    }

    /**
     * 缓存名称
     */
    public static final class CacheName {

        /**
         * 规则信息缓存
         */
        public static final String O2RE_RULE = "O2RE_RULE";

        private CacheName() {

        }
    }

    /**
     * 集合比较操作符
     *
     */
    public static class CollectionOperator {
        /**
         * 任意
         */
        public static final String ANY = "any";
        /**
         * 全部
         */
        public static final String ALL = "all";
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
        /**
         * 小于
         */
        public static final String LESS = "<";
        /**
         * 等于
         */
        public static final String EQUAL = "==";
        /**
         * 大于等于
         */
        public static final String GREATER_OR_EQUAL = ">=";
        /**
         * 小于等于
         */
        public static final String LESS_OR_EQUAL = "<=";
    }

}
