package org.o2.business.process.data;

import lombok.Getter;
import lombok.Setter;

import javax.xml.bind.annotation.XmlRootElement;
import java.util.Map;

/**
 * 流水线节点传输参数
 *
 * @author mark.bao@hand-china.com 2018/12/21
 */
@XmlRootElement(name = "businessProcessExecParam")
public class BusinessProcessExecParam {

    @Getter
    @Setter
    private Map<String, String> currentParam;
}
