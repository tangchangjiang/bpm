package org.o2.metadata.app.service.impl;

import org.hzero.mybatis.helper.SecurityTokenHelper;
import org.o2.ext.metadata.app.service.OnlineShopInfAuthService;
import org.o2.ext.metadata.domain.entity.OnlineShopInfAuth;
import org.o2.ext.metadata.domain.repository.OnlineShopInfAuthRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 网店接口表应用服务默认实现
 *
 * @author tingting.wang@hand-china.com 2019-3-25
 */
@Service
public class OnlineShopInfAuthServiceImpl implements OnlineShopInfAuthService {

    @Autowired
    private OnlineShopInfAuthRepository onlineShopInfAuthRepository;

    @Override
    public OnlineShopInfAuth updateOrInsert(final OnlineShopInfAuth onlineShopInfAuth) {
        if (onlineShopInfAuth.getOnlineShopId() != null) {
            final List<OnlineShopInfAuth> onlineShopInfAuthList = onlineShopInfAuthRepository.select(OnlineShopInfAuth.FIELD_ONLINE_SHOP_ID, onlineShopInfAuth.getOnlineShopId());
            if (onlineShopInfAuthList != null && onlineShopInfAuthList.size() > 0) {
                onlineShopInfAuth.setOnlineShopInfAuthId(onlineShopInfAuthList.get(0).getOnlineShopInfAuthId());
                SecurityTokenHelper.validToken(onlineShopInfAuth);
                onlineShopInfAuthRepository.updateByPrimaryKeySelective(onlineShopInfAuth);
                return onlineShopInfAuth;
            }
        }
        onlineShopInfAuthRepository.insertSelective(onlineShopInfAuth);
        return onlineShopInfAuth;
    }
}
