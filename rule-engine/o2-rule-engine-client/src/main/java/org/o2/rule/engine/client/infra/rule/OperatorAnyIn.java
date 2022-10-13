package org.o2.rule.engine.client.infra.rule;

import java.util.Collection;

/**
 * QL Express AnyIn
 *
 * @author wei.cai@hand-china.com 2020/4/28
 */
public class OperatorAnyIn extends AbstractCollectionOperator {

    /**
     * 构造函数
     * @param aName 名称
     */
    public OperatorAnyIn(String aName) {
        this.name = aName;
    }

    @Override
    public Object executeInner(Object[] list) throws Exception {
        validateLength(list);

        final Collection<?> firstCollection = getCollection(list[0]);
        final Collection<?> secondCollection = getCollection(list[1]);
        return firstCollection.stream().anyMatch(secondCollection::contains);
    }

}
