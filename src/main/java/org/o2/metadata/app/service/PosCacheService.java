package org.o2.metadata.app.service;

import java.util.Set;

/**
 * 门店缓存服务
 *
 * @author mark.bao@hand-china.com 2019-04-16
 */
public interface PosCacheService {

    /**
     * 保存门店快递配送接单量限制
     *
     * @param posCode         门店编码
     * @param expressQuantity 快递配送接单量限制
     */
    void saveExpressQuantity(String posCode, String expressQuantity);

    /**
     * 保存门店自提接单量限制
     *
     * @param posCode        门店编码
     * @param pickUpQuantity 自提单量限制
     */
    void savePickUpQuantity(String posCode, String pickUpQuantity);

    /**
     * 门店快递配送接单量增量更新
     *
     * @param posCode   门店编码
     * @param increment 快递配送接单量增量
     */
    void updateExpressValue(String posCode, String increment);

    /**
     * 门店自提接单量增量更新
     *
     * @param posCode   门店编码
     * @param increment 自提单量增量
     */
    void updatePickUpValue(String posCode, String increment);

    /**
     * 获取快递配送接单量限制
     *
     * @param posCode 门店编码
     * @return 快递配送接单量限制
     */
    String getExpressLimit(String posCode);

    /**
     * 获取自提接单量限制
     *
     * @param posCode 门店编码
     * @return 自提接单量限制
     */
    String getPickUpLimit(String posCode);

    /**
     * 获取实际快递配送接单量
     *
     * @param posCode 门店编码
     * @return 实际快递配送接单量
     */
    String getExpressValue(String posCode);

    /**
     * 获取实际自提接单量
     *
     * @param posCode 门店编码
     * @return 实际自提接单量
     */
    String getPickUpValue(String posCode);

    /**
     * 门店缓存KEY
     *
     * @param posCode 门店编码
     * @return 门店缓存KEY
     */
    String posCacheKey(String posCode);

    /**
     * 是否门店快递配送接单量到达上限
     *
     * @param posCode 门店编码
     * @return 结果(true : 到达上限)
     */
    boolean isPosExpressLimit(String posCode);

    /**
     * 是否门店自提接单量到达上限
     *
     * @param posCode 门店编码
     * @return 结果(true : 到达上限)
     */
    boolean isPosPickUpLimit(String posCode);

    /**
     * 获取快递配送接单量到达上限的门店
     *
     * @return 快递配送接单量到达上限的门店集合
     */
    Set<String> expressLimitPosCollection();

    /**
     * 获取自提接单量到达上限的门店
     *
     * @return 自提接单量到达上限的门店集合
     */
    Set<String> pickUpLimitPosCollection();

    /**
     * 重置门店快递配送接单量值
     *
     * @param posCode      门店编码
     */
    void resetPosExpressLimit(String posCode);

    /**
     * 重置门店自提接单量限制值
     *
     * @param posCode     门店编码
     */
    void resetPosPickUpLimit(String posCode);
}
