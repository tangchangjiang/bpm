package org.o2.process.domain.engine.runtime;


import org.hzero.core.base.BaseConstants;
import org.o2.core.helper.DateUtil;
import org.o2.process.domain.engine.BusinessProcessExecParam;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

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

    default List<String> paramParsingList(String paramCode, T dataObject){
        return Arrays.stream(dataObject.getCurrentParam().get(paramCode).split(BaseConstants.Symbol.COMMA)).collect(Collectors.toList());
    }

    default String paramParsing(String paramCode, T dataObject){
        return dataObject.getCurrentParam().get(paramCode);
    }

    default Date paramParsingDate(String paramCode, T dataObject) throws ParseException {
        return DateUtil.parseToDateTime(dataObject.getCurrentParam().get(paramCode));
    }

    default Long paramParsingLong(String paramCode, T dataObject){
        return Long.valueOf(dataObject.getCurrentParam().get(paramCode));
    }

    default BigDecimal paramParsingBigDecimal(String paramCode, T dataObject){
        return new BigDecimal(dataObject.getCurrentParam().get(paramCode));
    }
}
