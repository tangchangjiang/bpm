package org.o2.rule.engine.client.infra.rule;

import com.ql.util.express.ExpressRunner;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.BooleanUtils;

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
