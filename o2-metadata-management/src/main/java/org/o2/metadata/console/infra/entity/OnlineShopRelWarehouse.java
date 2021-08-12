package org.o2.metadata.console.infra.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Preconditions;
import io.choerodon.mybatis.annotation.ModifyAudit;
import io.choerodon.mybatis.annotation.VersionAudit;
import io.choerodon.mybatis.domain.AuditDomain;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.commons.collections4.CollectionUtils;
import org.o2.core.helper.FastJsonHelper;
import org.o2.data.redis.client.RedisCacheClient;
import org.o2.metadata.console.api.vo.OnlineShopRelWarehouseVO;
import org.o2.metadata.console.infra.constant.OnlineShopConstants;
import org.o2.metadata.console.infra.repository.OnlineShopRelWarehouseRepository;
import org.o2.metadata.console.infra.repository.OnlineShopRepository;
import org.o2.metadata.console.infra.repository.WarehouseRepository;
import org.o2.metadata.console.infra.constant.MetadataConstants;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.scripting.support.ResourceScriptSource;
import org.springframework.util.Assert;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 网店关联仓库
 *
 * @author yuying.shi@hand-china.com 2020-03-02
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel("网店关联仓库")
@VersionAudit
@ModifyAudit
@Table(name = "o2md_online_shop_rel_warehouse")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class OnlineShopRelWarehouse extends AuditDomain {

    public static final String FIELD_ONLINE_SHOP_REL_WAREHOUSE_ID = "onlineShopRelWarehouseId";
    public static final String FIELD_ONLINE_SHOP_ID = "onlineShopId";
    public static final String FIELD_POS_ID = "posId";
    public static final String FIELD_WAREHOUSE_ID = "warehouseId";
    public static final String FIELD_ACTIVE_FLAG = "activeFlag";
    public static final String FIELD_BUSINESS_ACTIVE_FLAG = "businessActiveFlag";
    public static final String FIELD_TENANT_ID = "tenantId";

    //
    // 数据库字段
    // ------------------------------------------------------------------------------

    @ApiModelProperty("表ID，主键，供其他表做外键")
    @Id
    @GeneratedValue
    private Long onlineShopRelWarehouseId;

    @ApiModelProperty(value = "网店id，关联o2md_online_shop.online_shop_id")
    @NotNull
    private Long onlineShopId;

    @ApiModelProperty(value = "服务点id,关联o2md_pos.pos_id")
    @NotNull
    private Long posId;

    @ApiModelProperty(value = "仓库id，关联o2md_warehouse.warehouse_id")
    @NotNull
    private Long warehouseId;

    @ApiModelProperty("是否有效")
    @NotNull
    @Max(1)
    @Min(0)
    private Integer activeFlag;

    @ApiModelProperty("是否业务有效（寻源，库存计算 判断关联关系）")
    @NotNull
    @Max(1)
    @Min(0)
    private Integer businessActiveFlag;


    @ApiModelProperty(value = "租户ID")
    @NotNull
    private Long tenantId;

    @ApiModelProperty(value = "仓库编码")
    @Transient
    private String warehouseCode;

    @Transient
    private String warehouseStatus;

    @Transient
    private String onlineShopCode;

    //
    // 业务方法(按public protected private顺序排列)
    // ------------------------------------------------------------------------------

    public boolean exist(final OnlineShopRelWarehouseRepository relPosRepository) {
        if (this.onlineShopRelWarehouseId != null) {
            return relPosRepository.existsWithPrimaryKey(this);
        }
        final OnlineShopRelWarehouse rel = new OnlineShopRelWarehouse();
        rel.setPosId(this.posId);
        rel.setWarehouseId(this.warehouseId);
        rel.setOnlineShopId(this.onlineShopId);
        return relPosRepository.selectCount(rel) > 0;
    }

    public void baseValidate(final OnlineShopRepository shopRepository, final WarehouseRepository warehouseRepository) {
        Assert.notNull(this.posId, "pos id must not null");
        Assert.notNull(this.warehouseId, "warehouseId id must not null");
        Assert.isTrue(warehouseRepository.existsWithPrimaryKey(this.warehouseId), "associate warehouse must exist");
        Assert.notNull(this.onlineShopId, "online shop id must not null");
        Assert.isTrue(shopRepository.existsWithPrimaryKey(this.onlineShopId), "associate online shop must exist");
        Preconditions.checkArgument(null != this.tenantId, MetadataConstants.ErrorCode.BASIC_DATA_TENANT_ID_IS_NULL);
    }

    /**
     * 拼写RedisHashMap
     * @param onlineShopRelWarehouses onlineShopRelWarehouseVOs
     * @return map
     */
    public Map<String, Object> buildRedisHashMap(List<OnlineShopRelWarehouseVO> onlineShopRelWarehouses) {
       Map<String,Object> map = new HashMap<>(4);
        onlineShopRelWarehouses.forEach(onlineShopRelWarehouseVO ->
                map.put(onlineShopRelWarehouseVO.getWarehouseCode(),onlineShopRelWarehouseVO.getActiveFlag()));
       return map;
    }

    /**
     * 拼写hashKey
     * @param onlineShopCode onlineShopCode
     * @return hashKey
     */
    public String buildRedisHashKey(final String onlineShopCode) {
        return  String.format(OnlineShopConstants.Redis.KEY_ONLINE_SHOP_REL_WAREHOUSE, this.tenantId,onlineShopCode);
    }

    /**
     * 组黄map
     * @param onlineShopRelWarehouseVOList onlineShopRelWarehouseVOList
     * @return map
     */
    public Map<Integer, List<OnlineShopRelWarehouseVO>> groupMap (List<OnlineShopRelWarehouseVO> onlineShopRelWarehouseVOList) {
        return onlineShopRelWarehouseVOList.stream().collect(Collectors.groupingBy(
                onlineShopRelWarehouseVO -> onlineShopRelWarehouseVO.getActiveFlag() == 1 ? 1 : 0
        ));
    }


    /**
     * 同步到redis
     * @param onlineShopRelWarehouseVOList  onlineShopRelWarehouseVOList
     * @param saveResourceScriptSource      saveResourceScriptSource
     * @param deleteResourceScriptSource    deleteResourceScriptSource
     * @param redisCacheClient              redisCacheClient
     */
    public void syncToRedis (final List<OnlineShopRelWarehouseVO> onlineShopRelWarehouseVOList,
                             final ResourceScriptSource saveResourceScriptSource,
                             final ResourceScriptSource deleteResourceScriptSource,
                             final RedisCacheClient redisCacheClient) {
        // 根据是否有效分组 无效 删除
        Map<Integer, List<OnlineShopRelWarehouseVO>> onlineShopRelWarehouseMap = this.groupMap(onlineShopRelWarehouseVOList);
        for (Map.Entry<Integer, List<OnlineShopRelWarehouseVO>> onlineShopRelWarehouseEntry : onlineShopRelWarehouseMap.entrySet()){
            // 根据 onlineShopCode 分组
            Map<String, List<OnlineShopRelWarehouseVO>>  synToRedisMap = onlineShopRelWarehouseEntry.getValue().stream()
                    .collect(Collectors.groupingBy(OnlineShopRelWarehouseVO::getOnlineShopCode));

            for (Map.Entry<String, List<OnlineShopRelWarehouseVO>> onlineShopRelWhEntry : synToRedisMap.entrySet()) {
                List<OnlineShopRelWarehouseVO> groupOnlineShopRelWarehouseVos = onlineShopRelWhEntry.getValue();
                if (CollectionUtils.isNotEmpty(groupOnlineShopRelWarehouseVos)) {
                    // 获取hashKey
                    final String hashKey = this.buildRedisHashKey(groupOnlineShopRelWarehouseVos.get(0).getOnlineShopCode());
                    if (onlineShopRelWarehouseEntry.getKey() == 1) {
                        try {
                            redisCacheClient.opsForHash().putAll(hashKey, new ObjectMapper().readValue(FastJsonHelper.objectToString(this.buildRedisHashMap(groupOnlineShopRelWarehouseVos)), new TypeReference<Map<String, String>>() {
                            }));
                        } catch (IOException e) {
                        }
                    } else {
                        redisCacheClient.opsForHash().delete(hashKey, groupOnlineShopRelWarehouseVos.stream().map(OnlineShopRelWarehouseVO::getWarehouseCode).toArray());
                    }
                }
            }

        }
    }

    /**
     *  operation redis
     * @param filedMaps filedMaps
     * @param keyList   keyList
     */
    public void executeScript(final Map<String, Map<String, Object>> filedMaps,
                              final List<String> keyList,
                              final ResourceScriptSource resourceScriptSource,
                              final RedisCacheClient redisCacheClient) {
        final DefaultRedisScript<Boolean> defaultRedisScript = new DefaultRedisScript<>();
        defaultRedisScript.setScriptSource(resourceScriptSource);
        redisCacheClient.execute(defaultRedisScript,keyList, FastJsonHelper.mapToString(filedMaps));
    }
}
