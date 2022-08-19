package org.o2.business.process.data;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.xml.bind.annotation.XmlRootElement;
import java.util.Map;

/**
 * 流水线节点传输参数
 *
 * @author mark.bao@hand-china.com 2018/12/21
 */
@XmlRootElement(name = "businessProcessExecParam")
@Data
public class BusinessProcessExecParam {

    private Map<String, String> currentParam;
    @JsonIgnore
    @JSONField(serialize = false)
    private Boolean nextFlag;
    @JsonIgnore
    @JSONField(serialize = false)
    private Exception exception;
}
