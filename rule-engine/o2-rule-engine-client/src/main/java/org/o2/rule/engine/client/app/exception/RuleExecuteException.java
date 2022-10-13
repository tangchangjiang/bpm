package org.o2.rule.engine.client.app.exception;

import lombok.Data;
import org.hzero.core.message.MessageAccessor;

/**
 * 规则执行异常
 *
 * @author wei.cai@hand-china.com
 * @date 2022/10/12
 */
@Data
public class RuleExecuteException extends Exception {

    /**
     * 错误编码
     */
    private final String code;

    /**
     * 构造函数
     * @param code 错误编码
     * @param objects 错误信息
     */
    public RuleExecuteException(String code, Object... objects) {
        super(MessageAccessor.getMessage(code, objects).getDesc());
        this.code = code;
    }

    /**
     * 构造函数
     * @param code 错误编码
     * @param throwable 错误异常
     * @param objects 错误对象
     */
    public RuleExecuteException(String code, Throwable throwable, Object... objects) {
        super(MessageAccessor.getMessage(code, objects).getDesc(), throwable);
        this.code = code;
    }

}
