package org.o2.business.process.exception;


import org.apache.commons.lang3.exception.ExceptionUtils;
import org.hzero.core.base.BaseConstants;
import org.o2.process.domain.engine.BusinessProcessExecParam;

/**
 * @author tangcj
 * @version V1.0
 * @date 2022/10/10 15:49
 */
public interface ProcessErrorHandler<T extends BusinessProcessExecParam> {

    String DEFAULT = "default";

    /**
     * 获取业务流程编码
     * @return
     */
    String getProcessCode();


    /**
     * 错误处理
     * @param processCode
     * @param processExecParam
     */
    void errorHandle(String processCode, T processExecParam);

    /**
     * 获取错误堆栈信息，直到O2包处理类
     * @param processExecParam
     * @return
     */
    default String getErrorMessage(T processExecParam){
        Exception e = processExecParam.getException();
        StringBuilder sb = new StringBuilder();
        if(e != null) {
            sb.append(ExceptionUtils.getMessage(e)).append(BaseConstants.Symbol.LB);
            StackTraceElement[] stackTraceElements = e.getStackTrace();
            for (StackTraceElement s : stackTraceElements) {
                sb.append(s).append(BaseConstants.Symbol.LB);
                if (s.getClassName().startsWith("org.o2")) {
                    break;
                }
            }
        }
        return sb.toString();
    }
}
