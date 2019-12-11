local posLimitKey = 'o2md:pos:express:limit';
local posCacheKey = 'o2md:pos:' .. ARGV[3] .. ARGV[1];

redis.call('hset', posCacheKey, 'express_limit_quantity', ARGV[2])
local pushLimit = tonumber(ARGV[2]);
local pushValue = tonumber(redis.call('hincrBy', posCacheKey, 'express_limit_value', '0'));
if (pushLimit > pushValue) then
    redis.call('srem', posLimitKey, ARGV[3] .. ARGV[1]);
else
    redis.call('sadd', posLimitKey, ARGV[3] .. ARGV[1]);
end