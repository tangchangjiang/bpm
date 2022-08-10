package org.o2.business.process.management.infra.constant;

/**
 *
 * redis key 管理
 * @author zhilin.ren@hand-china.com 2022/08/10 16:18
 */
public interface RedisKeyConstants {

    interface BusinessNode {
        String NODE_STATUS_KEY = "o2bpm:node:{%s}";


        /**
         * 获取节点状态redisKey
         * @param tenantId 租户id
         * @return 节点状态的key
         */
        static String getNodeStatusKey(Long tenantId) {

            return String.format(NODE_STATUS_KEY,tenantId);
        }

    }

}
