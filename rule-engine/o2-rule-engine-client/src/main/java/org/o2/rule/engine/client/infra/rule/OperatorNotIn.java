package org.o2.rule.engine.client.infra.rule;

import java.util.Collection;
import java.util.Objects;

/**
 * QL Express Not
 *
 * @author wei.cai@hand-china.com 2020/4/28
 */
public class OperatorNotIn extends AbstractCollectionOperator {

    /**
     * 构造器
     * @param aName 名称
     */
    public OperatorNotIn(String aName) {
        this.name = aName;
    }

    @Override
    public Object executeInner(Object[] list) throws Exception {
        validateLength(list);
        final Collection<?> firstList = getCollection(list[0]);
        final Collection<?> secondList = getCollection(list[1]);
        return firstList.isEmpty() || firstList.stream().filter(Objects::nonNull).anyMatch(e -> !secondList.contains(e));
    }

}
