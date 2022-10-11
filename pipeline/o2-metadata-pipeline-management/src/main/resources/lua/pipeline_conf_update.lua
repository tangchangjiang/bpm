local redisConfKey = KEYS[1];
if redis.call('exists', redisConfKey) == 0 then
    redis.call('hset', redisConfKey, 'info', ARGV[1]);
    redis.call('hset', redisConfKey, 'pipeline_version', ARGV[2]);
    return true;
end ;
if tonumber(redis.call('hget', redisConfKey, 'pipeline_version')) <= tonumber(ARGV[2]) then
    redis.call('hset', redisConfKey, 'info', ARGV[1]);
    redis.call('hset', redisConfKey, 'pipeline_version', ARGV[2]);
    return true;
else
    return false;
end ;