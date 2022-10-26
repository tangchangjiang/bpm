package org.o2.rule.engine.client.infra.rule;

import com.ql.util.express.DefaultContext;
import org.o2.rule.engine.client.app.exception.RuleRuntimeException;
import org.o2.rule.engine.client.infra.constant.RuleClientConstants;
import java.util.Objects;

/**
 * 客制化上下文
 *
 * @param <K> 泛型K
 * @param <V> 泛型V
 * @author wei.cai@hand-china.com 2020/5/12
 */
public class RuleExecuteContext<K, V> extends DefaultContext<K, V> {

    @Override
    public V get(Object key) {
        final V v = super.get(key);
        if (Objects.isNull(v)) {
            throw new RuleRuntimeException(RuleClientConstants.ErrorMessage.RULE_CONTEXT_IS_NULL);
        }
        return v;
    }
}
