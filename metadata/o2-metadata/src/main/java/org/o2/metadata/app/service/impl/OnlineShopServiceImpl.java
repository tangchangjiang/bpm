package org.o2.metadata.app.service.impl;

import org.o2.core.convert.ListConverter;
import org.o2.customer.client.O2CustomerClient;
import org.o2.customer.client.infra.co.CustomerFollowOnlineVO;
import org.o2.metadata.api.co.OnlineShopCO;
import org.o2.metadata.api.vo.OnlineShopVO;
import org.o2.metadata.app.service.OnlineShopService;
import org.o2.metadata.infra.convertor.OnlineShopConverter;
import org.o2.metadata.infra.redis.OnlineShopRedis;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

/**
 * 网店服务
 *
 * @author yipeng.zhu@hand-china.com 2021-08-05
 **/
@Service
public class OnlineShopServiceImpl implements OnlineShopService {
    private final OnlineShopRedis onlineShopRedis;
    private final O2CustomerClient o2CustomerClient;

    public OnlineShopServiceImpl(OnlineShopRedis onlineShopRedis,
                                 O2CustomerClient o2CustomerClient) {
        this.onlineShopRedis = onlineShopRedis;
        this.o2CustomerClient = o2CustomerClient;
    }

    @Override
    public OnlineShopCO getOnlineShop(String onlineShopCode) {
        return OnlineShopConverter.poToCoObject(onlineShopRedis.getOnlineShop(onlineShopCode));
    }

    @Override
    public OnlineShopVO getOnlineShopInfo(String onlineShopCode) {
        OnlineShopVO onlineShopVO = OnlineShopConverter.poToShopVO(onlineShopRedis.getOnlineShop(onlineShopCode));
        fillShopFollowInfo(onlineShopVO);
        return onlineShopVO;
    }

    @Override
    public List<OnlineShopCO> queryShopList(List<String> onlineShopCodes) {
        // TODO 添加缓存
        return ListConverter.toList(onlineShopRedis.selectShopList(onlineShopCodes), OnlineShopConverter::poToCoObject);
    }

    @Override
    public List<OnlineShopCO> queryShopListByType(Long tenantId, String onlineShopType) {
        return ListConverter.toList(onlineShopRedis.selectShopListByType(tenantId, onlineShopType), OnlineShopConverter::poToCoObject);
    }

    /**
     * 填充网店关注信息
     *
     * @param onlineShopVO 网店
     */
    protected void fillShopFollowInfo(OnlineShopVO onlineShopVO) {
        CustomerFollowOnlineVO follow = o2CustomerClient.findOnlineFollowNum(onlineShopVO.getOnlineShopCode());
        if (Objects.isNull(follow)) {
            return;
        }
        onlineShopVO.setFollowNum(follow.getQuantity());
        onlineShopVO.setFollowFlag(follow.getFollowFlag());
    }

}
