package org.o2.rule.engine.client.infra.rule;

import com.ql.util.express.Operator;
import org.apache.commons.lang3.ObjectUtils;
import org.o2.rule.engine.client.app.exception.RuleRuntimeException;
import org.o2.rule.engine.client.infra.constant.RuleClientConstants;

/**
 * @auther: changjiang.tang
 * @date: 2023/02/07/11:16
 * @since: V1.7.0
 */
public class OperatorNotBlank extends Operator {

    /**
     * 构造函数
     *
     * @param aName 名称
     */
    public OperatorNotBlank(String aName) {
        this.name = aName;
    }


    @Override
    public Object executeInner(Object[] list) throws Exception {
        if (list.length != 2) {
            throw new RuleRuntimeException(RuleClientConstants.ErrorMessage.RULE_PARAM_ERROR);
        }
        final Object param = list[0];
        return ObjectUtils.isNotEmpty(param);
    }
}
