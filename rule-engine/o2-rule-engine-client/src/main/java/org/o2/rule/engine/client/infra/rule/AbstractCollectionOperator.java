package org.o2.rule.engine.client.infra.rule;

import com.google.common.collect.Sets;
import com.ql.util.express.Operator;
import org.hzero.core.base.BaseConstants;
import org.o2.rule.engine.client.app.exception.RuleRuntimeException;
import org.o2.rule.engine.client.infra.constant.RuleClientConstants;
import org.springframework.util.ConcurrentReferenceHashMap;

import java.util.Arrays;
import java.util.Collection;
import java.util.Map;

/**
 * 抽象Collection操作类
 *
 * @author wei.cai@hand-china.com 2020/11/17
 */
public abstract class AbstractCollectionOperator extends Operator {
    private static final Integer PARAM_COUNT = 2;
    private static final int DEFAULT_INITIAL_CAPACITY = 200;
    protected static final Map<Object, Collection<?>> CACHE = new ConcurrentReferenceHashMap<>(DEFAULT_INITIAL_CAPACITY);

    /**
     * 获取集合
     * @param value 值
     * @return 返回集合
     */
    protected Collection<?> getCollection(Object value) {
        return CACHE.computeIfAbsent(value, key -> convertToCollection(value));
    }

    /**
     * 转化成List
     * @param value 结果
     * @return List结果
     */
    protected Collection<?> convertToCollection(Object value) {
        if (!validateValue(value)) {
            throw new RuleRuntimeException(RuleClientConstants.ErrorMessage.RULE_PARAM_ERROR);
        }
        if (value instanceof String) {
            return Sets.newHashSet(Arrays.asList(((String) value).split(BaseConstants.Symbol.COMMA)));
        }

        if (value.getClass().isArray()) {
            return Sets.newHashSet(Arrays.asList((Object[]) value));
        } else {
            return (Collection<?>) value;
        }
    }

    /**
     * 校验参数长度
     * @param list 集合
     */
    protected void validateLength(Object[] list) {
        if (list.length != PARAM_COUNT) {
            throw new RuleRuntimeException(RuleClientConstants.ErrorMessage.RULE_PARAM_ERROR);
        }
    }

    /**
     * 校验结果
     * @param value 结果
     * @return 是否成功
     */
    protected boolean validateValue(Object value) {
        return value != null && (value instanceof String || value.getClass().isArray() || value instanceof Collection);
    }

}
