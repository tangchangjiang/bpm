package org.o2.rule.engine.client.infra.rule;

import org.springframework.util.CollectionUtils;
import org.springframework.util.ReflectionUtils;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author hongbo.xiang@hand-china.com 2020/10/27 10:46
 */
public class OperatorListGet extends AbstractCollectionOperator {

    /**
     * 构造函数
     * @param aName 名称
     */
    public OperatorListGet(String aName) {
        this.name = aName;
    }

    @Override
    public Object executeInner(Object[] list) throws Exception {
        validateLength(list);

        List<Object> source = (List<Object>) list[0];

        if (CollectionUtils.isEmpty(source)) {
            return Collections.emptyList();
        }

        final String property = (String) list[1];

        final Class<?> aClass = source.get(0).getClass();
        final Field field = ReflectionUtils.findField(aClass, property);
        ReflectionUtils.makeAccessible(field);

        final List<Object> result = new ArrayList<>(source.size());

        for (Object obj : source) {
            result.add(ReflectionUtils.getField(field, obj));
        }

        return result;
    }

}
