package org.o2.rule.engine.client.infra.rule;

import com.google.common.collect.Sets;
import com.ql.util.express.Operator;
import org.hzero.core.base.BaseConstants;
import org.o2.rule.engine.client.app.exception.RuleRuntimeException;
import org.o2.rule.engine.client.infra.constant.RuleClientConstants;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ConcurrentReferenceHashMap;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
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

    /**
     * 集合教过比较
     * @param list 参数
     * @param collectionOperate 集合比较符
     * @param operate 值比较符
     * @return 是否成功
     */
    protected boolean collectionCompare(Object[] list, String collectionOperate, String operate) {

        List<Object> source = (List<Object>) list[0];
        Object value = list[1];

        if (CollectionUtils.isEmpty(source)) {
            return false;
        }

        final Comparable comparableB = (Comparable) value;

        switch (collectionOperate) {
            case RuleClientConstants.CollectionOperator.ANY:
                return source.stream().anyMatch(s -> {
                    final Comparable comparableA = (Comparable) s;
                    return this.compareByOperate(comparableA, comparableB, operate);
                });
            case RuleClientConstants.CollectionOperator.ALL:
                return source.stream().allMatch(s -> {
                    final Comparable comparableA = (Comparable) s;
                    return this.compareByOperate(comparableA, comparableB, operate);
                });
            default:
                return false;
        }
    }

    /**
     * 比较操作
     *
     * @param source  源
     * @param target  目标
     * @param operate 操作
     * @return boolean
     */
    protected boolean compareByOperate(Comparable source, Comparable target, String operate) {
        switch (operate) {
            case RuleClientConstants.Operator.GREATER:
                return source.compareTo(target) > 0;
            case RuleClientConstants.Operator.LESS:
                return source.compareTo(target) < 0;
            case RuleClientConstants.Operator.EQUAL:
                return source.compareTo(target) == 0;
            case RuleClientConstants.Operator.GREATER_OR_EQUAL:
                return source.compareTo(target) >= 0;
            case RuleClientConstants.Operator.LESS_OR_EQUAL:
                return source.compareTo(target) <= 0;
            default:
                return false;
        }
    }

}
