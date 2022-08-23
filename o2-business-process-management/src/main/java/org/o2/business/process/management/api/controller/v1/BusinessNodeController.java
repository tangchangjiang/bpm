package org.o2.business.process.management.api.controller.v1;

import io.choerodon.core.domain.Page;
import io.choerodon.core.iam.ResourceLevel;
import io.choerodon.mybatis.pagehelper.PageHelper;
import io.choerodon.mybatis.pagehelper.annotation.SortDefault;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import io.choerodon.mybatis.pagehelper.domain.Sort;
import io.choerodon.swagger.annotation.Permission;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.collections4.CollectionUtils;
import org.hzero.boot.platform.lov.annotation.ProcessLovValue;
import org.hzero.core.base.BaseConstants;
import org.hzero.core.base.BaseController;
import org.hzero.core.util.Results;
import org.hzero.mybatis.domian.Condition;
import org.hzero.mybatis.helper.SecurityTokenHelper;
import org.hzero.mybatis.util.Sqls;
import org.o2.business.process.management.api.dto.BatchBusinessNodeQueryDTO;
import org.o2.business.process.management.api.dto.BusinessNodeQueryDTO;
import org.o2.business.process.management.api.vo.BusinessNodeVO;
import org.o2.business.process.management.app.service.BizNodeParameterService;
import org.o2.business.process.management.app.service.BusinessNodeService;
import org.o2.business.process.management.config.BusinessProcessManagerAutoConfiguration;
import org.o2.business.process.management.domain.entity.BizNodeParameter;
import org.o2.business.process.management.domain.entity.BusinessNode;
import org.o2.business.process.management.domain.repository.BusinessNodeRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 业务流程节点表 管理 API
 *
 * @author zhilin.ren@hand-china.com 2022-08-10 14:31:01
 */
@RestController("businessNodeController.v1")
@Api(tags = BusinessProcessManagerAutoConfiguration.BUSINESS_PROCESS_NODE)
@RequestMapping("/v1/{organizationId}/business-nodes")
public class BusinessNodeController extends BaseController {

    private final BusinessNodeRepository businessNodeRepository;
    private final BusinessNodeService businessNodeService;
    private final BizNodeParameterService bizNodeParameterService;
    public static final String FIELD = "body.paramList";

    public BusinessNodeController(BusinessNodeRepository businessNodeRepository, BusinessNodeService businessNodeService, BizNodeParameterService bizNodeParameterService) {
        this.businessNodeRepository = businessNodeRepository;
        this.businessNodeService = businessNodeService;
        this.bizNodeParameterService = bizNodeParameterService;
    }

    @ApiOperation(value = "业务流程节点表维护-分页查询业务流程节点表列表")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @ProcessLovValue(targetField = {BaseConstants.FIELD_BODY, FIELD})
    @GetMapping
    public ResponseEntity<Page<BusinessNodeVO>> page(@PathVariable(value = "organizationId") Long organizationId,
                                                     BusinessNodeQueryDTO businessNodeQueryDTO,
                                                     @ApiIgnore @SortDefault(value = BusinessNode.FIELD_BIZ_NODE_ID,
                                                                     direction = Sort.Direction.DESC) PageRequest pageRequest) {

        businessNodeQueryDTO.setTenantId(organizationId);
        Page<BusinessNodeVO> page = PageHelper.doPageAndSort(pageRequest, () -> businessNodeRepository.listBusinessNode(businessNodeQueryDTO));

        // lov查询需要获取节点参数信息
        if (BaseConstants.Flag.YES.equals(businessNodeQueryDTO.getLovFlag()) && CollectionUtils.isNotEmpty(page.getContent())) {
            List<String> beanIdList = page.getContent().stream().map(BusinessNodeVO::getBeanId).collect(Collectors.toList());
            List<BizNodeParameter> bizNodeParameterList = bizNodeParameterService.getBizNodeParameterList(beanIdList, organizationId);
            if (CollectionUtils.isNotEmpty(bizNodeParameterList)) {
                Map<String, List<BizNodeParameter>> bizNodeParameterMap = bizNodeParameterList.stream().collect(Collectors.groupingBy(BizNodeParameter::getBeanId));
                page.getContent().forEach(v -> v.setParamList(bizNodeParameterMap.get(v.getBeanId())));
            }
        }

        return Results.success(page);
    }

    @ApiOperation(value = "业务流程节点表维护-查询业务流程节点表列表(不分页)")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @ProcessLovValue(targetField = {BaseConstants.FIELD_BODY, FIELD})
    @GetMapping("list")
    public ResponseEntity<List<BusinessNodeVO>> list(@PathVariable(value = "organizationId") Long organizationId,
                                                     BusinessNodeQueryDTO businessNodeQueryDTO) {

        businessNodeQueryDTO.setTenantId(organizationId);
        List<BusinessNodeVO> businessNodeList = businessNodeRepository.listBusinessNode(businessNodeQueryDTO);

        // lov查询需要获取节点参数信息
        if (CollectionUtils.isNotEmpty(businessNodeList)) {
            List<String> beanIdList = businessNodeList.stream().map(BusinessNodeVO::getBeanId).collect(Collectors.toList());
            List<BizNodeParameter> bizNodeParameterList = bizNodeParameterService.getBizNodeParameterList(beanIdList, organizationId);
            if (CollectionUtils.isNotEmpty(bizNodeParameterList)) {
                Map<String, List<BizNodeParameter>> bizNodeParameterMap = bizNodeParameterList.stream().collect(Collectors.groupingBy(BizNodeParameter::getBeanId));
                businessNodeList.forEach(v -> v.setParamList(bizNodeParameterMap.get(v.getBeanId())));
            }
        }
        return Results.success(businessNodeList);
    }


    @ApiOperation(value = "业务流程节点表维护-根据beanId查询业务流程节点表列表&节点参数信息")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @ProcessLovValue(targetField = {BaseConstants.FIELD_BODY, FIELD})
    @PostMapping("list-by-bean-Id")
    public ResponseEntity<List<BusinessNode>> listByBeanId(@PathVariable(value = "organizationId") Long organizationId,
                                                           @RequestBody BatchBusinessNodeQueryDTO batchBusinessNodeQueryDTO
    ) {

        validObject(batchBusinessNodeQueryDTO);
        List<BusinessNode> businessNodes = businessNodeRepository.selectByCondition(Condition.builder(BusinessNode.class)
                .andWhere(Sqls.custom().andIn(BusinessNode.FIELD_BEAN_ID, batchBusinessNodeQueryDTO.getBeanIdList())
                        .andEqualTo(BusinessNode.FIELD_TENANT_ID, organizationId))
                .build());
        // 获取节点参数信息
        if (CollectionUtils.isNotEmpty(businessNodes)) {
            List<BizNodeParameter> bizNodeParameterList = bizNodeParameterService.getBizNodeParameterList(batchBusinessNodeQueryDTO.getBeanIdList(), organizationId);
            if (CollectionUtils.isNotEmpty(bizNodeParameterList)) {
                Map<String, List<BizNodeParameter>> bizNodeParameterMap = bizNodeParameterList.stream().collect(Collectors.groupingBy(BizNodeParameter::getBeanId));
                businessNodes.forEach(v -> v.setParamList(bizNodeParameterMap.get(v.getBeanId())));
            }

        }
        return Results.success(businessNodes);
    }

    @ApiOperation(value = "业务流程节点表维护-查询业务流程节点表明细")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @ProcessLovValue(targetField = BaseConstants.FIELD_BODY)
    @GetMapping("/{bizNodeId}")
    public ResponseEntity<BusinessNode> detail(@PathVariable(value = "organizationId") Long organizationId,
                                                        @ApiParam(value = "业务流程节点表ID", required = true) @PathVariable Long bizNodeId) {
        BusinessNode businessNode = businessNodeService.detail(bizNodeId);
        return Results.success(businessNode);
    }

    @ApiOperation(value = "业务流程节点表维护-创建业务流程节点表")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PostMapping
    public ResponseEntity<BusinessNode> create(@PathVariable(value = "organizationId") Long organizationId,
                                                       @RequestBody BusinessNode businessNode) {
        businessNode.setTenantId(organizationId);
        validObject(businessNode);
        return Results.success(businessNodeService.save(businessNode));
    }

    @ApiOperation(value = "业务流程节点表维护-修改业务流程节点表")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PutMapping
    public ResponseEntity<BusinessNode> update(@PathVariable(value = "organizationId") Long organizationId,
                                                       @RequestBody BusinessNode businessNode) {
        SecurityTokenHelper.validToken(businessNode);
        return Results.success(businessNodeService.save(businessNode));
    }
}
