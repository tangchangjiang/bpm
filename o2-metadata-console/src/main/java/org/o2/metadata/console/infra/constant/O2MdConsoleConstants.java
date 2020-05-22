package org.o2.metadata.console.infra.constant;

import org.springframework.core.io.ClassPathResource;
import org.springframework.scripting.support.ResourceScriptSource;

/**
 * 元数据 console常量
 * @author yuying.shi@hand-china.com 2020/3/24
 */
public interface O2MdConsoleConstants {

    interface LuaCode {

        ResourceScriptSource BATCH_SAVE_WAREHOUSE_REDIS_HASH_VALUE_LUA =
                new ResourceScriptSource(new ClassPathResource("script/lua/batch_save_warehouse_redis_hash_value.lua"));

        ResourceScriptSource BATCH_SAVE_REDIS_HASH_VALUE_LUA =
                new ResourceScriptSource(new ClassPathResource("script/lua/batch_save_redis_hash_value.lua"));

        ResourceScriptSource BATCH_DELETE_SHOP_REL_WH_REDIS_HASH_VALUE_LUA =
                new ResourceScriptSource(new ClassPathResource("script/lua/batch_delete_shopRelWh_redis_hash_value.lua"));

        ResourceScriptSource BATCH_DELETE_REDIS_HASH_VALUE_LUA =
                new ResourceScriptSource(new ClassPathResource("script/lua/batch_delete_redis_hash_value.lua"));

        ResourceScriptSource BATCH_UPDATE_REDIS_HASH_VALUE_LUA =
                new ResourceScriptSource(new ClassPathResource("script/lua/batch_update_redis_hash_value.lua"));
    }

    interface  Path {
       String FILE = "file";

       String REGION ="region";

       String ZH_CN ="zh_CN";

       String EN_US ="en_US";

       String FILE_NAME = "country-region";

    }

    public interface FileSuffix {
        String JSON = ".json";
    }

    interface  Status {
        String UPDATE = "update";
        String CREATE = "create";
    }
}
