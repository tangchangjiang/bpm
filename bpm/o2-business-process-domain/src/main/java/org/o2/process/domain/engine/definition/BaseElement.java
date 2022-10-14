package org.o2.process.domain.engine.definition;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import io.choerodon.core.exception.CommonException;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.o2.core.helper.JsonHelper;
import org.o2.process.domain.engine.definition.Activity.ServiceTask;
import org.o2.process.domain.engine.definition.event.EndEvent;
import org.o2.process.domain.engine.definition.event.StartEvent;
import org.o2.process.domain.engine.definition.flow.ConditionalFlow;
import org.o2.process.domain.engine.definition.flow.DefaultFlow;
import org.o2.process.domain.engine.definition.flow.SequenceFlow;
import org.o2.process.domain.engine.definition.gateway.ExclusiveGateway;
import org.o2.process.domain.infra.ProcessEngineConstants;

import java.util.List;
import java.util.Map;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type", visible = true,
        defaultImpl = Void.class, include = JsonTypeInfo.As.EXISTING_PROPERTY)
@JsonSubTypes({
        @JsonSubTypes.Type(value = StartEvent.class, name = "START_EVENT"),
        @JsonSubTypes.Type(value = EndEvent.class, name = "END_EVENT"),
        @JsonSubTypes.Type(value = ServiceTask.class, name = "SERVICE_TASK"),
        @JsonSubTypes.Type(value = ExclusiveGateway.class, name = "EXCLUSIVE_GATEWAY"),
        @JsonSubTypes.Type(value = SequenceFlow.class, name = "SEQUENCE_FLOW"),
        @JsonSubTypes.Type(value = DefaultFlow.class, name = "DEFAULT_FLOW"),
        @JsonSubTypes.Type(value = ConditionalFlow.class, name = "CONDITIONAL_FLOW"),
})
@Data
@Slf4j
public abstract class BaseElement implements Element {

    private String id; // 流程内元素唯一key resourceId

    private List<String> outgoing;

    private List<String> incoming;

    private String type;

    protected void checkIncoming(Map<String, BaseElement> elementMap) {
        if (CollectionUtils.isEmpty(this.incoming)) {
            throwElementValidatorException(ProcessEngineConstants.ErrorCode.ELEMENT_LACK_INCOMING);
        }
    }

    protected void checkOutgoing(Map<String, BaseElement> elementMap) {
        if (CollectionUtils.isEmpty(this.outgoing)) {
            throwElementValidatorException(ProcessEngineConstants.ErrorCode.ELEMENT_LACK_OUTGOING);
        }

        if(this.outgoing.size() > 1){
            throwElementValidatorException(ProcessEngineConstants.ErrorCode.ELEMENT_TOO_MUCH_OUTGOING);
        }
    }


    protected void checkElement(Map<String, BaseElement> elementMap, List<String> nodes, List<String> validateType, String errorCode){
        if(MapUtils.isEmpty(elementMap)){
            throwElementValidatorException(ProcessEngineConstants.ErrorCode.ELEMENT_ILLEGAL_VERIFICATION_PARAMETERS);
        }
        for(String node : nodes){
            if(!elementMap.containsKey(node)){
                throwElementValidatorException(ProcessEngineConstants.ErrorCode.MODEL_UNKNOWN_ELEMENT_KEY);
            }
            if(!validateType.contains(elementMap.get(node).getType())){
                throwElementValidatorException(errorCode);
            }
        }
    }

    public void validate(Map<String, BaseElement> elementMap) {
        checkIncoming(elementMap);
        checkOutgoing(elementMap);
    }

    protected void throwElementValidatorException(String errorCode){
        String exceptionMsg = getElementValidatorExceptionMsg(errorCode);
        log.info(exceptionMsg);
        throw new CommonException(errorCode, exceptionMsg);
    }

    /**
     * 二开入口，可以对出错的数据进行一些处理
     * @param errorCode 错误编码
     */
    protected void recordElementValidatorException(String errorCode) {
        String exceptionMsg = getElementValidatorExceptionMsg(errorCode);
        log.warn(exceptionMsg);
    }

    protected String getElementValidatorExceptionMsg(String errorCode){
        return String.format(MODEL_DEFINITION_ERROR_MSG_FORMAT, errorCode, JsonHelper.objectToString(this));
    }

    public static final String MODEL_DEFINITION_ERROR_MSG_FORMAT = "message={%s}, FlowElement={%s}";

}
