package org.o2.business.process.exception;

import org.o2.business.process.data.BusinessProcessExecParam;

/**
 * @author tangcj
 * @version V1.0
 * @date 2022/8/12 15:50
 */
public interface ProcessErrorHandler {

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
    <T extends BusinessProcessExecParam> void errorHandle(String processCode, T processExecParam);

}
