package org.o2.business.process.management.infra.convert;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.choerodon.core.convertor.ApplicationContextHelper;
import io.choerodon.core.exception.CommonException;
import org.o2.business.process.management.api.vo.interactive.NotationEdge;
import org.o2.business.process.management.api.vo.interactive.NotationNode;
import org.o2.business.process.management.api.vo.interactive.ProcessModel;
import org.o2.business.process.management.infra.constant.BusinessProcessConstants;
import org.o2.process.domain.engine.BpmnModel;
import org.o2.process.domain.engine.definition.BaseElement;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author tangcj
 * @version V1.0
 * @date 2022/10/11 10:17
 */
public class ViewJsonConvert {

    private static volatile ObjectMapper objectMapper;

    private ViewJsonConvert(){
    }

    static {
        objectMapper = ApplicationContextHelper.getContext().getBean(ObjectMapper.class);
    }

    public static List<BaseElement> viewJsonConvert(String viewJson) {

        ProcessModel processModel = null;
        try {
            processModel = objectMapper.readValue(viewJson, ProcessModel.class);
        } catch (JsonProcessingException e) {
            throw new CommonException(e);
        }
        List<NotationEdge> notationEdges = processModel.getCells().stream()
                .filter(cell -> BusinessProcessConstants.CellType.FLOW_EDGE.equals(cell.getShape()))
                .map(cell -> (NotationEdge) cell).collect(Collectors.toList());

        List<NotationNode> notationNodes = processModel.getCells().stream()
                .filter(cell -> !BusinessProcessConstants.CellType.FLOW_EDGE.equals(cell.getShape()))
                .map(cell -> (NotationNode) cell).collect(Collectors.toList());

        List<BaseElement> baseElements = new ArrayList<>();
        notationNodes.forEach(node -> baseElements.add(BaseNodeFactory.createBaseNode(node)));

        List<BaseElement> result = new ArrayList<>();
        Map<String, BaseElement> elementMap = baseElements.stream().collect(Collectors.toMap(BaseElement::getId, Function.identity()));
        for (NotationEdge edge : notationEdges){
            BaseElement source = elementMap.get(edge.getSource().getCell());
            BaseElement target = elementMap.get(edge.getTarget().getCell());
            if(null == source || null == target){
                throw new CommonException(BusinessProcessConstants.ErrorCode.NO_CORRESPONDING_NODE_FOUND);
            }
            result.add(BaseFlowFactory.dealEdge(source, target, edge));
        }
        result.addAll(elementMap.values());
        return result;
    }

    public static String bpmnToJson(BpmnModel bpmnModel) {
        String result;
        try{
            result = objectMapper.writeValueAsString(bpmnModel);
        }catch (JsonProcessingException e) {
            throw new CommonException(e);
        }
        return result;
    }

    public static BpmnModel processJsonConvert(String processJson){
        BpmnModel result;
        try{
            result = objectMapper.readValue(processJson, BpmnModel.class);
        }catch (JsonProcessingException e) {
            throw new CommonException(e);
        }
        return result;
    }
}
