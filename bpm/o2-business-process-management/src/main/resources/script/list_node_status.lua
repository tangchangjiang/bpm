-- 批量查询节点状态
-- KEYS[1] : o2bpm:node:{tenantId}


local detailKey = KEYS[1];
local keys = ARGV;
local map = {};

for i, v in pairs(keys) do
    local temp = redis.call("HGET", detailKey, v);
    if temp then
        map[v] = temp;
    end
end

return cjson.encode(map);





