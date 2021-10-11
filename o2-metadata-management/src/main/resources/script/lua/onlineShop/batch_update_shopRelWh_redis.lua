local objMap = cjson.decode(ARGV[1]);

for i=1,#KEYS  do
    local map =  objMap[KEYS[i]];
    for fieldKey, fieldValue in pairs(map) do
        redis.call("hset",KEYS[i], fieldKey, fieldValue);
    end
end
