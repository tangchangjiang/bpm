package org.o2.business.process.management.infra.convert;

import io.choerodon.core.convertor.ApplicationContextHelper;
import io.choerodon.core.exception.CommonException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.MapUtils;
import org.hzero.core.base.BaseConstants;
import org.hzero.mybatis.domian.Condition;
import org.hzero.mybatis.util.Sqls;
import org.o2.business.process.management.api.vo.interactive.NotationNode;
import org.o2.business.process.management.domain.LovViewDO;
import org.o2.business.process.management.domain.entity.BizNodeParameter;
import org.o2.business.process.management.domain.repository.BizNodeParameterRepository;
import org.o2.business.process.management.infra.constant.BusinessProcessConstants;
import org.o2.cache.util.CollectionCacheHelper;
import org.o2.core.helper.DateUtil;
import org.o2.core.helper.JsonHelper;
import org.o2.process.domain.engine.definition.Activity.ServiceTask;
import org.o2.process.domain.engine.definition.BaseNode;
import org.o2.process.domain.engine.definition.event.EndEvent;
import org.o2.process.domain.engine.definition.event.StartEvent;
import org.o2.process.domain.engine.definition.gateway.ExclusiveGateway;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author tangcj
 * @version V1.0
 * @date 2022/10/11 10:47
 */
@Slf4j
public class BaseNodeFactory {

    private BaseNodeFactory(){

    }

    public static BaseNode createBaseNode(NotationNode node, Long tenantId){
        switch (node.getShape()){
            case BusinessProcessConstants.CellType.START_NODE:
                return buildStartEvent(node);
            case BusinessProcessConstants.CellType.FLOW_NODE:
                return buildServiceTask(node, tenantId);
            case BusinessProcessConstants.CellType.BRANCH_NODE:
                return buildExclusiveGateway(node);
            case BusinessProcessConstants.CellType.END_NODE:
                return buildEndEvent(node);
            default:
                throw new CommonException(BusinessProcessConstants.ErrorCode.UNSUPPORTED_DRAWING_TYPE);
        }
    }

    public static BaseNode buildStartEvent(NotationNode node){
        StartEvent startEvent = new StartEvent();
        startEvent.setId(node.getId());
        startEvent.setIncoming(new ArrayList<>());
        startEvent.setOutgoing(new ArrayList<>());
        return startEvent;
    }

    public static BaseNode buildServiceTask(NotationNode node, Long tenantId){
        ServiceTask serviceTask = new ServiceTask();
        serviceTask.setId(node.getId());
        serviceTask.setEnabledFlag(node.getData().getEnabledFlag());
        serviceTask.setBeanId(node.getData().getBeanId());
        serviceTask.setArgs(node.getData().getArgs());
        serviceTask.setIncoming(new ArrayList<>());
        serviceTask.setOutgoing(new ArrayList<>());
        dealArgs(serviceTask, tenantId);
        return serviceTask;
    }

    public static BaseNode buildExclusiveGateway(NotationNode node){
        ExclusiveGateway exclusiveGateway = new ExclusiveGateway();
        exclusiveGateway.setId(node.getId());
        exclusiveGateway.setIncoming(new ArrayList<>());
        exclusiveGateway.setOutgoing(new ArrayList<>());
        return exclusiveGateway;
    }

    public static BaseNode buildEndEvent(NotationNode node){
        EndEvent endEvent = new EndEvent();
        endEvent.setId(node.getId());
        endEvent.setIncoming(new ArrayList<>());
        endEvent.setOutgoing(new ArrayList<>());
        return endEvent;
    }

    // 可扩展，节点拿到参数的类型可在这里处理好
    public static void dealArgs(ServiceTask serviceTask, Long tenantId){
        Map<String, Object> argMap = serviceTask.getArgs();
        if(MapUtils.isNotEmpty(argMap)) {
            Map<String, BizNodeParameter> paramDefinitionMap = getBizNodeParameterDefinition(serviceTask, tenantId, argMap);
            for(Map.Entry<String, Object> entry : argMap.entrySet()){
                BizNodeParameter bizNodeParameter = paramDefinitionMap.get(entry.getKey());
                argMap.put(entry.getKey(), parseArgs(bizNodeParameter, entry.getValue()));
            }
        }
    }

    protected static Map<String, BizNodeParameter> getBizNodeParameterDefinition(ServiceTask serviceTask, Long tenantId, Map<String, Object> argMap) {
        Map<String, BizNodeParameter> paramDefinitionMap;
        try {
            BizNodeParameterRepository parameterRepository = ApplicationContextHelper.getContext().getBean(BizNodeParameterRepository.class);
            paramDefinitionMap = CollectionCacheHelper.getCache(BusinessProcessConstants.CacheParam.CACHE_NAME,
                    String.format(BusinessProcessConstants.CacheParam.NODE_PARAM_DEFINITION_CACHE, tenantId, serviceTask.getBeanId()),
                    argMap.keySet(), paramCodes -> {
                        List<BizNodeParameter> bizNodeParameterList = parameterRepository.selectByCondition(Condition.builder(BizNodeParameter.class)
                                .andWhere(Sqls.custom()
                                        .andEqualTo(BizNodeParameter.FIELD_TENANT_ID, tenantId)
                                        .andEqualTo(BizNodeParameter.FIELD_BEAN_ID, serviceTask.getBeanId())
                                        .andIn(BizNodeParameter.FIELD_PARAM_CODE, paramCodes)
                                ).build());
                        return bizNodeParameterList.stream().collect(Collectors.toMap(BizNodeParameter::getParamCode, Function.identity()));
                    });
        } catch (Exception e) {
            log.error("metadata->PipelineDriver->getPipelineByRemote error:{}", e.getMessage());
            CommonException processException = new CommonException(String.format(BusinessProcessConstants.ErrorMessage.BEAN_NETWORK_REQUEST_ERROR, serviceTask.getBeanId()));
            processException.setStackTrace(e.getStackTrace());
            throw processException;
        }
        return paramDefinitionMap;
    }

    protected static Object parseArgs(BizNodeParameter bizNodeParameter, Object paramValue){
        Object result = null;
        try{
            switch (bizNodeParameter.getParamEditTypeCode()) {
                case BusinessProcessConstants.BizParam.DATE_PICKER:
                    result = DateUtil.parseToDate((String) paramValue);
                    break;
                case BusinessProcessConstants.BizParam.DATE_TIME_PICKER:
                    result = DateUtil.parseToDateTime((String) paramValue);
                    break;
                case BusinessProcessConstants.BizParam.INPUT_NUMBER:
                    result = paramValue;
                    break;
                case BusinessProcessConstants.BizParam.COMBO_BOX:
                case BusinessProcessConstants.BizParam.INPUT:
                    if(BaseConstants.Flag.YES.equals(bizNodeParameter.getMultiFlag())) {
                        String temp = (String) paramValue;
                        result = Arrays.stream(temp.split(BaseConstants.Symbol.COMMA)).collect(Collectors.toList());
                        break;
                    }
                    result = paramValue;
                    break;
                case BusinessProcessConstants.BizParam.LOV:
                    List<LovViewDO> lovViewList = JsonHelper.stringToArray((String) paramValue, LovViewDO.class);
                    if(BaseConstants.Flag.YES.equals(bizNodeParameter.getMultiFlag())) {
                        result = lovViewList.stream().map(LovViewDO::getCode).collect(Collectors.toList());
                        break;
                    }
                    result = lovViewList.stream().map(LovViewDO::getCode).findFirst().orElse("");
                    break;
                default:
            }
        }catch (Exception e){
            CommonException processException = new CommonException(String.format(BusinessProcessConstants.ErrorMessage.PARAM_PARSING_ERROR, bizNodeParameter.getBeanId(), bizNodeParameter.getParamCode(), JsonHelper.objectToString(paramValue)));
            processException.setStackTrace(e.getStackTrace());
            throw processException;
        }
        return result;
    }

}
