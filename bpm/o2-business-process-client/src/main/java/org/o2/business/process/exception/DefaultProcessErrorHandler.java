package org.o2.business.process.exception;

import lombok.extern.slf4j.Slf4j;
import org.o2.process.domain.engine.BusinessProcessExecParam;
import org.springframework.stereotype.Service;

/**
 * @author tangcj
 * @version V1.0
 * @date 2022/10/10 15:50
 */
@Slf4j
@Service
public class DefaultProcessErrorHandler<T extends BusinessProcessExecParam> implements ProcessErrorHandler<T> {

    @Override
    public String getProcessCode() {
        return DEFAULT;
    }

    @Override
    public void errorHandle(String processCode, T processExecParam) {
        log.error("business process:{} error, error info:{}", processCode, getErrorMessage(processExecParam));
    }

}
