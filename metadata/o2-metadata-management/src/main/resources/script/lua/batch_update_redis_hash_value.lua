local objMap = cjson.decode(ARGV[1]);

for hashKey, filedMap in pairs(objMap) do
    redis.call('del', hashKey);
    for fieldKey, fieldValue in pairs(filedMap) do
        redis.call("hset", hashKey, fieldKey, fieldValue);
    end
end
