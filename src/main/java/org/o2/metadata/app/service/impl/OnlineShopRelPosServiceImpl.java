package org.o2.metadata.app.service.impl;

import org.apache.commons.lang3.StringUtils;
import org.hzero.core.base.BaseConstants;
import org.hzero.core.base.BaseConstants.Flag;
import org.o2.boot.metadata.constants.MetadataConstants;
import org.o2.metadata.app.service.OnlineShopRelPosService;
import org.o2.metadata.domain.entity.OnlineShop;
import org.o2.metadata.domain.entity.OnlineShopRelPos;
import org.o2.metadata.domain.entity.Pos;
import org.o2.metadata.domain.repository.OnlineShopRelPosRepository;
import org.o2.metadata.domain.repository.OnlineShopRepository;
import org.o2.metadata.domain.repository.PosRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.*;

//import org.o2.boot.inv.trigger.app.service.ShopRelPosTriggerService;

/**
 * 网店关联服务点应用服务默认实现
 *
 * @author tingting.wang@hand-china.com 2019-3-25
 */
@Service
public class OnlineShopRelPosServiceImpl implements OnlineShopRelPosService {
    private final OnlineShopRelPosRepository onlineShopRelPosRepository;
    private final OnlineShopRepository onlineShopRepository;
    private final PosRepository posRepository;
//    private final ShopRelPosTriggerService shopRelPosTriggerService;

    private static final Logger LOG = LoggerFactory.getLogger(OnlineShopRelPosServiceImpl.class);

    public OnlineShopRelPosServiceImpl(final OnlineShopRelPosRepository onlineShopRelPosRepository,
                                       final OnlineShopRepository onlineShopRepository,
                                       final PosRepository posRepository
                                       //TODO 库存相关暂时注释
//            , final ShopRelPosTriggerService shopRelPosTriggerService
    ) {
        this.onlineShopRelPosRepository = onlineShopRelPosRepository;
        this.onlineShopRepository = onlineShopRepository;
        this.posRepository = posRepository;
//        this.shopRelPosTriggerService = shopRelPosTriggerService;
    }

    @Override
    public List<OnlineShopRelPos> batchInsertSelective(final List<OnlineShopRelPos> relationships) {
        relationships.forEach(relationship -> {
            Assert.isTrue(!relationship.exist(onlineShopRelPosRepository), BaseConstants.ErrorCode.DATA_EXISTS);
            relationship.baseValidate(onlineShopRepository, posRepository);
            relationship.setBusinessActiveFlag(getIsInvCalculated(relationship));
        });
        return onlineShopRelPosRepository.batchInsertSelective(relationships);
    }

    @Override
    public List<OnlineShopRelPos> batchUpdateByPrimaryKey(final List<OnlineShopRelPos> relationships) {
        relationships.forEach(relationship -> {
            Assert.isTrue(relationship.exist(onlineShopRelPosRepository), BaseConstants.ErrorCode.DATA_NOT_EXISTS);
            relationship.baseValidate(onlineShopRepository, posRepository);
            relationship.setBusinessActiveFlag(getIsInvCalculated(relationship));
        });
        return onlineShopRelPosRepository.batchUpdateByPrimaryKey(relationships);
    }

    @Override
    public List<OnlineShopRelPos> resetIsInvCalculated(final String onlineShopCode, final String posCode) {
        OnlineShop onlineShop = null;
        Pos pos = null;

        //分别获取OnlineShop和Pos
        if (StringUtils.isNotEmpty(onlineShopCode)) {
            final List<OnlineShop> list = onlineShopRepository.select(OnlineShop.FIELD_ONLINE_SHOP_CODE, onlineShopCode);
            if (list.size() == 1) {
                onlineShop = list.get(0);
            }
        }

        if (StringUtils.isNotEmpty(posCode)) {
            final List<Pos> list = posRepository.select(Pos.FIELD_POS_CODE, posCode);
            if (list.size() == 1) {
                pos = list.get(0);
            }
        }

        if (onlineShop == null && pos == null) {
            LOG.error("onlineShop 和 pos 有且仅能传一个值");
            return Collections.emptyList();
        } else if (onlineShop != null && pos != null) {
            LOG.error("onlineShop 和 pos 有且仅能传一个值");
            return Collections.emptyList();
        }

        //查询对应的onlineShopRelPos
        final OnlineShopRelPos onlineShopRelPos = new OnlineShopRelPos();
        if (onlineShop != null) {
            onlineShopRelPos.setOnlineShopId(onlineShop.getOnlineShopId());
        }
        if (pos != null) {
            onlineShopRelPos.setPosId(pos.getPosId());
        }

        //进行更新数据
        final List<OnlineShopRelPos> onlineShopRelPosList = onlineShopRelPosRepository.select(onlineShopRelPos);
        final List<OnlineShopRelPos> toUpdateList = new ArrayList<>(onlineShopRelPosList.size());

        int oldValue;
        int newValue;
        for (final OnlineShopRelPos relPos : onlineShopRelPosList) {
            oldValue = relPos.getBusinessActiveFlag();
            newValue = getIsInvCalculated(relPos);
            if (oldValue != newValue) {
                relPos.setBusinessActiveFlag(newValue);
                toUpdateList.add(relPos);
            }
        }
        onlineShopRelPosRepository.batchUpdateByPrimaryKey(toUpdateList);

        //触发网店计算库存
        if (onlineShop != null) {
//            shopRelPosTriggerService.triggerByShopRelPos(onlineShopCode);
        } else {
            final Set<String> toUpdateCodeSet = new HashSet<>(toUpdateList.size());
            OnlineShop shop;
            for (final OnlineShopRelPos o : toUpdateList) {
                shop = onlineShopRepository.selectByPrimaryKey(o.getOnlineShopId());
                if (shop != null) {
                    toUpdateCodeSet.add(shop.getOnlineShopCode());
                }
//                shopRelPosTriggerService.triggerByShopRelPos(new ArrayList<>(toUpdateCodeSet));
            }
        }
        return onlineShopRelPosList;
    }

    private int getIsInvCalculated(final OnlineShopRelPos onlineShopRelPos) {
        if (onlineShopRelPos == null) {
            return 0;
        }

        final OnlineShop onlineShop = onlineShopRepository.selectByPrimaryKey(onlineShopRelPos.getOnlineShopId());
        final Pos pos = posRepository.selectByPrimaryKey(onlineShopRelPos.getPosId());
        return getIsInvCalculated(onlineShopRelPos, onlineShop, pos);
    }


    /**
     * * 满足以下条件，返回1
     * 1.网店关联POS有效
     * 2.网店有效
     * 3.POS状态为正常
     * 4.如果POS为门店，需要满足POS可快递发货且接单量未达到上限
     *
     * @return 判断结果
     */
    private int getIsInvCalculated(final OnlineShopRelPos onlineShopRelPos, final OnlineShop onlineShop, final Pos pos) {
        if (onlineShopRelPos == null || Flag.NO.equals(onlineShopRelPos.getActiveFlag())) {
            return 0;
        }

        if (onlineShop == null || Flag.NO.equals(onlineShop.getActiveFlag())) {
            return 0;
        }

        if (pos == null || !MetadataConstants.PosStatus.NORMAL.equals(pos.getPosStatusCode())) {
            return 0;
        }

        return 1;
    }
}
