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
         * Rule Execute Exception
         */
        public static final String RULE_EXECUTE_EXCEPTION = "o2re.client.execute_exception";

        private ErrorMessage() {

        }

    }

}
