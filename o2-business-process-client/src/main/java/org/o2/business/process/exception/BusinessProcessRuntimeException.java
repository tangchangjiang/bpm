package org.o2.business.process.exception;

/**
 * 流水线执行异常类
 *
 * @author mark.bao@hand-china.com 2018/12/21
 */
public class BusinessProcessRuntimeException extends RuntimeException {

    public BusinessProcessRuntimeException(final String code, final Object... parameters) {
        super(String.format(code, parameters));
    }

    public BusinessProcessRuntimeException(final String code, final Throwable cause, final Object... parameters) {
        super(String.format(code, parameters), cause);
    }
}
