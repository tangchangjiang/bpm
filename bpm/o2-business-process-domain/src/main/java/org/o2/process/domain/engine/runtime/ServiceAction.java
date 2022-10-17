package org.o2.process.domain.engine.runtime;


import org.o2.process.domain.engine.BusinessProcessExecParam;

/**
 * 流水线节点执行器
 *
 * @author mark.bao@hand-china.com 2018/12/21
 */
public interface ServiceAction<T extends BusinessProcessExecParam> {


    /**
     * 节点执行前
     * @param dataObject
     */
    default void beforeExecution(final T dataObject){

    }


    /**
     * 流水线节点执行实体
     *
     * @param dataObject 执行流转数据参数(已序列化)
     */
    void run(final T dataObject);


    /**
     * 节点执行后
     * @param dataObject
     */
    default void afterExecution(final T dataObject){

    }

}
