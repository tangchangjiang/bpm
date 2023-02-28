package org.o2.metadata.console.app.service.impl;

import io.choerodon.core.exception.CommonException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.hzero.core.base.BaseConstants;
import org.hzero.mybatis.domian.Condition;
import org.hzero.mybatis.helper.SecurityTokenHelper;
import org.hzero.mybatis.util.Sqls;
import org.o2.core.exception.O2CommonException;
import org.o2.core.helper.JsonHelper;
import org.o2.metadata.console.api.co.CarrierCO;
import org.o2.metadata.console.api.co.CarrierDeliveryRangeCO;
import org.o2.metadata.console.api.co.CarrierLogisticsCostCO;
import org.o2.metadata.console.api.co.CarrierMappingCO;
import org.o2.metadata.console.api.dto.CarrierDeliveryRangeDTO;
import org.o2.metadata.console.api.dto.CarrierFreightDTO;
import org.o2.metadata.console.api.dto.CarrierLogisticsCostDTO;
import org.o2.metadata.console.api.dto.CarrierMappingQueryInnerDTO;
import org.o2.metadata.console.api.dto.CarrierQueryInnerDTO;
import org.o2.metadata.console.api.dto.ReceiveAddressDTO;
import org.o2.metadata.console.app.bo.CarrierLogisticsCostBO;
import org.o2.metadata.console.app.bo.CarrierLogisticsCostDetailBO;
import org.o2.metadata.console.app.service.CarrierService;
import org.o2.metadata.console.infra.constant.CarrierConstants;
import org.o2.metadata.console.infra.convertor.CarrierConverter;
import org.o2.metadata.console.infra.entity.Carrier;
import org.o2.metadata.console.infra.entity.CarrierCantDelivery;
import org.o2.metadata.console.infra.entity.CarrierDeliveryRange;
import org.o2.metadata.console.infra.entity.CarrierMapping;
import org.o2.metadata.console.infra.entity.PosRelCarrier;
import org.o2.metadata.console.infra.redis.CarrierRedis;
import org.o2.metadata.console.infra.repository.CarrierCantDeliveryRepository;
import org.o2.metadata.console.infra.repository.CarrierDeliveryRangeRepository;
import org.o2.metadata.console.infra.repository.CarrierMappingRepository;
import org.o2.metadata.console.infra.repository.CarrierRepository;
import org.o2.metadata.console.infra.repository.PosRelCarrierRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 承运商应用服务默认实现
 *
 * @author tingting.wang@hand-china.com 2019-3-25
 */
@Service
@Slf4j
public class CarrierServiceImpl implements CarrierService {
    private final CarrierRepository carrierRepository;
    private final CarrierDeliveryRangeRepository carrierDeliveryRangeRepository;
    private final PosRelCarrierRepository posRelCarrierRepository;
    private final CarrierRedis carrierRedis;
    private final CarrierMappingRepository carrierMappingRepository;

    private final CarrierCantDeliveryRepository carrierCantDeliveryRepository;

    public CarrierServiceImpl(final CarrierRepository carrierRepository,
                              final CarrierDeliveryRangeRepository carrierDeliveryRangeRepository,
                              final PosRelCarrierRepository posRelCarrierRepository,
                              CarrierRedis carrierRedis, CarrierMappingRepository carrierMappingRepository,
                              CarrierCantDeliveryRepository carrierCantDeliveryRepository) {
        this.carrierRepository = carrierRepository;
        this.carrierDeliveryRangeRepository = carrierDeliveryRangeRepository;
        this.posRelCarrierRepository = posRelCarrierRepository;
        this.carrierRedis = carrierRedis;
        this.carrierMappingRepository = carrierMappingRepository;
        this.carrierCantDeliveryRepository = carrierCantDeliveryRepository;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<Carrier> batchMerge(Long organizationId, final List<Carrier> carrierList) {
        List<Carrier> updateList = new ArrayList<>();
        List<Carrier> insertList = new ArrayList<>();
        // 中台页面 控制了不能批量新建/更新数据 后续前端改成批量在优化
        List<Carrier> resultList = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(carrierList)) {
            for (Carrier carrier : carrierList) {
                // 校验优先级不可重复
                int result = carrierRepository.selectCountByCondition(Condition.builder(Carrier.class)
                        .andWhere(Sqls.custom().andEqualTo(Carrier.FIELD_PRIORITY, carrier.getPriority())
                                .andEqualTo(Carrier.FIELD_TENANT_ID, organizationId)
                                .andNotEqualTo(Carrier.FIELD_CARRIER_ID, carrier.getCarrierId(), true))
                        .build());
                if (result > 0) {
                    // 存在，不允许新建
                    throw new CommonException(CarrierConstants.ErrorCode.ERROR_EXISTS_PRIORITY_DATA);
                }
                carrier.setTenantId(organizationId);
                // 新增
                if (null == carrier.getCarrierId()) {
                    validCarrierNameUnique(carrier);
                    validCarrierCodeUnique(carrier);
                    insertList.add(carrier);
                } else {
                    SecurityTokenHelper.validToken(carrier);
                    // 更新
                    Carrier original = carrierRepository.selectByPrimaryKey(carrier);
                    if (!original.getCarrierName().equals(carrier.getCarrierName())) {
                        validCarrierNameUnique(carrier);
                    }
                    if (!original.getCarrierCode().equals(carrier.getCarrierCode())) {
                        throw new O2CommonException(null, CarrierConstants.ErrorCode.ERROR_CARRIER_CODE_NOT_UPDATE,
                                CarrierConstants.ErrorCode.ERROR_CARRIER_CODE_NOT_UPDATE);
                    }
                    updateList.add(carrier);
                }
            }
            carrierRepository.batchUpdateByPrimaryKey(updateList);
            carrierRepository.batchInsertSelective(insertList);
            carrierRedis.batchUpdateRedis(organizationId);
            resultList.addAll(updateList);
            resultList.addAll(insertList);
        }
        return resultList;
    }

    /**
     * 验证承运商名称唯一性
     *
     * @param carrier 承运商
     */
    private void validCarrierNameUnique(Carrier carrier) {
        Carrier query = new Carrier();
        query.setCarrierName(carrier.getCarrierName());
        query.setTenantId(carrier.getTenantId());
        List<Carrier> list = carrierRepository.select(query);
        if (!list.isEmpty()) {
            throw new O2CommonException(null, CarrierConstants.ErrorCode.ERROR_CARRIER_NAME_DUPLICATE,
                    CarrierConstants.ErrorCode.ERROR_CARRIER_NAME_DUPLICATE);
        }
    }

    /**
     * 验证承运商编码唯一性
     *
     * @param carrier 承运商
     */
    private void validCarrierCodeUnique(Carrier carrier) {
        Carrier query = new Carrier();
        query.setCarrierCode(carrier.getCarrierCode());
        query.setTenantId(carrier.getTenantId());
        List<Carrier> list = carrierRepository.select(query);
        if (!list.isEmpty()) {
            throw new O2CommonException(null, CarrierConstants.ErrorCode.ERROR_CARRIER_CODE_DUPLICATE,
                    CarrierConstants.ErrorCode.ERROR_CARRIER_CODE_DUPLICATE);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void batchDelete(Long organizationId, List<Carrier> carrierList) {
        List<Long> ids = new ArrayList<>(carrierList.size());
        for (final Carrier carrier : carrierList) {
            carrier.setTenantId(organizationId);
            ids.add(carrier.getCarrierId());
            if (carrier.getCarrierId() != null) {
                Long carrierId = carrier.getCarrierId();
                //删除承运商送达范围
                final List<CarrierDeliveryRange> list = carrierDeliveryRangeRepository.selectByCondition(Condition.builder(CarrierDeliveryRange.class)
                        .andWhere(Sqls.custom().andEqualTo(CarrierDeliveryRange.FIELD_TENANT_ID, organizationId)
                                .andEqualTo(CarrierDeliveryRange.FIELD_CARRIER_ID, carrierId)).build());
                carrierDeliveryRangeRepository.batchDeleteByPrimaryKey(list);
                //删除服务点关联承运商
                final List<PosRelCarrier> posRelCarrierList =
                        posRelCarrierRepository.selectByCondition(Condition.builder(PosRelCarrier.class).andWhere(Sqls.custom()
                        .andEqualTo(PosRelCarrier.FIELD_TENANT_ID, organizationId).andEqualTo(PosRelCarrier.FIELD_CARRIER_ID, carrierId)).build());
                posRelCarrierRepository.batchDeleteByPrimaryKey(posRelCarrierList);
            }
        }
        List<Carrier> carriers = carrierRepository.selectByIds(StringUtils.join(ids, ","));
        carrierRepository.batchDeleteByPrimaryKey(carrierList);
        carrierRedis.deleteRedis(carriers, organizationId);
    }

    @Override
    public Map<String, CarrierCO> listCarriers(CarrierQueryInnerDTO carrierQueryInnerDTO, Long organizationId) {
        Map<String, CarrierCO> map = new HashMap<>(16);
        List<Carrier> carriers = carrierRepository.batchSelect(carrierQueryInnerDTO, organizationId);
        if (carriers.isEmpty()) {
            return map;
        }
        for (Carrier carrier : carriers) {
            CarrierCO carrierCO = new CarrierCO();
            carrierCO.setCarrierCode(carrier.getCarrierCode());
            carrierCO.setCarrierName(carrier.getCarrierName());
            carrierCO.setActiveFlag(carrier.getActiveFlag());
            carrierCO.setPriority(carrier.getPriority());
            carrierCO.setCarrierTypeCode(carrier.getCarrierTypeCode());
            map.put(carrier.getCarrierCode(), carrierCO);
        }
        return map;
    }

    @Override
    public Map<String, CarrierMappingCO> listCarrierMappings(CarrierMappingQueryInnerDTO queryInnerDTO, Long organizationId) {
        queryInnerDTO.setTenantId(organizationId);
        Map<String, CarrierMappingCO> map = new HashMap<>(16);
        List<CarrierMapping> list = carrierMappingRepository.listCarrierMappings(queryInnerDTO);
        if (list.isEmpty()) {
            return map;
        }
        List<CarrierMappingCO> cos = CarrierConverter.poToCoListObjects(list);
        for (CarrierMappingCO carrier : cos) {
            map.put(carrier.getCarrierCode(), carrier);
        }
        return map;
    }

    @Override
    public Map<String, CarrierCO> importListCarriers(Long organizationId) {
        Map<String, CarrierCO> map = new HashMap<>(16);
        Carrier carrier = new Carrier();
        carrier.setTenantId(organizationId);
        List<Carrier> carriers = carrierRepository.listCarrier(carrier);
        if (carriers.isEmpty()) {
            return map;
        }
        for (Carrier carri : carriers) {
            CarrierCO carrierVO = new CarrierCO();
            carrierVO.setCarrierCode(carri.getCarrierCode());
            carrierVO.setCarrierName(carri.getCarrierName());
            carrierVO.setActiveFlag(carri.getActiveFlag());
            if (Objects.nonNull(carri.getCarrierName()) && !carri.getCarrierName().trim().equals("")) {
                map.put(carri.getCarrierName(), carrierVO);
            }
        }
        return map;
    }

    @Override
    @Deprecated
    public List<CarrierLogisticsCostCO> calculateLogisticsCost(CarrierLogisticsCostDTO carrierLogisticsCostDTO) {

        // TODO 该方法已废弃移除

        // 1. 数据初始化
        List<CarrierLogisticsCostCO> carrierLogisticsCostList = new ArrayList<>();
        // 查询运费模板信息
        CarrierFreightDTO carrierFreightDTO = new CarrierFreightDTO();
        carrierFreightDTO.setCarrierCodeList(carrierLogisticsCostDTO.getAlternateCarrierList());
        carrierFreightDTO.setTenantId(carrierLogisticsCostDTO.getTenantId());
        List<CarrierLogisticsCostBO> carrierLogisticsCostBOList = Collections.emptyList();

        if (CollectionUtils.isEmpty(carrierLogisticsCostBOList)) {
            return carrierLogisticsCostList;
        }

        if (log.isDebugEnabled()) {
            log.debug("[calculateLogisticsCost] carrierLogisticsCostBOList:{}", JsonHelper.collectionToString(carrierLogisticsCostBOList));
        }

        Map<String, CarrierLogisticsCostBO> logisticsCostMap = carrierLogisticsCostBOList.stream()
                .collect(Collectors.toMap(CarrierLogisticsCostBO::getCarrierCode, Function.identity()));

        // 循环计算
        for (String carrierCode : carrierLogisticsCostDTO.getAlternateCarrierList()) {
            CarrierLogisticsCostBO carrierLogisticsCostBO = logisticsCostMap.get(carrierCode);
            if (ObjectUtils.isNotEmpty(carrierLogisticsCostBO)) {
                // 数据处理
                preProcess(carrierLogisticsCostBO, carrierLogisticsCostDTO.getAddress().getCityCode());
                // 扩展点(目前产品不支持单位转换,仅支持以重量计价且单位默认为千克与立方厘米。此处是预留的扩展点,
                // 后续如有其它的方式,可扩展改方法进行单位转换（转换为标准的千克与立方厘米）)
                unitConvert(carrierLogisticsCostBO, carrierLogisticsCostDTO);
                // 重量维度计算
                BigDecimal weightCost = weightCostCalculate(carrierLogisticsCostBO, carrierLogisticsCostDTO.getSkuTotalWeight());
                // 重炮比维度计算
                BigDecimal volumeCost = volumeCostCalculate(carrierLogisticsCostBO, carrierLogisticsCostDTO);
                // 比较选出较大的物流成本
                BigDecimal result;
                if (weightCost.compareTo(volumeCost) > 0) {
                    result = weightCost;
                } else {
                    result = volumeCost;
                }
                // 更新结果
                CarrierLogisticsCostCO carrierLogisticsCost = new CarrierLogisticsCostCO();
                carrierLogisticsCost.setCarrierCode(carrierCode);
                carrierLogisticsCost.setLogisticsCost(result);
                carrierLogisticsCostList.add(carrierLogisticsCost);
            }

        }
        return carrierLogisticsCostList;
    }

    @Override
    public List<CarrierDeliveryRangeCO> checkDeliveryRange(CarrierDeliveryRangeDTO carrierDeliveryRangeDTO) {
        List<CarrierDeliveryRangeCO> carrierDeliveryRangeList = new ArrayList<>();
        // 判断可送达范围
        List<Carrier> carriers = carrierRepository.selectByCondition(Condition.builder(Carrier.class)
                .andWhere(Sqls.custom().andIn(Carrier.FIELD_CARRIER_CODE, carrierDeliveryRangeDTO.getAlternateCarrierList())
                        .andEqualTo(Carrier.FIELD_TENANT_ID, carrierDeliveryRangeDTO.getTenantId())).build());

        for (Carrier carrier : carriers) {
            CarrierDeliveryRangeCO carrierDeliveryRange = new CarrierDeliveryRangeCO();
            carrierDeliveryRange.setCarrierCode(carrier.getCarrierCode());
            if (CarrierConstants.CarrierDeliveryRegionType.NATIONWIDE.equals(carrier.getDeliveryRegionTypeCode())) {
                carrierDeliveryRange.setDeliveryFlag(BaseConstants.Flag.YES);
                carrierDeliveryRangeList.add(carrierDeliveryRange);
            } else if (CarrierConstants.CarrierDeliveryRegionType.CUSTOM_RANGE.equals(carrier.getDeliveryRegionTypeCode())) {
                // 自定义地区判断
                // 注意此处地址必须进行深拷贝，后续会变更地址，但不能影响其它承运商的判断
                ReceiveAddressDTO tempAddress = carrierDeliveryRangeDTO.getAddress().copy();
                // 递归调用
                carrierDeliveryRange.setDeliveryFlag(customCheckDeliveryFlag(tempAddress, carrier, BaseConstants.Digital.TWO));
                carrierDeliveryRangeList.add(carrierDeliveryRange);
            }
        }
        return carrierDeliveryRangeList;
    }

    /**
     * 判断承运商是否可送达（递归）
     *
     * @param tempAddress 收货地址
     * @param carrier     承运商信息
     * @param count       递归辅助标志,(2:省市区，1:省市，0:省，-1: 不存在不可送达范围,承运商可送达
     * @return 结果
     */
    protected int customCheckDeliveryFlag(ReceiveAddressDTO tempAddress, Carrier carrier, int count) {

        if (count < 0) {
            return 1;
        }
        if (checkAddressRange(tempAddress, carrier) == 0) {
            return 0;
        }
        if (BaseConstants.Digital.TWO == count) {
            tempAddress.setDistrictCode(null);
        } else if (BaseConstants.Digital.ONE == count) {
            tempAddress.setCityCode(null);
        }
        return customCheckDeliveryFlag(tempAddress, carrier, --count);
    }

    protected int checkAddressRange(ReceiveAddressDTO tempAddress, Carrier carrier) {

        int result = carrierCantDeliveryRepository.selectCountByCondition(Condition.builder(CarrierCantDelivery.class)
                .andWhere(Sqls.custom()
                        .andEqualTo(CarrierCantDelivery.FIELD_CARRIER_CODE, carrier.getCarrierCode())
                        .andEqualTo(CarrierCantDelivery.FIELD_TENANT_ID, carrier.getTenantId())
                        .andEqualTo(CarrierCantDelivery.FIELD_COUNTRY_CODE, tempAddress.getCountryCode())
                        .andEqualTo(CarrierCantDelivery.FIELD_REGION_CODE, tempAddress.getRegionCode())
                        .andEqualTo(CarrierCantDelivery.FIELD_CITY_CODE, tempAddress.getCityCode())
                        .andEqualTo(CarrierCantDelivery.FIELD_DISTRICT_CODE, tempAddress.getDistrictCode()))
                .build());

        if (result > 0) {
            // 不可送
            return 0;
        }
        // 可送
        return 1;
    }

    protected void preProcess(CarrierLogisticsCostBO carrierLogisticsCostBO, String regionCode) {
        Optional<CarrierLogisticsCostDetailBO> costDetailOptional = carrierLogisticsCostBO.getCarrierLogisticsCostDetailList()
                .stream().filter(v -> StringUtils.equals(regionCode, v.getRegionCode())).findFirst();
        if (costDetailOptional.isPresent()) {
            // 存在特殊区域
            carrierLogisticsCostBO.setCarrierLogisticsCostDetailBO(costDetailOptional.get());
        } else {
            // 不存在特殊区域
            costDetailOptional = carrierLogisticsCostBO.getCarrierLogisticsCostDetailList()
                    .stream().filter(v -> StringUtils.isBlank(v.getRegionCode())).findFirst();
            costDetailOptional.ifPresent(carrierLogisticsCostBO::setCarrierLogisticsCostDetailBO);
        }
    }

    protected void unitConvert(CarrierLogisticsCostBO carrierLogisticsCostBO, CarrierLogisticsCostDTO carrierLogisticsCostDTO) {
        // 产品运费模板单位默认为千克与立方厘米,项目上若非以上单位需要进行重写改方法
    }

    protected BigDecimal weightCostCalculate(CarrierLogisticsCostBO carrierLogisticsCostBO, BigDecimal weight) {

        BigDecimal cost;
        CarrierLogisticsCostDetailBO carrierLogisticsCostDetailBO = carrierLogisticsCostBO.getCarrierLogisticsCostDetailBO();

        if (weight.compareTo(carrierLogisticsCostDetailBO.getFirstPieceWeight()) > 0) {
            // 大于首重
            BigDecimal nextCost =
                    weight.subtract(carrierLogisticsCostDetailBO.getFirstPieceWeight()).divide(carrierLogisticsCostDetailBO.getNextPieceWeight(),
                                    2, RoundingMode.UP)
                    .multiply(carrierLogisticsCostDetailBO.getNextPrice()).setScale(2, RoundingMode.UP);
            cost = carrierLogisticsCostDetailBO.getFirstPrice().add(nextCost);
        } else {
            // 小于等于首重
            cost = carrierLogisticsCostDetailBO.getFirstPrice();
        }
        return cost;
    }

    protected BigDecimal volumeCostCalculate(CarrierLogisticsCostBO carrierLogisticsCostBO, CarrierLogisticsCostDTO carrierLogisticsCostDTO) {
        BigDecimal weight = carrierLogisticsCostDTO.getSkuTotalVolume().divide(BigDecimal.valueOf(carrierLogisticsCostBO.getVolumeConversion()), 2,
                RoundingMode.UP);
        return weightCostCalculate(carrierLogisticsCostBO, weight);

    }
}
