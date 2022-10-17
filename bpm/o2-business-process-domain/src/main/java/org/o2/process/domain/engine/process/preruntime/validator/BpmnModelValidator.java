/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.o2.process.domain.engine.process.preruntime.validator;

import com.google.common.collect.Maps;
import io.choerodon.core.exception.CommonException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.o2.process.domain.engine.BpmnModel;
import org.o2.process.domain.engine.definition.BaseElement;
import org.o2.process.domain.infra.ProcessEngineConstants;
import org.o2.process.domain.infra.ProcessEngineConstants.FlowElementType;
import org.o2.process.domain.util.BpmnModelUtil;
import org.o2.process.domain.util.DirectedGraph;

import java.text.MessageFormat;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author yusu
 */
@Slf4j
public class BpmnModelValidator {

    public static final String MODEL_DEFINITION_ERROR_MSG_FORMAT = "message={%s}, elementId={%s}";

    public static void validate(BpmnModel flowModel){

        if (flowModel == null || CollectionUtils.isEmpty(flowModel.getFlowElements())) {
            log.info("message={}", ProcessEngineConstants.ErrorCode.MODEL_EMPTY);
            throw new CommonException(ProcessEngineConstants.ErrorCode.MODEL_EMPTY);
        }

        List<BaseElement> flowElementList = flowModel.getFlowElements();

        Map<String, BaseElement> flowElementMap = getFlowElementMap(flowElementList);

        checkElement(flowElementList, flowElementMap);

        checkCycle(flowModel, flowElementMap);
    }

    protected static Map<String, BaseElement> getFlowElementMap(List<BaseElement> flowElementList) {
        Map<String, BaseElement> flowElementMap = Maps.newHashMap();

        for(BaseElement flowElement : flowElementList) {
            if(!FlowElementType.FLOW.contains(flowElement.getType())
                    && !FlowElementType.NODE.contains(flowElement.getType())){
                throw new CommonException(ProcessEngineConstants.ErrorCode.INVALID_ELEMENT_TYPE);
            }

            if (flowElementMap.containsKey(flowElement.getId())) {
                String exceptionMsg = MessageFormat.format(BpmnModelValidator.MODEL_DEFINITION_ERROR_MSG_FORMAT,
                        ProcessEngineConstants.ErrorCode.ELEMENT_KEY_NOT_UNIQUE, flowElement.getId());
                log.info(exceptionMsg);
                throw new CommonException(exceptionMsg);
            }
            flowElementMap.put(flowElement.getId(), BpmnModelUtil.CLASS_MAP.get(flowElement.getType()).cast(flowElement));
        }
        return flowElementMap;
    }

    /**
     * 对每个元素进行校验，并且校验起始节点和结束节点的数量
     * @param flowElementList 所有元素
     * @param flowElementMap 元素集合
     */
    protected static void checkElement(List<BaseElement> flowElementList, Map<String, BaseElement> flowElementMap) {
        int startEventCount = 0;
        int endEventCount = 0;

        for (BaseElement flowElement : flowElementList) {

            //进行校验
            flowElement.validate(flowElementMap);

            if (FlowElementType.START_EVENT.equals(flowElement.getType())) {
                startEventCount++;
            }

            if (FlowElementType.END_EVENT.equals(flowElement.getType())) {
                endEventCount++;
            }
        }

        if (startEventCount != 1) {
            log.info("message={}||startEventCount={}", ProcessEngineConstants.ErrorCode.START_NODE_INVALID, startEventCount);
            throw new CommonException(ProcessEngineConstants.ErrorCode.START_NODE_INVALID);
        }

        if (endEventCount < 1) {
            log.info("message={}", ProcessEngineConstants.ErrorCode.END_NODE_INVALID);
            throw new CommonException(ProcessEngineConstants.ErrorCode.END_NODE_INVALID);
        }
    }


    /**
     * 死循环校验
     * @param flowModel 流程模型
     * @param flowElementMap 元素集合
     */
    private static void checkCycle(BpmnModel flowModel, Map<String, BaseElement> flowElementMap) {
        DirectedGraph<BaseElement> directedGraph = new DirectedGraph<>();
        for (BaseElement node : flowModel.getAllNodes()) {
            List<String> outgoingNodes = node.getOutgoing();
            if (CollectionUtils.isNotEmpty(outgoingNodes)) {
                outgoingNodes.forEach(
                        outgoingNode -> directedGraph.add(DirectedGraph.Edge.of(node, flowElementMap.get(outgoingNode))));
            }
        }
        List<BaseElement> cyclicVertexList = directedGraph.findCyclicVertexList();
        if (CollectionUtils.isNotEmpty(cyclicVertexList)) {
            throw new CommonException("Cyclic nodes found in flow " + flowModel.getProcessCode()
                    + " check node [" + cyclicVertexList.stream().map(BaseElement::getId)
                    .collect(Collectors.joining(",")) + "]");
        }
    }

}
