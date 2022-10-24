package org.o2.rule.engine.client.infra.rule;

import org.springframework.util.CollectionUtils;
import java.util.List;

/**
 * @author hongbo.xiang@hand-china.com 2020/10/27 10:46
 */
public class OperatorAnyGreater extends AbstractCollectionOperator {

    /**
     * 构造函数
     * @param aName 名称
     */
    public OperatorAnyGreater(String aName) {
        this.name = aName;
    }

    @Override
    public Object executeInner(Object[] list) throws Exception {
        validateLength(list);

        List<Object> source = (List<Object>) list[0];

        if (CollectionUtils.isEmpty(source)) {
            return false;
        }

        Object compare = list[1];

        boolean result = false;

        for (Object obj : source) {

            if (result) {
                return result;
            }

            if (obj.getClass().equals(compare.getClass()) && obj instanceof Comparable) {
                final Comparable comparableA = (Comparable) obj;
                final Comparable comparableB = (Comparable) compare;

                result = comparableA.compareTo(comparableB) > 0;
            }
        }

        return result;
    }

}
