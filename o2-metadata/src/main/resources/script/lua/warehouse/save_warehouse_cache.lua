local hashKey = KEYS[1];
local configMap = cjson.decode(ARGV[1]);

for configKey, configValue in pairs(configMap) do
    redis.call('hset', hashKey, configKey, configValue);
end
