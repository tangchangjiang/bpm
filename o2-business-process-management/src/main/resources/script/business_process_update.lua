-- 批量查询节点状态
-- KEYS[1] : o2bpm:node:{tenantId}


local processDetailKey = KEYS[1];
local nodeDetailKey = KEYS[2];
local processMap = cjson.decode(ARGV[1]);
local nodeMap = cjson.decode(ARGV[2]);

for processKey, processValue in ipairs(processMap) do
    redis.call("HSET", processDetailKey, processKey, processValue);
end

for nodeKey, nodeValue in ipairs(nodeMap) do
    redis.call("HSET", nodeDetailKey, nodeKey, nodeValue);
end





