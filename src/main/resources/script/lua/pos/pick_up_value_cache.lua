local posLimitKey = 'o2md:pos:pick_up:limit';
local posCacheKey = 'o2md:pos:' .. ARGV[1];

local pushLimit = tonumber(redis.call('hincrBy', posCacheKey, 'pick_up_limit_quantity', '0'));
local pushValue = tonumber(redis.call('hincrBy', posCacheKey, 'pick_up_limit_value', ARGV[2]));
if (pushLimit > pushValue) then
    redis.call('srem', posLimitKey, ARGV[1]);
else
    redis.call('sadd', posLimitKey, ARGV[1]);
end