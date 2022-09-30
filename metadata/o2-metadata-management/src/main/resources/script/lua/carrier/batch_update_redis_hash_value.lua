local deleteMap =  cjson.decode(ARGV[2]);
local updateMap = cjson.decode(ARGV[1]);
local key = KEYS[1];
for fieldKey, filedMap in pairs(deleteMap) do
    redis.call("hdel", key, fieldKey);
end
for fieldKey, fieldValue in pairs(updateMap) do
    redis.call("hset", key, fieldKey, fieldValue);
end
