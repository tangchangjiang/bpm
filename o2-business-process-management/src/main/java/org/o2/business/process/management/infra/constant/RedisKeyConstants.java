package org.o2.business.process.management.infra.constant;

import org.springframework.core.io.ClassPathResource;
import org.springframework.scripting.support.ResourceScriptSource;

/**
 *
 * redis key 管理
 * @author zhilin.ren@hand-china.com
 * @date 2022/08/10 16:18
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

        ResourceScriptSource UPDATE_REFUND_LUA =
                new ResourceScriptSource(new ClassPathResource("script/lua/list_process_node_status.lua"));
    }

    interface BusinessProcess{
        String BUSINESS_PROCESS_KEY = "o2bpm:process:{%d}";


        /**
         * 获取业务流程redisKey
         * @param tenantId 租户id
         * @return 业务流程redisKey
         */
        static String getBusinessProcessKey(Long tenantId) {
            return String.format(BUSINESS_PROCESS_KEY, tenantId);
        }

    }

}
