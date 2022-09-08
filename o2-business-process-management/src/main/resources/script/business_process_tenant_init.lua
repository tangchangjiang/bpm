-- 批量查询节点状态
-- KEYS[1] : o2bpm:node:{tenantId}


local processDetailKey = KEYS[1];
local nodeDetailKey = KEYS[2];
local processTimeKey = KEYS[3];
local processMap = cjson.decode(ARGV[1]);
local nodeMap = cjson.decode(ARGV[2]);
local updateTime = ARGV[3];

for processKey, processValue in pairs(processMap) do
    redis.call("HSET", processDetailKey, processKey, processValue);
    redis.call("hset", processTimeKey, processKey, updateTime);
end

for nodeKey, nodeValue in pairs(nodeMap) do
    redis.call("HSET", nodeDetailKey, nodeKey, nodeValue);
end





