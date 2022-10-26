package org.o2.rule.engine.client.infra.rule.any;

import org.o2.rule.engine.client.infra.constant.RuleClientConstants;
import org.o2.rule.engine.client.infra.rule.AbstractCollectionOperator;

/**
 * @author xiang.zhao@hand-china.com 2022/10/27
 */
public class OperatorAnyGreaterOrEqual extends AbstractCollectionOperator {

    /**
     * 构造函数
     * @param aName 名称
     */
    public OperatorAnyGreaterOrEqual(String aName) {
        this.name = aName;
    }

    @Override
    public Object executeInner(Object[] list) throws Exception {
        validateLength(list);

        return collectionCompare(list, RuleClientConstants.CollectionOperator.ANY, RuleClientConstants.Operator.GREATER_OR_EQUAL);
    }

}
