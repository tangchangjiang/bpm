local warehouseLimitKey = KEYS[1];
--local warehouseCacheKey = 'o2md:warehouse:' .. ARGV[2] ..':'.. ARGV[1];
local warehouseCacheKey = ARGV[3];

local pushLimit = tonumber(redis.call('hincrBy', warehouseCacheKey, 'express_limit_quantity', '0'));
redis.call('hset', warehouseCacheKey, 'express_limit_value', '0');
if (pushLimit > 0) then
    redis.call('srem', warehouseLimitKey, ARGV[1]);
else
    redis.call('sadd', warehouseLimitKey, ARGV[1]);
end