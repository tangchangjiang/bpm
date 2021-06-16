local hashKeya =  cjson.decode(ARGV[1]);

for hashKey, filedMap in pairs(hashKeya) do
    redis.call('del', hashKey);
end
