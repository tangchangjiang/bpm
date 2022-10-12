package org.o2.metadata.pipeline.data;

import lombok.Getter;
import lombok.Setter;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * 流水线节点传输参数
 *
 * @author mark.bao@hand-china.com 2018/12/21
 */
@XmlRootElement(name = "pipelineExecParam")
public class PipelineExecParam {

    @Getter
    @Setter
    private String nextStrategy;
}
