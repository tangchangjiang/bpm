local warehouseLimitKey = KEYS[1];
--local warehouseCacheKey = 'o2md:warehouse:' .. ARGV[3] ..':'.. ARGV[1];
local warehouseCacheKey = ARGV[4];

local pushLimit = tonumber(redis.call('hincrBy', warehouseCacheKey, 'express_limit_quantity', '0'));
local pushValue = tonumber(redis.call('hincrBy', warehouseCacheKey, 'express_limit_value', ARGV[2]));
if (pushLimit > pushValue) then
    redis.call('srem', warehouseLimitKey, ARGV[1]);
else
    redis.call('sadd', warehouseLimitKey, ARGV[1]);
end