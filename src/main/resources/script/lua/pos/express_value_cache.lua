local posLimitKey = 'o2md:pos:express:limit';
local posCacheKey = 'o2md:pos:' .. ARGV[3] .. ARGV[1];

local pushLimit = tonumber(redis.call('hincrBy', posCacheKey, 'express_limit_quantity', '0'));
local pushValue = tonumber(redis.call('hincrBy', posCacheKey, 'express_limit_value', ARGV[2]));
if (pushLimit > pushValue) then
    redis.call('srem', posLimitKey, ARGV[3] .. ARGV[1]);
else
    redis.call('sadd', posLimitKey, ARGV[3] .. ARGV[1]);
end