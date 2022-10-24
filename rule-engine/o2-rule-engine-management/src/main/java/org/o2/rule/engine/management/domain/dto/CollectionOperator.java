package org.o2.rule.engine.management.domain.dto;

import com.google.common.collect.Maps;
import org.o2.rule.engine.management.infra.constants.RuleEngineConstants;
import java.util.Map;

/**
 * @author xiang.zhao@hand-chian.com 2022/10/24
 */
public class CollectionOperator {

    private String code;
    private String value;

    public CollectionOperator() {
    }

    public CollectionOperator(String code, String value) {
        this.code = code;
        this.value = value;
    }

    public static final CollectionOperator GREATER = new CollectionOperator(RuleEngineConstants.Operator.GREATER, RuleEngineConstants.Operator.GREATER_CODE);
    public static final CollectionOperator GREATER_OR_EQUAL = new CollectionOperator(RuleEngineConstants.Operator.GREATER_OR_EQUAL, RuleEngineConstants.Operator.GREATER_OR_EQUAL_CODE);
    public static final CollectionOperator LESS = new CollectionOperator(RuleEngineConstants.Operator.LESS, RuleEngineConstants.Operator.LESS_CODE);
    public static final CollectionOperator LESS_OR_EQUAL = new CollectionOperator(RuleEngineConstants.Operator.LESS_OR_EQUAL, RuleEngineConstants.Operator.LESS_OR_EQUAL_CODE);
    public static final CollectionOperator EQUAL = new CollectionOperator(RuleEngineConstants.Operator.EQUAL, RuleEngineConstants.Operator.EQUAL_CODE);

    public static final Map<String, String> MAP = Maps.newHashMapWithExpectedSize(5);

    static {
        MAP.put(RuleEngineConstants.Operator.GREATER, RuleEngineConstants.Operator.GREATER_CODE);
        MAP.put(RuleEngineConstants.Operator.GREATER_OR_EQUAL, RuleEngineConstants.Operator.GREATER_OR_EQUAL_CODE);
        MAP.put(RuleEngineConstants.Operator.LESS, RuleEngineConstants.Operator.LESS_CODE);
        MAP.put(RuleEngineConstants.Operator.LESS_OR_EQUAL, RuleEngineConstants.Operator.LESS_OR_EQUAL_CODE);
        MAP.put(RuleEngineConstants.Operator.EQUAL, RuleEngineConstants.Operator.EQUAL_CODE);
    }

    public static String translatorOperator(String code) {
        return MAP.get(code);
    }

}
