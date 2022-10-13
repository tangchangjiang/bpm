package org.o2.rule.engine.client.infra.rule;

import java.util.Collection;

/**
 * @author hongbo.xiang@hand-china.com 2020/10/27 10:46
 */
public class OperatorAllIn extends AbstractCollectionOperator {

    /**
     * 构造函数
     * @param aName 名称
     */
    public OperatorAllIn(String aName) {
        this.name = aName;
    }

    @Override
    public Object executeInner(Object[] list) throws Exception {
        validateLength(list);

        final Collection<?> firstCollection = getCollection(list[0]);
        final Collection<?> secondCollection = getCollection(list[1]);
        return firstCollection.containsAll(secondCollection);
    }

}
