package org.o2.rule.engine.management.domain.dto;

/**
 * AND OR枚举
 *
 * @author xiang.zhao@hand-china.com 2022/10/13
 */
public enum AndOr {

    /**
     * 并且
     */
    AND(" && "),
    /**
     * 或者
     */
    OR(" || ");

    private final String value;

    private AndOr(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}