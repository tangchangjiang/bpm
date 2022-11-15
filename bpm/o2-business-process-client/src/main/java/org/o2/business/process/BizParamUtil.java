package org.o2.business.process;

import io.choerodon.core.convertor.ApplicationContextHelper;
import lombok.extern.slf4j.Slf4j;
import org.hzero.core.base.BaseConstants;
import org.hzero.core.util.ResponseUtils;
import org.o2.business.process.constants.BusinessProcessConstants;
import org.o2.business.process.domain.BizNodeParameterDO;
import org.o2.business.process.exception.BusinessProcessRuntimeException;
import org.o2.business.process.infra.BusinessProcessRemoteService;
import org.o2.cache.util.CollectionCacheHelper;
import org.o2.core.helper.DateUtil;
import org.o2.core.helper.JsonHelper;

import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author tangcj
 * @version V1.0
 * @date 2022/11/2 11:36
 */
@Slf4j
public class BizParamUtil {

    public static final Map<String, Class<?>> PARAM_CLASS_MAP;

    static {
        Map<String, Class<?>> map = new HashMap<>();
        map.put(BusinessProcessConstants.BizParam.DATE_PICKER, Date.class);
        map.put(BusinessProcessConstants.BizParam.DATE_TIME_PICKER, Date.class);
        map.put(BusinessProcessConstants.BizParam.INPUT_NUMBER, Integer.class);
        map.put(BusinessProcessConstants.BizParam.COMBO_BOX, String.class);
        map.put(BusinessProcessConstants.BizParam.LOV, String.class);
        map.put(BusinessProcessConstants.BizParam.INPUT, String.class);
        PARAM_CLASS_MAP = Collections.unmodifiableMap(map);
    }

    private BizParamUtil() {
    }

    @SuppressWarnings("unchecked")
    public static <T> T paramParsing(String beanId, String paramCode, Object paramValue, Long tenantId) {
        Map<String, BizNodeParameterDO> paramDefinitionMap;
        try {
            BusinessProcessRemoteService pipelineRemoteService = ApplicationContextHelper.getContext().getBean(BusinessProcessRemoteService.class);
            paramDefinitionMap = CollectionCacheHelper.getCache(BusinessProcessConstants.CacheParam.CACHE_NAME,
                    String.format(BusinessProcessConstants.CacheParam.NODE_PARAM_DEFINITION_CACHE, tenantId, beanId),
                    Collections.singletonList(paramCode), paramCodes -> {
                        BizNodeParameterDO bizNodeParameterDO = ResponseUtils.getResponse(pipelineRemoteService.getParamDefinition(tenantId, beanId,
                                paramCode), BizNodeParameterDO.class);
                        if (null == bizNodeParameterDO) {
                            return Collections.emptyMap();
                        }
                        Map<String, BizNodeParameterDO> map = new HashMap<>();
                        map.put(paramCode, bizNodeParameterDO);
                        return map;
                    });
        } catch (Exception e) {
            log.error("metadata->PipelineDriver->getPipelineByRemote error:{}", e.getMessage());
            BusinessProcessRuntimeException processException =
                    new BusinessProcessRuntimeException(BusinessProcessConstants.ErrorMessage.PIPELINE_NETWORK_REQUEST_ERROR, beanId);
            processException.setStackTrace(e.getStackTrace());
            throw processException;
        }
        if (!paramDefinitionMap.containsKey(paramCode)) {
            throw new BusinessProcessRuntimeException(BusinessProcessConstants.ErrorMessage.PARAM_DEFINITION_NOT_EXISTS, beanId, paramCode);
        }
        BizNodeParameterDO parameterDO = paramDefinitionMap.get(paramCode);
        if (!PARAM_CLASS_MAP.containsKey(parameterDO.getParamEditTypeCode())) {
            throw new BusinessProcessRuntimeException(BusinessProcessConstants.ErrorMessage.PARAM_TYPE_NOT_SUPPORTED, beanId, paramCode);
        }
        Object result = doParsing(beanId, paramCode, paramValue, parameterDO);
        return (T) result;
    }

    protected static Object doParsing(String beanId, String paramCode, Object paramValue, BizNodeParameterDO parameterDO) {
        Object result = null;
        try {
            switch (parameterDO.getParamEditTypeCode()) {
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
                case BusinessProcessConstants.BizParam.LOV:
                case BusinessProcessConstants.BizParam.INPUT:
                    if (BaseConstants.Flag.YES.equals(parameterDO.getMultiFlag())) {
                        String temp = (String) paramValue;
                        result = Arrays.stream(temp.split(BaseConstants.Symbol.COMMA)).collect(Collectors.toList());
                        break;
                    }
                    result = paramValue;
                    break;
                default:
            }
        } catch (Exception e) {
            BusinessProcessRuntimeException processException =
                    new BusinessProcessRuntimeException(BusinessProcessConstants.ErrorMessage.PARAM_PARSING_ERROR, beanId, paramCode,
                            JsonHelper.objectToString(paramValue));
            processException.setStackTrace(e.getStackTrace());
            throw processException;
        }
        return result;
    }

}
