local warehouseLimitKey = KEYS[1];
local warehouseCacheKey = 'o2md:warehouse:' .. ARGV[3] ..':'.. ARGV[1];

redis.call('hset', warehouseCacheKey, 'pick_up_limit_quantity', ARGV[2])
local pushLimit = tonumber(ARGV[2]);
local pushValue = tonumber(redis.call('hincrBy', warehouseCacheKey, 'pick_up_limit_value', '0'));
if (pushLimit > pushValue) then
    redis.call('srem', warehouseLimitKey, ARGV[3] ..':'.. ARGV[1]);
else
    redis.call('sadd', warehouseLimitKey, ARGV[3] ..':'.. ARGV[1]);
end