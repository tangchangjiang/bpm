local posLimitKey = 'o2md:pos:pick_up:limit';
local posCacheKey = 'o2md:pos:' .. ARGV[1];

redis.call('hset', posCacheKey, 'pick_up_limit_quantity', ARGV[2])
local pushLimit = tonumber(ARGV[2]);
local pushValue = tonumber(redis.call('hincrBy', posCacheKey, 'pick_up_limit_value', '0'));
if (pushLimit > pushValue) then
    redis.call('srem', posLimitKey, ARGV[1]);
else
    redis.call('sadd', posLimitKey, ARGV[1]);
end