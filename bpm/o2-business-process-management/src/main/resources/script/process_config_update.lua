-- 批量查询节点状态
-- KEYS[1] : o2bpm:node:{tenantId}


local processDetailKey = KEYS[1];
local processTimeKey = KEYS[2];

local processCode = ARGV[1];
local detailJson = ARGV[2];
local updateTime = ARGV[3];

redis.call("hset", processDetailKey, processCode, detailJson);
redis.call("hset", processTimeKey, processCode, updateTime);





