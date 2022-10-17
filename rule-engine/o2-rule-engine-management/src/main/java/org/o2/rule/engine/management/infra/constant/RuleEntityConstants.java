package org.o2.rule.engine.management.infra.constant;

/**
 * 描述
 *
 * @author yuncheng.ma@hand-china.com
 * @since 2022/10/14 10:23
 */
public class RuleEntityConstants {
    public static final String RULE_ENTITY_RULE_CODE = "O2MD.RULE_ENTITY_CODE";

    private RuleEntityConstants(){}

    public static class ErrorCode {
        public static final String RULE_CODE_ERROR = "o2md.rule.entity.error.code_error";

        private ErrorCode(){}
    }

    public static class RedisKey {

        public static final String RULE_ENTITY = "o2re:rule_entity:{%d}";

        private RedisKey(){}
    }
}
