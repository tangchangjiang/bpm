
local processDetailKey = KEYS[1];
local processTimeKey = KEYS[2];

local processMap = cjson.decode(ARGV[1]);
local updateTime = ARGV[2];

for processKey, processValue in pairs(processMap) do
    redis.call("HSET", processDetailKey, processKey, processValue);
    redis.call("hset", processTimeKey, processKey, updateTime);
end





