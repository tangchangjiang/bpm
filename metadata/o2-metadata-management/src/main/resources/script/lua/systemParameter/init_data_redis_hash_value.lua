--map 类型系统参数
local map= cjson.decode(ARGV[3]);
for hashKey, filedMap in pairs(map) do
    for fieldKey, fieldValue in pairs(filedMap) do
        redis.call("hset", hashKey, fieldKey, fieldValue);
    end
end

-- kv 类型系统参数
local kvKey = KEYS[1]
local kvMap = cjson.decode(ARGV[1]);
for fieldKey, fieldValue in pairs(kvMap) do
    redis.call("hset", kvKey, fieldKey, fieldValue);
end

-- set 类型系统参数
local setKey = KEYS[2]
local setMap = cjson.decode(ARGV[2]);
for fieldKey, fieldValue in pairs(setMap) do
    redis.call("hset", setKey, fieldKey, fieldValue);
end
