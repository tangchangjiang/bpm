package org.o2.rule.engine.client.app.exception;

/**
 * 规则执行异常
 *
 * @author wei.cai@hand-china.com
 * @date 2022/10/12
 */
public class RuleExecuteException extends Exception {

    /**
     * 构造函数
     * @param message 错误消息
     */
    public RuleExecuteException(String message) {
        super(message);
    }

}
