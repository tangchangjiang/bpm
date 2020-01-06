local posLimitKey = 'o2md:pos:express:limit';
local posCacheKey = 'o2md:pos:' .. ARGV[2] ..':'.. ARGV[1];

local pushLimit = tonumber(redis.call('hincrBy', posCacheKey, 'express_limit_quantity', '0'));
redis.call('hset', posCacheKey, 'express_limit_value', '0');
if (pushLimit > 0) then
    redis.call('srem', posLimitKey, ARGV[2] ..':'.. ARGV[1]);
else
    redis.call('sadd', posLimitKey, ARGV[2] ..':'.. ARGV[1]);
end