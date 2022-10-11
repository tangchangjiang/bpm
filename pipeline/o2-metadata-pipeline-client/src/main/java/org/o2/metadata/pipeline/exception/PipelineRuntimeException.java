package org.o2.metadata.pipeline.exception;

/**
 * 流水线执行异常类
 *
 * @author mark.bao@hand-china.com 2018/12/21
 */
public class PipelineRuntimeException extends RuntimeException {

    public PipelineRuntimeException(final String code, final Object... parameters) {
        super(String.format(code, parameters));
    }

    public PipelineRuntimeException(final String code, final Throwable cause, final Object... parameters) {
        super(String.format(code, parameters), cause);
    }
}
