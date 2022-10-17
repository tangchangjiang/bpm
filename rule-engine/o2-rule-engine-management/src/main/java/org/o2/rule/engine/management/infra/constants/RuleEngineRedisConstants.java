package org.o2.rule.engine.management.infra.constants;

/**
 * 规则引擎Redis相关常量
 *
 * @author xiang.zhao@hand-chian.com 2022/10/12
 */
public class RuleEngineRedisConstants {

    /**
     * Redis key
     *
     */
    public static class RedisKey {
        /**
         * 规则key Hash
         * key=ruleCode; value=rule
         */
        public static final String RULE_KEY = "o2md:rule:{%d}:info";
        /**
         * 规则实体最后更新时间
         */
        public static final String RULE_ENTITY_UPDATE_TIME_KEY = "o2md:rule:{%d}:entity";

        public static String getRuleKey(Long tenantId) {
            return String.format(RULE_KEY, tenantId);
        }

        public static String getRuleEntityUpdateTimeKey(Long tenantId) {
            return String.format(RULE_ENTITY_UPDATE_TIME_KEY, tenantId);
        }
    }
}
