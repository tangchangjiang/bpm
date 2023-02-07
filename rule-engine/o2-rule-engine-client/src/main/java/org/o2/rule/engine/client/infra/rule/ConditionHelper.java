package org.o2.rule.engine.client.infra.rule;

import com.ql.util.express.ExpressRunner;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.BooleanUtils;
import org.o2.rule.engine.client.infra.rule.all.OperatorAllEqual;
import org.o2.rule.engine.client.infra.rule.all.OperatorAllGreater;
import org.o2.rule.engine.client.infra.rule.all.OperatorAllGreaterOrEqual;
import org.o2.rule.engine.client.infra.rule.all.OperatorAllLess;
import org.o2.rule.engine.client.infra.rule.all.OperatorAllLessOrEqual;
import org.o2.rule.engine.client.infra.rule.any.OperatorAnyEqual;
import org.o2.rule.engine.client.infra.rule.any.OperatorAnyGreater;
import org.o2.rule.engine.client.infra.rule.any.OperatorAnyGreaterOrEqual;
import org.o2.rule.engine.client.infra.rule.any.OperatorAnyLess;
import org.o2.rule.engine.client.infra.rule.any.OperatorAnyLessOrEqual;

/**
 * 条件帮助工具类
 *
 * @author wei.cai@hand-china.com
 * @date 2022/10/13
 */
@Slf4j
public class ConditionHelper {

    /**
     * QL规则引擎
     */
    public static final ExpressRunner EXPRESS_RUNNER = new ExpressRunner();

    private ConditionHelper() {

    }

    static {
        try {
            EXPRESS_RUNNER.addOperator("anyIn", new OperatorAnyIn("anyIn"));
            EXPRESS_RUNNER.addOperator("allIn", new OperatorAllIn("allIn"));
            EXPRESS_RUNNER.addOperator("notIn", new OperatorNotIn("notIn"));
            EXPRESS_RUNNER.addOperator("listGet", new OperatorListGet("listGet"));
            EXPRESS_RUNNER.addOperator("anyGreater", new OperatorAnyGreater("anyGreater"));
            EXPRESS_RUNNER.addOperator("anyGreaterOrEqual", new OperatorAnyGreaterOrEqual("anyGreaterOrEqual"));
            EXPRESS_RUNNER.addOperator("anyLess", new OperatorAnyLess("anyLess"));
            EXPRESS_RUNNER.addOperator("anyLessOrEqual", new OperatorAnyLessOrEqual("anyLessOrEqual"));
            EXPRESS_RUNNER.addOperator("anyEqual", new OperatorAnyEqual("anyEqual"));
            EXPRESS_RUNNER.addOperator("allGreater", new OperatorAllGreater("allGreater"));
            EXPRESS_RUNNER.addOperator("allGreaterOrEqual", new OperatorAllGreaterOrEqual("allGreaterOrEqual"));
            EXPRESS_RUNNER.addOperator("allLess", new OperatorAllLess("allLess"));
            EXPRESS_RUNNER.addOperator("allLessOrEqual", new OperatorAllLessOrEqual("allLessOrEqual"));
            EXPRESS_RUNNER.addOperator("allEqual", new OperatorAllEqual("allEqual"));
            EXPRESS_RUNNER.addOperator("notBlank", new OperatorNotBlank("notBlank"));
        } catch (Exception e) {
            log.error("EXPRESS_RUNNER addOperator error", e);
        }
    }

    /**
     * 判断是够满足条件
     *
     * @param expression 表达式
     * @param context    执行上下文
     * @throws Exception 异常
     * @return expression是否满足条件
     */
    public static boolean matchCondition(String expression,
                                         RuleExecuteContext<String, Object> context) throws Exception {
        if (log.isDebugEnabled()) {
            log.debug("Expression:[{}]", expression);
        }
        final Object r = EXPRESS_RUNNER.execute(expression, context, null, true, false);

        if (log.isDebugEnabled()) {
            log.debug("Result:[{}]", r);
        }
        return BooleanUtils.isTrue(cast(r));
    }

    @SuppressWarnings("unchecked")
    private static <T> T cast(Object object) {
        return (T) object;
    }

}
