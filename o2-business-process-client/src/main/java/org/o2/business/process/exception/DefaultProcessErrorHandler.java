package org.o2.business.process.exception;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.o2.business.process.data.BusinessProcessExecParam;
import org.springframework.stereotype.Service;

/**
 * @author tangcj
 * @version V1.0
 * @date 2022/8/12 16:32
 */
@Service
@Slf4j
public class DefaultProcessErrorHandler<T extends BusinessProcessExecParam> implements ProcessErrorHandler<T>{

    @Override
    public String getProcessCode() {
        return DEFAULT;
    }

    @Override
    public void errorHandle(String processCode ,T processExecParam) {
       log.error("business process:{} error, error info:{}", processCode, ExceptionUtils.getMessage(processExecParam.getException()));
    }
}
