package org.o2.business.process.management.infra.constant;

import org.springframework.core.io.ClassPathResource;
import org.springframework.scripting.support.ResourceScriptSource;

/**
 *
 * redis key 管理
 * @author zhilin.ren@hand-china.com
 * @date 2022/08/10 16:18
 */
public interface BusinessProcessRedisConstants {

    String LUA_NULL_MAP = "{}";

    interface BusinessProcessLua{
        ResourceScriptSource LIST_PROCESS_NODE_STATUS =
                new ResourceScriptSource(new ClassPathResource("script/list_node_status.lua"));

        ResourceScriptSource BUSINESS_PROCESS_TENANT_INITIALIZE_LUA =
                new ResourceScriptSource(new ClassPathResource("script/business_process_tenant_init.lua"));

        ResourceScriptSource BUSINESS_PROCESS_CONFIG_UPDATE_LUA =
                new ResourceScriptSource(new ClassPathResource("script/process_config_update.lua"));

        ResourceScriptSource BUSINESS_PROCESS_CONFIG_BATCH_UPDATE_LUA =
                new ResourceScriptSource(new ClassPathResource("script/process_config_batch_update.lua"));
    }

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

    interface BusinessProcess{
        String BUSINESS_PROCESS_KEY = "o2bpm:process:{%d}";

        String PROCESS_LAST_MODIFIED_TIME_KEY = "o2bpm:process:{%d}:last_modified_time";

        /**
         * 获取业务流程redisKey
         * @param tenantId 租户id
         * @return 业务流程redisKey
         */
        static String getBusinessProcessKey(Long tenantId) {
            return String.format(BUSINESS_PROCESS_KEY, tenantId);
        }


        /**
         * 获取业务最后更新时间key
         * @param tenantId
         * @return
         */
        static String getProcessLastModifiedTimeKey(Long tenantId) {
            return String.format(PROCESS_LAST_MODIFIED_TIME_KEY, tenantId);
        }
    }

}